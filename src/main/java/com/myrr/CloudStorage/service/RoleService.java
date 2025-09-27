package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.entity.Role;
import com.myrr.CloudStorage.domain.enums.RoleType;

public interface RoleService {
    Role getRole(final RoleType roleType);
}
