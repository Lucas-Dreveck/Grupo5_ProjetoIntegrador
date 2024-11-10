package com.ambientese.grupo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ambientese.grupo5.model.QuestionModel;
import com.ambientese.grupo5.model.enums.PillarEnum;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionModel, Long> {
    
    @Query("SELECT q FROM QuestionModel q ORDER BY q.description ASC")
    List<QuestionModel> findAllOrderByDescriptionAsc(); 
    
    List<QuestionModel> findAllByDescriptionStartingWithIgnoreCaseOrderByDescriptionAsc(String description);
    
    List<QuestionModel> findByPillar(PillarEnum pillar);
}
