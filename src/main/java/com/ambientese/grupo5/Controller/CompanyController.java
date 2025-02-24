package com.ambientese.grupo5.controller;


import com.ambientese.grupo5.dto.CompanyRegistration;
import com.ambientese.grupo5.dto.CompanyRequest;
import com.ambientese.grupo5.model.CompanyModel;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.services.CompanyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/auth/Company")
@Tag(name = "Empresa", description = "Endpoints para gerenciamento de empresas")
public class CompanyController {
    
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public ResponseEntity<List<CompanyModel>> getAllCompanies() {
        List<CompanyModel> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCompanyById(@PathVariable Long id) {
        Optional<CompanyModel> company = companyRepository.findById(id);
        return company.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyRegistration>> findCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<CompanyRegistration> result = companyService.allPagedCompaniesWithFilter(name, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/evaluation/search")
    public ResponseEntity<List<CompanyRegistration>> companiesForEvaluation(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<CompanyRegistration> result = companyService.allPagedCompaniesWithFilter(name, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public CompanyModel createCompany (@RequestBody CompanyRequest CompanyModel) {
        return companyService.createCompany(CompanyModel);
    }

    @PutMapping("/{id}")
    public CompanyModel updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyRequest CompanyRequest) {
        return companyService.updateCompany(id, CompanyRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok().build();
    }
}
