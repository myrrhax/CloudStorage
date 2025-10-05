package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface FileStorageService {
    FileDto loadFile(MultipartFile file,
                     Long userId,
                     String filename,
                     String parentDirectoryId);
}
