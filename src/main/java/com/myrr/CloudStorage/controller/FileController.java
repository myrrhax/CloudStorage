package com.myrr.CloudStorage.controller;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.security.JwtEntity;
import com.myrr.CloudStorage.service.FileStorageService;
import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/files")
@Validated
public class FileController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("")
    public ResponseEntity<FileDto> loadFile(@RequestPart("file") @NotNull MultipartFile file,
                                            @RequestParam("parent") @NullableUUID String parentDirectoryId,
                                            @RequestParam("displayName") String displayName,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        JwtEntity jwtEntity = (JwtEntity) userDetails;
        long userId = jwtEntity.getId();

        FileDto dto = this.fileStorageService.loadFile(file,
                userId,
                displayName,
                parentDirectoryId);

        return ResponseEntity
                .created(URI.create(dto.getUrl()))
                .body(dto);
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> loadAvatar(@RequestPart @NotNull MultipartFile file,
                                             @RequestParam String displayName) {
        return ResponseEntity.ok(file.getOriginalFilename() + " " + displayName);
    }


}
