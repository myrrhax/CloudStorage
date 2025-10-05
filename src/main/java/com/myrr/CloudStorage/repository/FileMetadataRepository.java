package com.myrr.CloudStorage.repository;

import com.myrr.CloudStorage.domain.entity.FileMetadata;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FileMetadataRepository extends CrudRepository<FileMetadata, UUID> {

}
