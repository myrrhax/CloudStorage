package com.myrr.CloudStorage.fabric.impl;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;
import com.myrr.CloudStorage.domain.enums.FileType;
import com.myrr.CloudStorage.fabric.FileMetadataFabric;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FileMetadataDefaultImpl implements FileMetadataFabric {

    @Value("${file.server.path}")
    private String fileServerUrl;

    private String fileServerFileEndpoint;

    @PostConstruct
    public void init() {
        this.fileServerFileEndpoint = fileServerUrl + "{id}";
    }

    @Override
    public FileDto convert(FileMetadata metadata) {
        return new FileDto(metadata.getId(),
                metadata.getName(),
                UriComponentsBuilder
                        .fromUriString(this.fileServerFileEndpoint)
                        .buildAndExpand(metadata.getId())
                        .toUriString(),
                FileType.FILE);
    }

    public void setFileServerUrl(String fileServerUrl) {
        this.fileServerUrl = fileServerUrl;
    }
}
