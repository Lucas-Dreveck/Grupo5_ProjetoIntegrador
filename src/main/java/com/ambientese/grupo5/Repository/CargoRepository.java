package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ambientese.grupo5.Model.CargoModel;

public interface CargoRepository extends JpaRepository<CargoModel, Long>{
    
    CargoModel findByDescricao(String descricao);
}
