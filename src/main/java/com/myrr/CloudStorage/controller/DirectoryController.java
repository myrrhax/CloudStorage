package com.myrr.CloudStorage.controller;

import com.myrr.CloudStorage.domain.dto.CreateDirectoryDto;
import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.security.JwtEntity;
import com.myrr.CloudStorage.service.FileStorageService;
import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/directories")
@Validated
public class DirectoryController {
    private final FileStorageService fileStorageService;

    @Autowired
    public DirectoryController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<FileDto> addDirectory(@RequestBody CreateDirectoryDto createDirectoryDto,
                                                @AuthenticationPrincipal UserDetails userDetails) {

        JwtEntity jwtEntity = (JwtEntity) userDetails;
        long userId = jwtEntity.getId();

        FileDto dto = this.fileStorageService.createDirectory(createDirectoryDto.name(),
                createDirectoryDto.parentId(),
                userId);

        return ResponseEntity
                .created(URI.create(dto.getUrl()))
                .body(dto);
    }

    @GetMapping("{id}")
    @PreAuthorize("@fileSecurity.hasAccessToFile(#id, authentication)")
    public ResponseEntity<Page<FileDto>> lookupDirectory(@PathVariable @NullableUUID String id,
                                                      @RequestParam int page,
                                                      @RequestParam int pageSize,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        JwtEntity jwtEntity = (JwtEntity) userDetails;
        long userId = jwtEntity.getId();

        Page<FileDto> fileData = this.fileStorageService.lookupDirectory(id,
                userId,
                page,
                pageSize);

        return ResponseEntity.ok(fileData);
    }
}
