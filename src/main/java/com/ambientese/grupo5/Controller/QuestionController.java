package com.ambientese.grupo5.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ambientese.grupo5.dto.QuestionRegistration;
import com.ambientese.grupo5.dto.QuestionRequest;
import com.ambientese.grupo5.model.QuestionModel;
import com.ambientese.grupo5.model.enums.PillarEnum;
import com.ambientese.grupo5.services.QuestionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth/Question")
@Tag(name = "Perguntas", description = "Endpoints para gerenciamento de perguntas")
public class QuestionController {
   
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{pillar}")
    public List<QuestionModel> listQuestionsPerPillar(@PathVariable PillarEnum pillar) {
        return questionService.listQuestionsPerPillar(pillar);
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuestionRegistration>> searchQuestions(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {


        List<QuestionRegistration> resultado = questionService.allPagedQuestionsWithFilter(name, page, size);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("search/id/{id}")
    public QuestionModel getQuestion(@PathVariable long id) {
        return questionService.getQuestion(id);
    }

    @PostMapping
    public QuestionModel createQuestion(@RequestBody QuestionRequest request) {
        return questionService.createQuestion(request.getDescription(), request.getPillar());
    }

    @PutMapping("/{id}")
    public QuestionModel updateQuestion(@PathVariable long id, @RequestBody QuestionRequest request) {
        return questionService.updateQuestion(id, request.getDescription(), request.getPillar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable long id) {
        return questionService.deleteQuestion(id);
    }
}
