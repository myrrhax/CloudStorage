package com.myrr.CloudStorage.service.impl;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;
import com.myrr.CloudStorage.domain.entity.User;
import com.myrr.CloudStorage.domain.enums.FileType;
import com.myrr.CloudStorage.domain.exceptions.UnableToLoadFileException;
import com.myrr.CloudStorage.domain.exceptions.badrequest.FileCannotBeNullException;
import com.myrr.CloudStorage.domain.exceptions.badrequest.InvalidFileExtensionException;
import com.myrr.CloudStorage.domain.exceptions.badrequest.InvalidParentException;
import com.myrr.CloudStorage.domain.exceptions.conflict.FileAlreadyExistsException;
import com.myrr.CloudStorage.domain.exceptions.notfound.ApplicationFileNotFoundException;
import com.myrr.CloudStorage.domain.exceptions.notfound.DirectoryNotFoundException;
import com.myrr.CloudStorage.domain.exceptions.notfound.FilesNotFoundException;
import com.myrr.CloudStorage.domain.exceptions.notfound.UserNotFoundException;
import com.myrr.CloudStorage.fabric.FileMetadataFabric;
import com.myrr.CloudStorage.repository.FileMetadataRepository;
import com.myrr.CloudStorage.repository.UserRepository;
import com.myrr.CloudStorage.service.FileStorageService;
import com.myrr.CloudStorage.utils.FileStorageExtensions;
import com.myrr.CloudStorage.utils.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional
public class MinioFileStorage implements FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(MinioFileStorage.class);

    private final MinioClient minioClient;
    private final FileMetadataRepository fileMetadataRepository;
    private final MinioProperties minioProperties;
    private final UserRepository userRepository;
    private final FileStorageExtensions fileStorageExtensions;
    private final FileMetadataFabric metadataFabric;


    @Autowired
    public MinioFileStorage(MinioClient minioClient,
                            FileMetadataRepository fileMetadataRepository,
                            MinioProperties minioProperties,
                            UserRepository userRepository, FileStorageExtensions fileStorageExtensions, FileMetadataFabric metadataFabric) {
        this.minioClient = minioClient;
        this.fileMetadataRepository = fileMetadataRepository;
        this.minioProperties = minioProperties;
        this.userRepository = userRepository;
        this.fileStorageExtensions = fileStorageExtensions;
        this.metadataFabric = metadataFabric;
    }

    @Override
    public FileDto loadFileToServer(MultipartFile file,
                                    Long userId,
                                    String filename,
                                    String parentDirectoryId) {
        if (file == null || file.getSize() == 0) {
            throw new FileCannotBeNullException();
        }
        this.fileStorageExtensions.validateFileExtension(file);

        if (filename == null || filename.isBlank()) {
            filename = file.getOriginalFilename();
        } else {
            try {
                 this.fileStorageExtensions.getExtension(filename);
            } catch (InvalidFileExtensionException ex) {
                if (file.getOriginalFilename() == null)
                    throw ex;

                filename = filename + this.fileStorageExtensions.getExtension(file.getOriginalFilename());
            }
        }

        UUID usedDirectoryId = this.fileStorageExtensions.parseNullableUUID(parentDirectoryId);
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        FileMetadata parent = usedDirectoryId.equals(UUID.fromString(FileStorageExtensions.EMPTY_UUID_PATTERN))
                ? null
                : this.fileMetadataRepository.findById(usedDirectoryId)
                    .orElseThrow(() -> new DirectoryNotFoundException(usedDirectoryId));

        if (parent != null && !parent.getType().equals(FileType.DIRECTORY)) {
            throw new InvalidParentException();
        }

        FileMetadata metadata = new FileMetadata(filename, FileType.FILE, user, parent, file.getSize());

        try {
            FileMetadata savedEntity = this.fileMetadataRepository.save(metadata);
            this.fileMetadataRepository.flush();
            String name = this.fileStorageExtensions.getFileName(userId,
                    savedEntity.getId().toString(),
                    usedDirectoryId);
            this.minioClient.putObject(PutObjectArgs.builder()
                    .object(name)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .bucket(this.minioProperties.getFilesBucket())
                    .build());
        } catch (DataIntegrityViolationException ex) {
            log.error("File already exists ", ex);
            throw new FileAlreadyExistsException();
        } catch (Exception ex) {
            log.error("An error occurred file loading ", ex);
            throw new UnableToLoadFileException();
        }

        return metadataFabric.convert(metadata);
    }

    @Override
    public FileMetadata getFileMetadata(UUID fileId) {
        return this.fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new ApplicationFileNotFoundException(fileId));
    }

    @Override
    public FileDto downloadFile(UUID fileId) {
        FileMetadata fileMetadata = getFileMetadata(fileId);
        String parentId = fileMetadata.getParent() == null
                ? FileStorageExtensions.EMPTY_UUID_PATTERN
                : fileMetadata.getParent().getId().toString();
        String minioName = String.format(FileStorageExtensions.FILE_PATTERN,
                fileMetadata.getOwner().getId(),
                parentId,
                fileMetadata.getId().toString());

        log.info("Trying to get: {} file", minioName);
        try {
            InputStream stream = this.minioClient
                    .getObject(GetObjectArgs.builder()
                            .bucket(minioProperties.getFilesBucket())
                            .object(minioName)
                            .build());

            return new FileDto(fileMetadata.getId(),
                    fileMetadata.getName(),
                    fileMetadata.getType(),
                    stream);

        } catch (Exception e) {
            log.error("Unable to get file", e);
            throw new UnableToLoadFileException();
        }
    }

    @Override
    public FileDto createDirectory(String directoryName, long userId) {
        return null;
    }

    @Override
    public Page<FileDto> lookupDirectory(String directoryId, int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("name"));

        Page<FileMetadata> result = this.fileMetadataRepository.findAllByParentId(
                this.fileStorageExtensions.parseNullableUUID(directoryId),
                pageRequest);

        if (result.getTotalElements() == 0 || result.getSize() == 0)
            throw new FilesNotFoundException();

        return result.map(this.metadataFabric::convert);
    }
}
