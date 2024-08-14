package com.ambientese.grupo5.Repository;

import com.ambientese.grupo5.Model.Enums.EixoEnum;
import com.ambientese.grupo5.Model.PerguntasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PerguntasRepository extends JpaRepository<PerguntasModel, Long> {
    
    @Query("SELECT f FROM PerguntasModel f ORDER BY f.descricao ASC")
    List<PerguntasModel> findAllOrderByDescricaoAsc(); 
    
    List<PerguntasModel> findAllByDescricaoStartingWithIgnoreCaseOrderByDescricaoAsc(String descricao);
    
    List<PerguntasModel> findByEixo(EixoEnum eixo);
}
