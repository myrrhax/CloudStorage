package com.myrr.CloudStorage.controller;

import com.myrr.CloudStorage.domain.dto.CreateDirectoryDto;
import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.security.JwtEntity;
import com.myrr.CloudStorage.service.FileStorageService;
import com.myrr.CloudStorage.utils.FileStorageExtensions;
import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Директории", description = "Контроллер для создания/просмотра содержимого директорий")
public class DirectoryController {
    private final FileStorageService fileStorageService;

    @Autowired
    public DirectoryController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    @Operation(summary = "Добавление новой директории", description = "Создание новой директории в виртуальной ФС пользователя")
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
    @Operation(summary = "Просмотр содержимого директории", description = "Возвращает список файлов, содержащихся в директории")
    public ResponseEntity<Page<FileDto>> lookupDirectory(
                                                  @PathVariable
                                                  @NullableUUID
                                                  @Parameter(
                                                      description = "Id директории (или пустой UUID)",
                                                      schema = @Schema(
                                                          type = "string",
                                                          format = "uuid",
                                                          defaultValue = "00000000-0000-0000-0000-000000000000"
                                                      )
                                                  )
                                                  String id,
                                                  @RequestParam(required = false, defaultValue = "0")
                                                  @Parameter(name = "Номер страницы")
                                                  int page,
                                                  @RequestParam(required = false, defaultValue = "15")
                                                  @Parameter(name = "Число элементов на странице")
                                                  int pageSize,
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
