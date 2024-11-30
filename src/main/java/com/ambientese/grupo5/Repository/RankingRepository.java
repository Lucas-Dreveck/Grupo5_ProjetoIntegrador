package com.ambientese.grupo5.repository;

import com.ambientese.grupo5.model.RankingView;
import com.ambientese.grupo5.model.enums.SizeEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<RankingView, Long> {
    @Query("SELECT r FROM RankingView r " +
           "WHERE (:segment IS NULL OR r.segment = :segment) " +
           "AND (:companySize IS NULL OR r.size = :companySize) " +
           "AND (:tradeName IS NULL OR LOWER(r.companyName) LIKE LOWER(:tradeName%) ) " +
           "ORDER BY r.finalScore DESC")
    List<RankingView> findBySegmentAndSizeAndCompanyNameStartingWithOrderByFinalScoreDesc(
        @Param("segment") String segment,
        @Param("companySize") SizeEnum companySize,
        @Param("tradeName") String tradeName,
        Pageable pageable
    );
}