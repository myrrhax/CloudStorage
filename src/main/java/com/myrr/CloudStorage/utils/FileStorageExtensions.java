package com.myrr.CloudStorage.utils;

import com.myrr.CloudStorage.domain.exceptions.badrequest.FileCannotBeNullException;
import com.myrr.CloudStorage.domain.exceptions.badrequest.InvalidFileExtensionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Component
public class FileStorageExtensions {
    public static final String EMPTY_UUID_PATTERN = "00000000-0000-0000-0000-000000000000";
    public static final UUID EMPTY_UUID = UUID.fromString(EMPTY_UUID_PATTERN);
    public static final String FILE_PATTERN = "%d/%s";

    @Value("${file.storage.extensions}")
    private Set<String> validFileExtensions;

    public String getExtension(String name) {
        int lastPointIndex = name.lastIndexOf('.');
        if (lastPointIndex == -1 || lastPointIndex == name.length() - 1)
            throw new InvalidFileExtensionException();

        return name.substring(lastPointIndex);
    }

    public void validateFileExtension(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            throw new FileCannotBeNullException();
        }

        String extension = getExtension(file.getOriginalFilename());
        if (!validFileExtensions.contains(extension))
            throw new InvalidFileExtensionException();
    }

    public UUID parseNullableUUID(String uuid) {
         return uuid == null || uuid.isBlank()
                ? EMPTY_UUID
                : UUID.fromString(uuid);
    }

    public String getFileName(Long userId, String fileName) {
        return String.format(FILE_PATTERN, userId, fileName);
    }

    public void setValidFileExtensions(Set<String> validFileExtensions) {
        this.validFileExtensions = validFileExtensions;
    }
}
