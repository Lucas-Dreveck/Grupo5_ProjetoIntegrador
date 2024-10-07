package com.ambientese.grupo5.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ambientese.grupo5.Model.EmployeeModel;

public interface EmployeeRepository extends JpaRepository<EmployeeModel, Long>{
    @Query("SELECT e FROM EmployeeModel e ORDER BY e.name ASC")
    List<EmployeeModel> findAllOrderByNameAsc(); 

    List<EmployeeModel> findAllByNameStartingWithIgnoreCaseOrderByNameAsc(String name);

    @Query("SELECT e FROM EmployeeModel e WHERE LOWER(e.role.description) LIKE LOWER(CONCAT(:roleDescription, '%')) ORDER BY e.name ASC")
    List<EmployeeModel> findAllByRoleDescriptionOrderByNameAsc(@Param("roleDescription") String roleDescription);
    
    EmployeeModel findByEmail(String email);

    EmployeeModel findByUserId(Long id);

}
