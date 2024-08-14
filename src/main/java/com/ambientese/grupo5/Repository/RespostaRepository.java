package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ambientese.grupo5.Model.RespostaId;
import com.ambientese.grupo5.Model.RespostaModel;

public interface RespostaRepository extends JpaRepository<RespostaModel, RespostaId> {

    boolean existsByPerguntaId(long id);
    
}
