package com.ambientese.grupo5.Services.Validations.Company;

import com.ambientese.grupo5.DTO.CompanyRequest;
import com.ambientese.grupo5.Exception.ValidationException;
import com.ambientese.grupo5.Model.AddressModel;
import org.springframework.stereotype.Service;

@Service
public class MandatoryFieldsValidation {

    private final CNPJValidation cnpjValidation;
    private final PhoneValidation phoneValidation;
    private final CEPValidation cepValidation;
    private final EmailValidation emailValidation;

    public MandatoryFieldsValidation(CNPJValidation cnpjValidation,
                                      PhoneValidation phoneValidation,
                                      CEPValidation cepValidation,
                                      EmailValidation emailValidation) {
        this.cnpjValidation = cnpjValidation;
        this.phoneValidation = phoneValidation;
        this.cepValidation = cepValidation;
        this.emailValidation = emailValidation;
    }

    public void validateMandatoryFields(CompanyRequest companyRequest) {
        validateCompanyRequest(companyRequest);
        validateFormats(companyRequest);
    }

    private void validateCompanyRequest(CompanyRequest companyRequest) {
        if (companyRequest == null || companyRequestBlankFields(companyRequest)) {
            throw new ValidationException("Os dados da empresa não podem estar em branco");
        }
        validateAddress(companyRequest.getAddress());
    }

    private boolean companyRequestBlankFields(CompanyRequest companyRequest) {
        return companyRequest.getTradeName() == null || companyRequest.getTradeName().trim().isEmpty() ||
                companyRequest.getApplicantsName() == null || companyRequest.getApplicantsName().trim().isEmpty() ||
                companyRequest.getApplicantsPhone() == null || companyRequest.getApplicantsPhone().trim().isEmpty() ||
                companyRequest.getCompanyName() == null || companyRequest.getCompanyName().trim().isEmpty() ||
                companyRequest.getCnpj() == null || companyRequest.getCnpj().trim().isEmpty() ||
                companyRequest.getCompanyPhone() == null || companyRequest.getCompanyPhone().trim().isEmpty() ||
                companyRequest.getSegment() == null || companyRequest.getSegment().trim().isEmpty() ||
                companyRequest.getCompanySize() == null || companyRequest.getCompanySize().toString().trim().isEmpty() ||
                companyRequest.getEmail() == null || companyRequest.getEmail().trim().isEmpty();
    }

    private void validateAddress(AddressModel address) {
        if (address == null || addressBlankFields(address)) {
            throw new ValidationException("O endereço não pode estar em branco");
        }
    }

    private boolean addressBlankFields(AddressModel address) {
        return address.getCep() == null || address.getCep().trim().isEmpty() ||
                address.getNumber() == null ||
                address.getPatio() == null || address.getPatio().trim().isEmpty() ||
                address.getCity() == null || address.getCity().trim().isEmpty() ||
                address.getNeighborhood() == null || address.getNeighborhood().trim().isEmpty() ||
                address.getUF() == null || address.getUF().trim().isEmpty();
    }

    private void validateFormats(CompanyRequest companyRequest) {
        validateEmail(companyRequest.getEmail());
        validateCEP(companyRequest.getAddress().getCep());
        validatePhone(companyRequest.getCompanyPhone());
        validatePhone(companyRequest.getApplicantsPhone());
        validateCNPJ(companyRequest.getCnpj());
    }

    private void validateEmail(String email) {
        if (!emailValidation.isValidEmail(email)) {
            throw new ValidationException("O e-mail inserido não é válido");
        }
    }

    private void validateCEP(String cep) {
        if (!cepValidation.isValidCep(cep)) {
            throw new ValidationException("O CEP inserido não é válido");
        }
    }

    private void validatePhone(String phone) {
        if (!phoneValidation.isValidPhone(phone)) {
            throw new ValidationException("O telefone inserido não é válido");
        }
    }

    private void validateCNPJ(String cnpj) {
        if (!cnpjValidation.isValidCnpj(cnpj)) {
            throw new ValidationException("O CNPJ inserido não é válido");
        }
    }
}
