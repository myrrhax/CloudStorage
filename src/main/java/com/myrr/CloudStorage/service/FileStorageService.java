package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface FileStorageService {
    FileDto loadFileToServer(MultipartFile file,
                             Long userId,
                             String filename,
                             String parentDirectoryId);
    FileMetadata getFileMetadata(UUID fileId);
    FileDto downloadFile(UUID fileId);
}
