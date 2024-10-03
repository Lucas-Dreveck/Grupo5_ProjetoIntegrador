package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ambientese.grupo5.Model.AnswerId;
import com.ambientese.grupo5.Model.AnswerModel;

public interface AnswerRepository extends JpaRepository<AnswerModel, AnswerId> {

    boolean existsByQuestionId(long id);
    
}
