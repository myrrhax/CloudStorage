package com.myrr.CloudStorage.domain.entity;

import com.myrr.CloudStorage.domain.enums.FileType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "files_metadata")
public class FileMetadata {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            mappedBy = "parent")
    private List<FileMetadata> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FileMetadata parent;

    @Column
    private Long size;

    @Column(name = "creation_time")
    private Instant creationTime;

    private FileMetadata() {
    }

    public FileMetadata(String name,
                        FileType type,
                        User owner,
                        FileMetadata parent,
                        long size) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.creationTime = Instant.now();
        this.parent = parent;
        this.size = size;
    }

    public void addChild(FileMetadata file) {
        this.children.add(file);
        file.setParent(file);
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

    public List<FileMetadata> getChildren() {
        return children;
    }

    public void setChildren(List<FileMetadata> children) {
        this.children = children;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public FileMetadata getParent() {
        return parent;
    }

    public void setParent(FileMetadata parent) {
        this.parent = parent;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
