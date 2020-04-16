package com.storyart.storyservice.service;

import com.storyart.storyservice.model.Role;
import com.storyart.storyservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface RoleService {
    Role findRoleById(int id);
}

@Service
class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findRoleById(int id) {
        return roleRepository.findById(id).orElse(null);
    }
}
