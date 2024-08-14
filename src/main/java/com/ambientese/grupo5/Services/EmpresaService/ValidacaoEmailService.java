package com.ambientese.grupo5.Services.EmpresaService;

import org.springframework.stereotype.Service;

@Service
public class ValidacaoEmailService {
    public boolean isValidEmail(String email) {
        return email.contains("@") && email.indexOf("@") < email.lastIndexOf(".");
    }
}
