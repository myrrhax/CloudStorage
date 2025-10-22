package com.myrr.CloudStorage.repository;

import com.myrr.CloudStorage.domain.entity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {
    Page<FileMetadata> findAllByParentId(UUID parentId, Pageable pageable);
}
