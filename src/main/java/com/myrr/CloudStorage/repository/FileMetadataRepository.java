package com.myrr.CloudStorage.repository;

import com.myrr.CloudStorage.domain.entity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {
    @Query("""
        SELECT fm
        FROM FileMetadata fm
        WHERE fm.owner.id = :ownerId
          AND ((fm.parent IS NULL AND :parentId IS NULL) 
            OR (fm.parent IS NOT NULL AND fm.parent.id = :parentId))
          AND fm.type != com.myrr.CloudStorage.domain.enums.FileType.AVATAR
       """)
    Page<FileMetadata> findAllByParentIdAndOwnerId(UUID parentId,
                                                   long ownerId,
                                                   Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(fm.size), 0)
            FROM FileMetadata fm
            WHERE fm.type = com.myrr.CloudStorage.domain.enums.FileType.FILE
              AND fm.owner.id = :userId
        """)
    long getFilledSpace(@Param("userId") long userId);
}
