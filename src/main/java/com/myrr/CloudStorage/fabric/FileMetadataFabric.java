package com.myrr.CloudStorage.fabric;

import com.myrr.CloudStorage.domain.dto.FileDto;
import com.myrr.CloudStorage.domain.entity.FileMetadata;

public interface FileMetadataFabric {
    FileDto convert(FileMetadata metadata);
}
