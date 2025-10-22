package com.myrr.CloudStorage.domain.dto;

import com.myrr.CloudStorage.utils.validation.validator.NullableUUID;

import java.util.UUID;

public record CreateDirectoryDto(String name,
                                 @NullableUUID String parentId) {
}
