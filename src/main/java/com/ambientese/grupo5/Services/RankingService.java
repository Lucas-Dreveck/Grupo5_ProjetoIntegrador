package com.ambientese.grupo5.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.model.enums.SizeEnum;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.repository.EvaluationRepository;
import com.ambientese.grupo5.repository.RankingRepository;
import com.ambientese.grupo5.model.RankingView;

import jakarta.transaction.Transactional;

@Service
public class RankingService {

    private final EvaluationRepository evaluationRepository;
    private final RankingRepository rankingRepository;
    private final CompanyRepository companyRepository;

    public RankingService(EvaluationRepository evaluationRepository, 
                          RankingRepository rankingRepository, 
                          CompanyRepository companyRepository) {
        this.evaluationRepository = evaluationRepository;
        this.rankingRepository = rankingRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public List<RankingView> sortByScoreWithFilter(String tradeName, String segment, SizeEnum companySize, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        List<RankingView> results = rankingRepository.findBySegmentAndSizeAndCompanyNameStartingWithOrderByFinalScoreDesc(
            segment, 
            companySize, 
            tradeName,
            pageable
        );
    
        if (!results.isEmpty()) {
            results.get(results.size() - 1).setFinishList(true);
        }
    
        return results;
    }

    public List<String> listSegments() {
        return companyRepository.findDistinctSegment();
    }
    
    public List<EvaluationModel>sortByEnvironmentalPillar(){
        return evaluationRepository.findAllByOrderByEnvironmentalScoreAsc();
    }
    public List<EvaluationModel>sortBySocialPillar (){
        return evaluationRepository.findAllByOrderBySocialScoreAsc();
    }
    public List<EvaluationModel>sortByGovernmentPillar (){
        return evaluationRepository.findAllByOrderByGovernmentScoreAsc();
    }

    public List<EvaluationModel> sortByScore() {
        return evaluationRepository.findLatestByCompanyOrderByFinalScoreDesc();
    }
}
