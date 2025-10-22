package com.myrr.CloudStorage.domain.dto;

import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;

public record CreateDirectoryDto(String name,
                                 @NullableUUID String parentId) {
}
