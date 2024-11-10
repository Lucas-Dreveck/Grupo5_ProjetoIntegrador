package com.ambientese.grupo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ambientese.grupo5.model.AnswerId;
import com.ambientese.grupo5.model.AnswerModel;

public interface AnswerRepository extends JpaRepository<AnswerModel, AnswerId> {

    boolean existsByQuestionId(long id);
    
}
