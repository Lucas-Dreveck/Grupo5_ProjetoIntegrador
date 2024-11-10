package com.ambientese.grupo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ambientese.grupo5.model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long>{
    
    RoleModel findByDescription(String description);
}
