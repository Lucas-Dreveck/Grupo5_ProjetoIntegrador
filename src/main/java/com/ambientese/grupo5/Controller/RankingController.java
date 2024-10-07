package com.ambientese.grupo5.Controller;

import com.ambientese.grupo5.DTO.RankingEvaluation;
import com.ambientese.grupo5.Model.EvaluationModel;
import com.ambientese.grupo5.Model.Enums.SizeEnum;
import com.ambientese.grupo5.Services.RankingService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/ranking")
@Tag(name = "Ranking", description = "Endpoints para gerenciamento de rankings")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/score")
    public ResponseEntity<List<RankingEvaluation>> sortByScore(
            @RequestParam(required = false) String tradeName,
            @RequestParam(required = false) String segment,
            @RequestParam(required = false) SizeEnum companySize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        
        List<RankingEvaluation> result = rankingService.sortByScoreWithFilter(tradeName, segment, companySize, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/segments")
    public List<String> listSegments() {
        return rankingService.listSegments();
    }

    @GetMapping("/enviornmental")
    public List<EvaluationModel> sortByEnviornmentalPillar() {
        return rankingService.sortByEnviornmentalPillar();
    }

    @GetMapping("/social")
    public List<EvaluationModel> sortBySocialPillar() {
        return rankingService.sortBySocialPillar();
    }

    @GetMapping("/government")
    public List<EvaluationModel> sortByGovernmentPillar() {
        return rankingService.sortByGovernmentPillar();
    }
}
