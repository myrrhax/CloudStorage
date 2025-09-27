package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.model.entity.Role;
import com.myrr.CloudStorage.model.enums.RoleType;

public interface RoleService {
    Role getRole(final RoleType roleType);
}
