package com.myrr.CloudStorage.fabric.impl;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;
import com.myrr.CloudStorage.domain.enums.FileType;
import com.myrr.CloudStorage.fabric.FileMetadataFabric;
import com.myrr.CloudStorage.utils.FileStorageExtensions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;

@Component
public class FileMetadataDefaultImpl implements FileMetadataFabric {

    @Value("${file.server.path}")
    private String fileServerUrl;

    @Override
    public FileDto convert(FileMetadata metadata) {
        return new FileDto(metadata.getId(),
                metadata.getName(),
                getFileUrl(metadata),
                metadata.getType(),
                metadata.getOwner().getId(),
                metadata.getParent() == null ? FileStorageExtensions.EMPTY_UUID : metadata.getParent().getId()
        );
    }

    @Override
    public FileDto convert(FileMetadata metadata, InputStream inputStream) {
        FileDto dto = this.convert(metadata);
        dto.setFileStream(inputStream);

        return dto;
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
