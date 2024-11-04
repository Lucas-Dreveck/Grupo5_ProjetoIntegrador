package com.ambientese.grupo5.Controller;

import com.ambientese.grupo5.DTO.EvaluationRequest;
import com.ambientese.grupo5.DTO.EvaluationResponse;
import com.ambientese.grupo5.Model.EvaluationModel;
import com.ambientese.grupo5.Services.EvaluationService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Questionario", description = "Endpoints para gerenciamento de questionários")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping("/api/auth/haveActiveEvaluation/{companyId}")
    public boolean haveActiveEvaluation(@PathVariable() Long companyId) {
        return evaluationService.haveActiveEvaluation(companyId);
    }

    @GetMapping("/api/auth/evaluation/{isNewEvaluation}")
    public EvaluationResponse showEvaluation(@PathVariable() Boolean isNewEvaluation, @RequestParam(required = false) Long companyId) {
        return evaluationService.searchQuestionsInDb(isNewEvaluation, companyId);
    }

    @PostMapping("/api/auth/processAnswers")
    public EvaluationModel processAnswers(@RequestParam("companyId") Long companyId, @RequestParam("isComplete") Boolean isComplete, @RequestBody List<EvaluationRequest> answers) {
        return evaluationService.createEvaluation(companyId, answers, isComplete);
    }
}
