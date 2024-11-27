package com.ambientese.grupo5.controller;

import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.model.enums.SizeEnum;
import com.ambientese.grupo5.services.RankingService;
import com.ambientese.grupo5.model.RankingView;

import io.swagger.v3.oas.annotations.tags.Tag;

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

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/score")
    public ResponseEntity<List<RankingView>> sortByScore(
            @RequestParam(required = false) String tradeName,
            @RequestParam(required = false) String segment,
            @RequestParam(required = false) SizeEnum companySize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        
        List<RankingView> result = rankingService.sortByScoreWithFilter(tradeName, segment, companySize, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/segments")
    public List<String> listSegments() {
        return rankingService.listSegments();
    }

    @GetMapping("/environmental")
    public List<EvaluationModel> sortByEnvironmentalPillar() {
        return rankingService.sortByEnvironmentalPillar();
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
