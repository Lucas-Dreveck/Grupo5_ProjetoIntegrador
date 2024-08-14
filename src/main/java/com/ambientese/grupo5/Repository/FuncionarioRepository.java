package com.ambientese.grupo5.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ambientese.grupo5.Model.FuncionarioModel;

public interface FuncionarioRepository extends JpaRepository<FuncionarioModel, Long>{
    @Query("SELECT f FROM FuncionarioModel f ORDER BY f.nome ASC")
    List<FuncionarioModel> findAllOrderByNomeAsc(); 

    List<FuncionarioModel> findAllByNomeStartingWithIgnoreCaseOrderByNomeAsc(String nome);

    @Query("SELECT f FROM FuncionarioModel f WHERE LOWER(f.cargo.descricao) LIKE LOWER(CONCAT(:cargoDescricao, '%')) ORDER BY f.nome ASC")
    List<FuncionarioModel> findAllByCargoDescricaoOrderByNomeAsc(@Param("cargoDescricao") String cargoDescricao);
    
    FuncionarioModel findByEmail(String email);

    FuncionarioModel findByUsuarioId(Long id);

}
