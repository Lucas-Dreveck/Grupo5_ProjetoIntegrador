package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ambientese.grupo5.Model.EmpresaModel;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<EmpresaModel, Long> {

    @Query("SELECT e FROM EmpresaModel e ORDER BY e.nomeFantasia ASC")
    List<EmpresaModel> findAllOrderByNomeFantasiaAsc();    

    @Query("SELECT DISTINCT e.ramo FROM EmpresaModel e")
    List<String> findDistinctRamos();

    List<EmpresaModel> findAllByNomeFantasiaStartingWithIgnoreCaseOrderByNomeFantasiaAsc(String nomeFantasia);

    List<EmpresaModel> findAllByRazaoSocialStartingWithIgnoreCaseOrderByRazaoSocialAsc(String razaoSocial);

    List<EmpresaModel> findAllByRamoStartingWithIgnoreCaseOrderByNomeFantasiaAsc(String ramo);
    
    Optional<EmpresaModel> findByCnpj(String cnpj);
}
