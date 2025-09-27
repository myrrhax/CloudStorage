package com.myrr.CloudStorage.service.impl;

import com.myrr.CloudStorage.domain.entity.Role;
import com.myrr.CloudStorage.domain.enums.RoleType;
import com.myrr.CloudStorage.repository.RoleRepository;
import com.myrr.CloudStorage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRole(RoleType roleType) {
        return this.roleRepository.findByRole(roleType);
    }
}
