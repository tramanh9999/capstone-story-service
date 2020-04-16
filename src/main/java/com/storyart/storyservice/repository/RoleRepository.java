package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
