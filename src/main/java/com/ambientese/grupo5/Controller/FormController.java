package com.ambientese.grupo5.Controller;

import com.ambientese.grupo5.DTO.FormRequest;
import com.ambientese.grupo5.DTO.FormResponse;
import com.ambientese.grupo5.Model.FormModel;
import com.ambientese.grupo5.Services.FormService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Questionario", description = "Endpoints para gerenciamento de questionários")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping("/api/auth/haveActiveForm/{companyId}")
    public boolean havaActiveForm(@PathVariable() Long companyId) {
        return formService.haveActiveForm(companyId);
    }

    @GetMapping("/api/auth/form/{isNewForm}")
    public FormResponse showForm(@PathVariable() Boolean isNewForm, @RequestParam(required = false) Long companyId) {
        return formService.searchQuestionsInDb(isNewForm, companyId);
    }

    @PostMapping("/api/auth/processAnswers")
    public FormModel processAnswers(@RequestParam("companyId") Long companyId, @RequestParam("isComplete") Boolean isComplete, @RequestBody List<FormRequest> answers) {
        return formService.createForm(companyId, answers, isComplete);
    }
}
