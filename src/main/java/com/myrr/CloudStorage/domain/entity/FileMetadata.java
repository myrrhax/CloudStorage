package com.myrr.CloudStorage.domain.entity;

import com.myrr.CloudStorage.domain.enums.FileType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "files_metadata")
public class FileMetadata {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_url", unique = true, nullable = false)
    private String url;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private FileMetadata() {
    }

    public FileMetadata(String url, String name, FileType type, User owner) {
        this.url = url;
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
