package com.ambientese.grupo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ambientese.grupo5.model.EvaluationModel;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<EvaluationModel, Long>, JpaSpecificationExecutor<EvaluationModel> {
    
    @Query("""
        SELECT e 
        FROM EvaluationModel e 
        WHERE e.id = (
            SELECT MAX(f2.id) 
            FROM EvaluationModel f2 
            WHERE f2.company.id = e.company.id
        )
        ORDER BY e.finalScore DESC
        """)
    List<EvaluationModel> findLatestByCompanyOrderByFinalScoreDesc();



    @Query("SELECT e FROM EvaluationModel e WHERE e.id IN (SELECT MAX(e2.id) FROM EvaluationModel e2 WHERE e2.company.id = :companyId GROUP BY e2.company.id)")
    EvaluationModel findLatestEvaluationByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT e FROM EvaluationModel e WHERE e.company.id = :companyId AND e.finalScore IS NULL")
    Optional<EvaluationModel> findIncompleteByCompanyId(@Param("companyId") Long companyId);

    List<EvaluationModel> findAllByOrderByFinalScoreAsc();
    List<EvaluationModel> findAllByOrderBySocialScoreAsc();
    List<EvaluationModel> findAllByOrderByEnviornmentalScoreAsc();
    List<EvaluationModel> findAllByOrderByGovernmentScoreAsc();

    List<EvaluationModel> findByCompanyId(Long companyId);
}
