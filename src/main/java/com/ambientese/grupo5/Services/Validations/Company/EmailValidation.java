package com.ambientese.grupo5.Services.Validations.Company;

import org.springframework.stereotype.Service;

@Service
public class EmailValidation {
    public boolean isValidEmail(String email) {
        return email.contains("@") && email.indexOf("@") < email.lastIndexOf(".");
    }
}
