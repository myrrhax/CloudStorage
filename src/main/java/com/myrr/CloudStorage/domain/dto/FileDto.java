package com.myrr.CloudStorage.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myrr.CloudStorage.domain.enums.FileType;
import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.InputStream;
import java.util.UUID;

public class FileDto {

    @JsonProperty("id")
    @NotNull
    private UUID id;

    @JsonProperty("name")
    @NotEmpty
    private String name;

    @JsonProperty(value = "url", access = JsonProperty.Access.READ_ONLY)
    private String url;

    @JsonProperty(value = "fileType", access = JsonProperty.Access.READ_ONLY)
    private FileType fileType;

    @JsonProperty(value = "ownerId", access = JsonProperty.Access.READ_ONLY)
    private Long ownerId;

    @JsonProperty(value = "parentId")
    @NullableUUID
    private UUID parentId;

    @JsonIgnore
    private InputStream fileStream;

    public FileDto(UUID id,
                   String name,
                   String url,
                   FileType fileType,
                   Long ownerId, UUID parentId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.fileType = fileType;
        this.ownerId = ownerId;
        this.parentId = parentId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
