package com.ambientese.grupo5.Services.Validations.Company;

import com.ambientese.grupo5.Exception.ValidationException;
import com.ambientese.grupo5.Model.CompanyModel;
import com.ambientese.grupo5.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CNPJValidation {

    private final CompanyRepository companyRepository;

    public CNPJValidation(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) {
            return false;
        }
        int[] digits = new int[cnpj.length()];
        for (int i = 0; i < cnpj.length(); i++) {
            digits[i] = Character.getNumericValue(cnpj.charAt(i));
        }
        int sum = 0;
        sum += digits[0] * 5;
        sum += digits[1] * 4;
        sum += digits[2] * 3;
        sum += digits[3] * 2;
        sum += digits[4] * 9;
        sum += digits[5] * 8;
        sum += digits[6] * 7;
        sum += digits[7] * 6;
        sum += digits[8] * 5;
        sum += digits[9] * 4;
        sum += digits[10] * 3;
        sum += digits[11] * 2;
        int rest = sum % 11;
        int dv1 = rest < 2 ? 0 : 11 - rest;
        if (digits[12] != dv1) {
            return false;
        }
        sum = 0;
        sum += digits[0] * 6;
        sum += digits[1] * 5;
        sum += digits[2] * 4;
        sum += digits[3] * 3;
        sum += digits[4] * 2;
        sum += digits[5] * 9;
        sum += digits[6] * 8;
        sum += digits[7] * 7;
        sum += digits[8] * 6;
        sum += digits[9] * 5;
        sum += digits[10] * 4;
        sum += digits[11] * 3;
        sum += digits[12] * 2;
        rest = sum % 11;
        int dv2 = rest < 2 ? 0 : 11 - rest;
        return digits[13] == dv2;
    }

    public void validateSingleCNPJ(String cnpj, Long companyId) {
        Optional<CompanyModel> empresaExistente = companyRepository.findByCnpj(cnpj);
        empresaExistente.ifPresent(company -> {
            if (!company.getId().equals(companyId)) {
                throw new ValidationException("Já existe uma empresa cadastrada com este CNPJ");
            }
        });
    }
}