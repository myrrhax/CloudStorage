package com.myrr.CloudStorage.security.expression;

import com.myrr.CloudStorage.domain.entity.FileMetadata;
import com.myrr.CloudStorage.security.JwtEntity;
import com.myrr.CloudStorage.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("fileSecurity")
public class FileSecurity {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileSecurity(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public boolean hasAccessToFile(UUID fileId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        if (user == null)
            return false;
        Long userId = user.getId();
        FileMetadata metadata = this.fileStorageService.getFileMetadata(fileId);

        return metadata.getOwner().getId().equals(userId);
    }
}
