package com.ambientese.grupo5.services.validations.company;

import org.springframework.stereotype.Service;

@Service
public class EmailValidation {
    public boolean isValidEmail(String email) {
        return email.contains("@") && email.indexOf("@") < email.lastIndexOf(".");
    }
}
