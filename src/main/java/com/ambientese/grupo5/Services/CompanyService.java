package com.ambientese.grupo5.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.DTO.CompanyRegistration;
import com.ambientese.grupo5.DTO.CompanyRequest;
import com.ambientese.grupo5.Exception.ValidationException;
import com.ambientese.grupo5.Model.AddressModel;
import com.ambientese.grupo5.Model.CompanyModel;
import com.ambientese.grupo5.Model.EvaluationModel;
import com.ambientese.grupo5.Repository.AddressRepository;
import com.ambientese.grupo5.Repository.CompanyRepository;
import com.ambientese.grupo5.Repository.EvaluationRepository;
import com.ambientese.grupo5.Services.Validations.Company.CNPJValidation;
import com.ambientese.grupo5.Services.Validations.Company.MandatoryFieldsValidation;

import jakarta.transaction.Transactional;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private MandatoryFieldsValidation mandatoryFieldsValidation;

    @Autowired
    private CNPJValidation cnpjValidation;

    public List<CompanyModel> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional
    public List<CompanyRegistration> allPagedCompaniesWithFilter(String name, int page, int size) {
        List<CompanyModel> companies;
        if (name != null && !name.isEmpty()) {
            companies = companyRepository.findAllByTradeNameStartingWithIgnoreCaseOrderByTradeNameAsc(name);

            if (companies.isEmpty()) {
                companies = companyRepository.findAllByCompanyNameStartingWithIgnoreCaseOrderByCompanyNameAsc(name);

                if (companies.isEmpty()) {
                    companies = companyRepository.findAllBySegmentStartingWithIgnoreCaseOrderByTradeNameAsc(name);

                    if (companies.isEmpty()) {
                        companies = companyRepository.findAllOrderByTradeNameAsc().stream()
                                .filter(company -> company.getCompanySize().toString().toLowerCase().startsWith(name.toLowerCase()))
                                .collect(Collectors.toList());
                    }
                }
            }
        } else {
            companies = companyRepository.findAll();
        }

        List<CompanyRegistration> result = pageCompanies(companies, page, size);

        return result;
    }

    public CompanyModel createCompany(CompanyRequest companyRequest) {
        mandatoryFieldsValidation.validateMandatoryFields(companyRequest);
        cnpjValidation.validateSingleCNPJ(companyRequest.getCnpj(), null);
        AddressModel addressModel = new AddressModel();
        mapAddress(addressModel, companyRequest);
        AddressModel enderecoSalvo = addressRepository.save(addressModel);
        CompanyModel companyModel = new CompanyModel();
        mapCompany(companyModel, companyRequest);
        companyModel.setAddres(enderecoSalvo);
        return companyRepository.save(companyModel);
    }

    public CompanyModel updateCompany(Long id, CompanyRequest companyRequest) {
        CompanyModel companyModel = companyRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Empresa não encontrada com o ID: " + id));
        mandatoryFieldsValidation.validateMandatoryFields(companyRequest);
        cnpjValidation.validateSingleCNPJ(companyRequest.getCnpj(), id);
        mapCompany(companyModel, companyRequest);
        return companyRepository.save(companyModel);
    }

    @Transactional
    public void deleteCompany(Long id) {
        CompanyModel company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            List<EvaluationModel> evaluations = evaluationRepository.findByCompanyId(company.getId());
            for (EvaluationModel evaluation : evaluations) {
                evaluationRepository.delete(evaluation);
            }
            companyRepository.delete(company);
        }
    }

    
    private List<CompanyRegistration> pageCompanies(List<CompanyModel> companies, int page, int size) {
        int total = companies.size();
        int start = Math.min(page * size, total);
        int end = Math.min((page + 1) * size, total);

        return companies.subList(start, end).stream()
                .map(company -> new CompanyRegistration(
                        company.getId(),
                        company.getImageUrl(),
                        company.getTradeName(),
                        company.getApplicantsName(),
                        company.getApplicantsPhone(),
                        company.getCompanyName(),
                        company.getCnpj(),
                        company.getSocialInscription(),
                        company.getAddres(),
                        company.getEmail(),
                        company.getCompanyPhone(),
                        company.getSegment(),
                        company.getCompanySize(),
                        company.getRanking(),
                        end == total
                ))
                .collect(Collectors.toList());
    }

    public void mapCompany(CompanyModel companyModel, CompanyRequest companyRequest) {
        companyModel.setTradeName(companyRequest.getTradeName());
        companyModel.setApplicantsName(companyRequest.getApplicantsName());
        companyModel.setApplicantsPhone(companyRequest.getApplicantsPhone());
        companyModel.setCompanyName(companyRequest.getCompanyName());
        companyModel.setCnpj(companyRequest.getCnpj());
        companyModel.setSocialInscription(companyRequest.getSocialInscription());
        companyModel.setEmail(companyRequest.getEmail());
        companyModel.setCompanyPhone(companyRequest.getCompanyPhone());
        companyModel.setSegment(companyRequest.getSegment());
        companyModel.setCompanySize(companyRequest.getCompanySize());
        companyModel.setAddres(companyRequest.getAddress());
    }

    public void mapAddress(AddressModel addressModel, CompanyRequest companyRequest) {
        addressModel.setCep(companyRequest.getAddress().getCep());
        addressModel.setNumber(companyRequest.getAddress().getNumber());
        addressModel.setPatio(companyRequest.getAddress().getPatio());
        addressModel.setComplement(companyRequest.getAddress().getComplement());
        addressModel.setCity(companyRequest.getAddress().getCity());
        addressModel.setNeighborhood(companyRequest.getAddress().getNeighborhood());
        addressModel.setUF(companyRequest.getAddress().getUF());
    }
}
