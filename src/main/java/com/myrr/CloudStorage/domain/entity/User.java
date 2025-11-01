package com.myrr.CloudStorage.domain.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", unique = true, nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avatar_id")
    private FileMetadata avatar;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH },
            mappedBy = "user")
    private Set<RefreshToken> tokens;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<FileMetadata> files;

    public User(String name,
                String email,
                String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = null;
        this.isConfirmed = false;
        this.roles = new HashSet<>();
        this.tokens = new HashSet<>();
        this.files = new ArrayList<>();
    }

    private User() {
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<RefreshToken> getTokens() {
        return tokens;
    }

    public void setTokens(Set<RefreshToken> tokens) {
        this.tokens = tokens;
    }

    public List<FileMetadata> getFiles() {
        return files;
    }

    public void setFiles(List<FileMetadata> files) {
        this.files = files;
    }

    public FileMetadata getAvatar() {
        return avatar;
    }

    public void setAvatar(FileMetadata avatar) {
        this.avatar = avatar;
    }
}
