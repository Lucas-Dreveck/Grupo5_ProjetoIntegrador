package com.ambientese.grupo5.Repository;

import com.ambientese.grupo5.Model.FormularioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FormularioRepository extends JpaRepository<FormularioModel, Long>, JpaSpecificationExecutor<FormularioModel> {
    
    @Query("SELECT f FROM FormularioModel f WHERE f.id IN (SELECT MAX(f2.id) FROM FormularioModel f2 GROUP BY f2.empresa.id) ORDER BY f.pontuacaoFinal DESC")
    List<FormularioModel> findLatestByEmpresaOrderByPontuacaoFinalDesc();

    @Query("SELECT f FROM FormularioModel f WHERE f.id IN (SELECT MAX(f2.id) FROM FormularioModel f2 WHERE f2.empresa.id = :empresaId GROUP BY f2.empresa.id)")
    FormularioModel findLatestFormByEmpresaId(@Param("empresaId") Long empresaId);

    @Query("SELECT f FROM FormularioModel f WHERE f.empresa.id = :empresaId AND f.pontuacaoFinal IS NULL")
    Optional<FormularioModel> findIncompleteByEmpresaId(@Param("empresaId") Long empresaId);

    List<FormularioModel> findAllByOrderByPontuacaoFinalAsc();
    List<FormularioModel> findAllByOrderByPontuacaoSocialAsc();
    List<FormularioModel> findAllByOrderByPontuacaoAmbientalAsc();
    List<FormularioModel> findAllByOrderByPontuacaoGovernamentalAsc();

    List<FormularioModel> findByEmpresaId(Long empresaId);
}
