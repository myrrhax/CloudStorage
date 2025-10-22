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
import java.util.Optional;
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

        Optional<FileMetadata> parent = getParentById(usedDirectoryId);
        validateParentType(parent);

        FileMetadata metadata = new FileMetadata(filename,
                FileType.FILE,
                user,
                parent.orElse(null),
                file.getSize());

        try {
            FileMetadata savedEntity = this.fileMetadataRepository.saveAndFlush(metadata);
            String name = this.fileStorageExtensions.getFileName(userId,
                    savedEntity.getId().toString());
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
    public FileDto updateFileMetadata(FileDto updatedDto) {
        FileMetadata metadata = this.fileMetadataRepository.findById(updatedDto.getId())
                .orElseThrow(FilesNotFoundException::new);
        FileMetadata newParent = this.fileMetadataRepository.findById(updatedDto.getParentId())
                        .orElseThrow(FilesNotFoundException::new);

        try {
            metadata.setName(updatedDto.getName());
            metadata.setParent(newParent);
            this.fileMetadataRepository.flush();

            return this.metadataFabric.convert(metadata);
        } catch (Exception ex) {
            throw new FileAlreadyExistsException();
        }
    }

    @Override
    public FileMetadata getFileMetadata(UUID fileId) {
        return this.fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new ApplicationFileNotFoundException(fileId));
    }

    @Override
    public FileDto downloadFile(UUID fileId) {
        FileMetadata fileMetadata = getFileMetadata(fileId);
        String minioName = this.fileStorageExtensions.getFileName(
            fileMetadata.getOwner().getId(),
            fileMetadata.getId().toString()
        );

        log.info("Trying to get: {} file", minioName);
        try {
            InputStream stream = this.minioClient
                    .getObject(GetObjectArgs.builder()
                            .bucket(minioProperties.getFilesBucket())
                            .object(minioName)
                            .build());

            return this.metadataFabric.convert(fileMetadata, stream);

        } catch (Exception e) {
            log.error("Unable to get file", e);
            throw new UnableToLoadFileException();
        }
    }

    @Override
    public FileDto createDirectory(String directoryName, String parentId, long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UUID parentUuid = this.fileStorageExtensions.parseNullableUUID(parentId);
        Optional<FileMetadata> parentMetadata = getParentById(parentUuid);
        validateParentType(parentMetadata);

        FileMetadata metadata = new FileMetadata(directoryName,
                FileType.DIRECTORY,
                user,
                parentMetadata.orElse(null),
                0);
        try {
            this.fileMetadataRepository.saveAndFlush(metadata);

            log.info("New directory {} for user {} was created", directoryName, userId);
            return this.metadataFabric.convert(metadata);
        } catch (Exception ex) {
            log.error("An error occurred directory creation for user {}",
                    userId, ex);
            throw new FileAlreadyExistsException();
        }
    }

    @Override
    public Page<FileDto> lookupDirectory(String directoryId, long ownerId, int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("name"));
        UUID parentId = this.fileStorageExtensions.parseNullableUUID(directoryId);

        Optional<FileMetadata> parent = getParentById(parentId);
        validateParentType(parent);

        Page<FileMetadata> result = this.fileMetadataRepository.findAllByParentIdAndOwnerId(
                parent.isPresent() ? parentId : null,
                ownerId,
                pageRequest);

        if (result.isEmpty())
            throw new FilesNotFoundException();

        return result.map(this.metadataFabric::convert);
    }

    private Optional<FileMetadata> getParentById(UUID usedDirectoryId) {
        return usedDirectoryId.equals(FileStorageExtensions.EMPTY_UUID)
                ? Optional.empty()
                : this.fileMetadataRepository.findById(usedDirectoryId);
    }

    private static void validateParentType(Optional<FileMetadata> parentMetadata) {
        if (parentMetadata.isPresent() && !parentMetadata.get().getType().equals(FileType.DIRECTORY))
            throw new InvalidParentException();
    }
}
