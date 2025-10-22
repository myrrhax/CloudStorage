package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorageService {
    FileDto loadFileToServer(MultipartFile file,
                             Long userId,
                             String filename,
                             String parentDirectoryId);
    FileMetadata getFileMetadata(UUID fileId);
    FileDto downloadFile(UUID fileId);
    FileDto createDirectory(String directoryName, String parentId, long userId);
    Page<FileDto> lookupDirectory(String directoryId, long ownerId, int page, int pageSize);
}
