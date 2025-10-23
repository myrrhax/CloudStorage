package com.myrr.CloudStorage.domain.dto;

import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto для создания директории")
public record CreateDirectoryDto(@Schema(description = "Имя директории", example = "my directory") String name,
                                 @Schema(description = "Id родителя (NULL / UUID)") @NullableUUID String parentId) {
}
