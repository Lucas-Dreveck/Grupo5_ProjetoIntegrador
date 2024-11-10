package com.ambientese.grupo5.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.dto.RankingEvaluation;
import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.model.enums.SizeEnum;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.repository.EvaluationRepository;
import com.ambientese.grupo5.specifications.EvaluationSpecifications;

import jakarta.transaction.Transactional;

@Service
public class RankingService {

    private final EvaluationRepository evaluationRepository;
    private final CompanyRepository companyRepository;

    public RankingService(EvaluationRepository evaluationRepository, 
                          CompanyRepository companyRepository) {
        this.evaluationRepository = evaluationRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public List<RankingEvaluation> sortByScoreWithFilter(String tradeName, String segment, SizeEnum companySize, int page, int size) {
        Specification<EvaluationModel> spec = Specification.where(null);

        if (tradeName != null && !tradeName.isEmpty()) {
            spec = spec.and(EvaluationSpecifications.hasTradeName(tradeName));
        }
        if (segment != null && !segment.isEmpty()) {
            spec = spec.and(EvaluationSpecifications.hasSegment(segment));
        }
        if (companySize != null) {
            spec = spec.and(EvaluationSpecifications.hasSize(companySize));
        }

        List<EvaluationModel> latestEvaluations = evaluationRepository.findLatestByCompanyOrderByFinalScoreDesc();

        final Specification<EvaluationModel> finalSpec = spec;
        List<EvaluationModel> filteredEvaluations = latestEvaluations.stream()
                .filter(evaluation -> {
                    boolean matches = true;
                    if (finalSpec != null) {
                        if (tradeName != null && !tradeName.isEmpty()) {
                            matches = matches && evaluation.getCompany().getTradeName().toLowerCase().startsWith(tradeName.toLowerCase());
                        }
                        if (segment != null && !segment.isEmpty()) {
                            matches = matches && evaluation.getCompany().getSegment().equalsIgnoreCase(segment);
                        }
                        if (companySize != null) {
                            matches = matches && evaluation.getCompany().getCompanySize() == companySize;
                        }
                    }
                    return matches;
                })
                .collect(Collectors.toList());

        int start = Math.min(page * size, filteredEvaluations.size());
        int end = Math.min((page + 1) * size, filteredEvaluations.size());
        

        List<RankingEvaluation> result = filteredEvaluations.subList(start, end).stream()
            .map(evaluation -> new RankingEvaluation(
                evaluation.getCompany().getId(),
                evaluation.getCompany().getRanking(),
                evaluation.getCompany().getTradeName(),
                evaluation.getCertificate(),
                evaluation.getCompany().getSegment(),
                evaluation.getCompany().getCompanySize(),
                evaluation.getCompany().getAddres().getCity(),
                evaluation.getFinalScore(),
                evaluation.getSocialScore(),
                evaluation.getEnviornmentalScore(),
                evaluation.getGovernmentScore(),
                end == filteredEvaluations.size()
            )).collect(Collectors.toList());

        return result;
    }

    public List<String> listSegments() {
        return companyRepository.findDistinctSegment();
    }
    
    public List<EvaluationModel>sortByEnviornmentalPillar(){
        return evaluationRepository.findAllByOrderByEnviornmentalScoreAsc();
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
