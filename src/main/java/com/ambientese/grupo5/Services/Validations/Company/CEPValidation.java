package com.ambientese.grupo5.Services.Validations.Company;

import org.springframework.stereotype.Service;

@Service
public class CEPValidation {
    public boolean isValidCep(String cep) {
        String cepRegex = "\\d{5}-\\d{3}|\\d{8}";
        return cep != null && cep.matches(cepRegex);
    }
}
