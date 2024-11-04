package com.ambientese.grupo5.services.validations.company;
import org.springframework.stereotype.Service;

@Service
public class PhoneValidation {

    public boolean isValidPhone (String phone) {
        return phone != null;
    }
}
