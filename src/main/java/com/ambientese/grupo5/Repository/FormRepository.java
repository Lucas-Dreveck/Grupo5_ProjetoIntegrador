package com.ambientese.grupo5.Repository;

import com.ambientese.grupo5.Model.FormModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FormRepository extends JpaRepository<FormModel, Long>, JpaSpecificationExecutor<FormModel> {
    
    @Query("""
        SELECT f 
        FROM FormModel f 
        INNER JOIN (
            SELECT f2.company.id AS companyId, MAX(f2.id) AS maxId 
            FROM FormModel f2 
            GROUP BY f2.company.id
        ) AS subquery 
        ON f.id = subquery.maxId
        ORDER BY f.finalScore DESC
        """)
    List<FormModel> findLatestByCompanyOrderByFinalScoreDesc();


    @Query("SELECT f FROM FormModel f WHERE f.id IN (SELECT MAX(f2.id) FROM FormModel f2 WHERE f2.company.id = :companyId GROUP BY f2.company.id)")
    FormModel findLatestFormByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT f FROM FormModel f WHERE f.company.id = :companyId AND f.finalScore IS NULL")
    Optional<FormModel> findIncompleteByCompanyId(@Param("companyId") Long companyId);

    List<FormModel> findAllByOrderByFinalScoreAsc();
    List<FormModel> findAllByOrderBySocialScoreAsc();
    List<FormModel> findAllByOrderByEnviornmentalScoreAsc();
    List<FormModel> findAllByOrderByGovernmentScoreAsc();

    List<FormModel> findByCompanyId(Long companyId);
}
