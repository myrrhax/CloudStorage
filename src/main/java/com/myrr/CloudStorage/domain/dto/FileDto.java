package com.myrr.CloudStorage.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myrr.CloudStorage.domain.enums.FileType;

import java.io.InputStream;
import java.util.UUID;

public class FileDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;

    @JsonProperty("file_type")
    private FileType fileType;

    @JsonIgnore
    private InputStream fileStream;

    public FileDto(UUID id,
                   String name,
                   String url,
                   FileType fileType) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.fileType = fileType;
    }

    public FileDto(UUID id,
                   String name,
                   FileType fileType,
                   InputStream fileStream) {
        this.id = id;
        this.name = name;
        this.fileType = fileType;
        this.fileStream = fileStream;
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
}
