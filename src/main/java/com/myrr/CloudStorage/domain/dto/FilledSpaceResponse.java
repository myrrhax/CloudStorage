package com.myrr.CloudStorage.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FilledSpaceResponse(
   @JsonProperty
   long filledSpace,
   @JsonProperty
   long maxSpace
) {

}
