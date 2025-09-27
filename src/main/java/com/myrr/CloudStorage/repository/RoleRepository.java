package com.myrr.CloudStorage.repository;

import com.myrr.CloudStorage.model.entity.Role;
import com.myrr.CloudStorage.model.enums.RoleType;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByRole(RoleType role);
}
