package com.myrr.CloudStorage.fabric.impl;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;
import com.myrr.CloudStorage.domain.enums.FileType;
import com.myrr.CloudStorage.fabric.FileMetadataFabric;
import com.myrr.CloudStorage.utils.FileStorageExtensions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.util.UUID;

@Component
public class FileMetadataDefaultImpl implements FileMetadataFabric {

    @Value("${file.server.path}")
    private String fileServerUrl;

    @Override
    public FileDto convert(FileMetadata metadata) {
        return new FileDto(metadata.getId(),
                metadata.getName(),
                getFileUrl(metadata),
                metadata.getType());
    }

    @Override
    public FileDto convert(FileMetadata metadata, InputStream inputStream) {
        FileDto dto = this.convert(metadata);
        dto.setFileStream(inputStream);

        return dto;
    }

    @NotNull
    private static FileDto getParentDirectoryPlaceholder() {
        return new FileDto(UUID.fromString(FileStorageExtensions.EMPTY_UUID_PATTERN),
                "",
                "",
                FileType.DIRECTORY);
    }

    private String getFileUrl(FileMetadata metadata) {
        String uriString = this.fileServerUrl + (metadata.getType().equals(FileType.FILE)
                ? "/files/"
                : "/directories/"
            ) + metadata.getId();

        return UriComponentsBuilder
            .fromUriString(uriString)
            .buildAndExpand(metadata.getId())
            .toUriString();
    }

    public void setFileServerUrl(String fileServerUrl) {
        this.fileServerUrl = fileServerUrl;
    }
}
