package com.ambientese.grupo5.repository;

import com.ambientese.grupo5.model.RankingView;
import com.ambientese.grupo5.model.enums.SizeEnum;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<RankingView, Long> {
    List<RankingView> findBySegmentAndSizeAndCompanyNameStartingWithOrderByFinalScoreDesc(
        String segment, 
        SizeEnum size, 
        String companyName,
        Pageable pageable
    );
}

