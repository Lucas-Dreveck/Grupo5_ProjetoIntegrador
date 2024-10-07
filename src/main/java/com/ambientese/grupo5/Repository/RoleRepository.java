package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ambientese.grupo5.Model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long>{
    
    RoleModel findByDescription(String description);
}
