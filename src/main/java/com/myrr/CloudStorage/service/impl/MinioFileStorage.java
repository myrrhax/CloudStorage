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
import com.myrr.CloudStorage.domain.exceptions.notfound.UserNotFoundException;
import com.myrr.CloudStorage.repository.FileMetadataRepository;
import com.myrr.CloudStorage.repository.UserRepository;
import com.myrr.CloudStorage.service.FileStorageService;
import com.myrr.CloudStorage.utils.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class MinioFileStorage implements FileStorageService {
    public static final String FILE_PATTERN = "%d/%s/%s";

    private static final String EMPTY_UUID_PATTERN = "00000000-0000-0000-0000-000000000000";
    private static final Logger log = LoggerFactory.getLogger(MinioFileStorage.class);

    private final MinioClient minioClient;
    private final FileMetadataRepository fileMetadataRepository;
    private final MinioProperties minioProperties;
    private final UserRepository userRepository;

    @Value("${file.storage.extensions}")
    private Set<String> validFileExtensions;

    @Value("${file.server.path}")
    private String fileServerUrl;

    private String fileServerFileEndpoint;

    @Autowired
    public MinioFileStorage(MinioClient minioClient,
                            FileMetadataRepository fileMetadataRepository,
                            MinioProperties minioProperties,
                            UserRepository userRepository) {
        this.minioClient = minioClient;
        this.fileMetadataRepository = fileMetadataRepository;
        this.minioProperties = minioProperties;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        this.fileServerFileEndpoint = fileServerUrl + "{id}";
    }

    @Override
    public FileDto loadFileToServer(MultipartFile file,
                                    Long userId,
                                    String filename,
                                    String parentDirectoryId) {
        if (file == null || file.getSize() == 0) {
            throw new FileCannotBeNullException();
        }
        validateFileExtension(file);

        if (filename == null || filename.isBlank()) {
            filename = file.getOriginalFilename();
        } else {
            try {
                 getExtension(filename);
            } catch (InvalidFileExtensionException ex) {
                if (file.getOriginalFilename() == null)
                    throw ex;

                filename = filename + getExtension(file.getOriginalFilename());
            }
        }

        final UUID usedDirectoryId = parentDirectoryId == null || parentDirectoryId.isBlank()
                ? UUID.fromString(EMPTY_UUID_PATTERN)
                : UUID.fromString(parentDirectoryId);

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        FileMetadata parent = usedDirectoryId.equals(UUID.fromString(EMPTY_UUID_PATTERN))
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
            String name = FILE_PATTERN.formatted(userId, usedDirectoryId, savedEntity.getId().toString());
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

        return new FileDto(metadata.getId(),
                metadata.getName(),
                UriComponentsBuilder
                        .fromUriString(this.fileServerFileEndpoint)
                        .buildAndExpand(metadata.getId())
                        .toUriString(),
                FileType.FILE);
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
                ? EMPTY_UUID_PATTERN
                : fileMetadata.getParent().getId().toString();
        String minioName = String.format(FILE_PATTERN,
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

    private static String getExtension(String name) {
        int lastPointIndex = name.indexOf('.');
        if (lastPointIndex == -1 || lastPointIndex == name.length() - 1)
            throw new InvalidFileExtensionException();

        return name.substring(lastPointIndex);
    }

    private void validateFileExtension(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            throw new FileCannotBeNullException();
        }

        String extension = getExtension(file.getOriginalFilename());
        if (!validFileExtensions.contains(extension))
            throw new InvalidFileExtensionException();
    }

    public void setValidFileExtensions(Set<String> validFileExtensions) {
        this.validFileExtensions = validFileExtensions;
    }

    public void setFileServerUrl(String fileServerUrl) {
        this.fileServerUrl = fileServerUrl;
    }
}
