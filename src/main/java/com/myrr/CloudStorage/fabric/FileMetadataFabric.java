package com.myrr.CloudStorage.fabric;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;

import java.io.InputStream;

public interface FileMetadataFabric {
    FileDto convert(FileMetadata metadata);
    FileDto convert(FileMetadata metadata, InputStream inputStream);
}
