package com.myrr.CloudStorage.controller;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.dto.FilledSpaceResponse;
import com.myrr.CloudStorage.security.JwtEntity;
import com.myrr.CloudStorage.service.FileStorageService;
import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;
import com.myrr.CloudStorage.utils.validation.validator.ValidAvatar;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/files")
@Validated
@Tag(name = "Файлы", description = "Контроллер файлов")
public class FileController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка", description = "Загрузка файла на сервер")
    public ResponseEntity<FileDto> loadFile(@RequestPart("file") @NotNull @Parameter(name = "Файл") MultipartFile file,
                                            @RequestParam(value = "parent", required = false) @NullableUUID @Parameter(description = "Id родительской директории") String parentDirectoryId,
                                            @RequestParam(value = "displayName", required = false) @Parameter(description = "Имя файла") String displayName,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        JwtEntity jwtEntity = (JwtEntity) userDetails;
        long userId = jwtEntity.getId();

        FileDto dto = this.fileStorageService.loadFileToServer(file,
                userId,
                displayName,
                parentDirectoryId);

        return ResponseEntity
                .created(URI.create(dto.getUrl()))
                .body(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@fileSecurity.hasAccessToFile(#id, authentication)")
    @Operation(summary = "Скачивание", description = "Скачивание файла с сервера")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable @UUID String id) {
        FileDto fileDto = this.fileStorageService.downloadFile(java.util.UUID.fromString(id));
        MediaType mediaType = MediaTypeFactory.getMediaType(fileDto.getName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getId() + "\"")
                .body(new InputStreamResource(fileDto.getFileStream()));
    }

    @PutMapping
    @PreAuthorize("@fileSecurity.hasAccessToFile(#dto.getId(), authentication)")
    @Operation(summary = "Обновление", description = "Обновление метаданных о файле/перенос файла")
    public ResponseEntity<FileDto> updateFile(@RequestBody FileDto dto) {
        FileDto result = this.fileStorageService.updateFileMetadata(dto);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@fileSecurity.hasAccessToFile(#id, authentication)")
    @Operation(summary = "Удаление", description = "Удаление файла с сервера")
    public ResponseEntity<Void> deleteFile(@PathVariable java.util.UUID id) {
        this.fileStorageService.deleteFile(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/avatars")
    @Operation(summary = "Загрузка аватара")
    public ResponseEntity<FileDto> loadAvatar(@RequestPart("avatar") @ValidAvatar @Parameter(name = "Файл") MultipartFile file) {
        FileDto dto = this.fileStorageService.loadAvatar(file);

        return ResponseEntity
                .ok(dto);
    }

    @GetMapping("/filledSpace")
    @Operation(summary = "Ифнормация о занятости диска")
    public ResponseEntity<FilledSpaceResponse> getFilledSpaceInfo() {
        return ResponseEntity
                .ok(this.fileStorageService.getFilledSpaceInfo());
    }
}
