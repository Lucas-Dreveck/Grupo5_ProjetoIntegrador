package com.ambientese.grupo5.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.DTO.RankingForm;
import com.ambientese.grupo5.Model.FormModel;
import com.ambientese.grupo5.Model.Enums.SizeEnum;
import com.ambientese.grupo5.Repository.CompanyRepository;
import com.ambientese.grupo5.Repository.FormRepository;
import com.ambientese.grupo5.Specifications.FormSpecifications;

import jakarta.transaction.Transactional;

@Service
public class RankingService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public List<RankingForm> sortByScoreWithFilter(String tradeName, String segment, SizeEnum companySize, int page, int size) {
        Specification<FormModel> spec = Specification.where(null);

        if (tradeName != null && !tradeName.isEmpty()) {
            spec = spec.and(FormSpecifications.hasTradeName(tradeName));
        }
        if (segment != null && !segment.isEmpty()) {
            spec = spec.and(FormSpecifications.hasSegment(segment));
        }
        if (companySize != null) {
            spec = spec.and(FormSpecifications.hasSize(companySize));
        }

        List<FormModel> latestForms = formRepository.findLatestByCompanyOrderByFinalScoreDesc();

        final Specification<FormModel> finalSpec = spec;
        List<FormModel> filteredForms = latestForms.stream()
                .filter(form -> {
                    boolean matches = true;
                    if (finalSpec != null) {
                        if (tradeName != null && !tradeName.isEmpty()) {
                            matches = matches && form.getCompany().getTradeName().toLowerCase().startsWith(tradeName.toLowerCase());
                        }
                        if (segment != null && !segment.isEmpty()) {
                            matches = matches && form.getCompany().getSegment().equalsIgnoreCase(segment);
                        }
                        if (companySize != null) {
                            matches = matches && form.getCompany().getCompanySize() == companySize;
                        }
                    }
                    return matches;
                })
                .collect(Collectors.toList());

        int start = Math.min(page * size, filteredForms.size());
        int end = Math.min((page + 1) * size, filteredForms.size());
        

        List<RankingForm> result = filteredForms.subList(start, end).stream()
            .map(form -> new RankingForm(
                form.getCompany().getId(),
                form.getCompany().getRanking(),
                form.getCompany().getTradeName(),
                form.getCertificate(),
                form.getCompany().getSegment(),
                form.getCompany().getCompanySize(),
                form.getFinalScore(),
                form.getSocialScore(),
                form.getEnviornmentalScore(),
                form.getGovernmentScore(),
                end == filteredForms.size()
            )).collect(Collectors.toList());

        return result;
    }

    public List<String> listSegments() {
        return companyRepository.findDistinctSegment();
    }
    
    public List<FormModel>sortByEnviornmentalPillar(){
        return formRepository.findAllByOrderByEnviornmentalScoreAsc();
    }
    public List<FormModel>sortBySocialPillar (){
        return formRepository.findAllByOrderBySocialScoreAsc();
    }
    public List<FormModel>sortByGovernmentPillar (){
        return formRepository.findAllByOrderByGovernmentScoreAsc();
    }

    public List<FormModel> sortByScore() {
        return formRepository.findLatestByCompanyOrderByFinalScoreDesc();
    }
}
