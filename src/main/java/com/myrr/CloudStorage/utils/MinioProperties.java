package com.myrr.CloudStorage.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String avatarBucket;
    private String filesBucket;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAvatarBucket() {
        return avatarBucket;
    }

    public void setAvatarBucket(String avatarBucket) {
        this.avatarBucket = avatarBucket;
    }

    public String getFilesBucket() {
        return filesBucket;
    }

    public void setFilesBucket(String filesBucket) {
        this.filesBucket = filesBucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
