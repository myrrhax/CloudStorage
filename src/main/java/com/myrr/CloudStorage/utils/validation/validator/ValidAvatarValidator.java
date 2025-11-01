package com.myrr.CloudStorage.utils.validation.validator;

import com.myrr.CloudStorage.domain.exceptions.badrequest.InvalidFileExtensionException;
import com.myrr.CloudStorage.utils.FileStorageExtensions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class ValidAvatarValidator implements ConstraintValidator<ValidAvatar, MultipartFile> {
    private final Set<String> validAvatarExtensions;
    private final FileStorageExtensions fileStorageExtensions;

    public ValidAvatarValidator(FileStorageExtensions fileStorageExtensions,
            @Value("${file.storage.avatar-extensions}") Set<String> validAvatarExtensions) {
        this.fileStorageExtensions = fileStorageExtensions;
        this.validAvatarExtensions = validAvatarExtensions;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty())
            return false;

        String fileName = file.getOriginalFilename();
        if (fileName == null)
            return false;

        try {
            String extension = fileStorageExtensions.getExtension(fileName);

            return validAvatarExtensions.contains(extension);
        } catch (InvalidFileExtensionException e) {
            return false;
        }
    }
}
