package com.ambientese.grupo5.services;

import com.ambientese.grupo5.dto.CompanyRegistration;
import com.ambientese.grupo5.dto.CompanyRequest;
import com.ambientese.grupo5.exception.ValidationException;
import com.ambientese.grupo5.model.AddressModel;
import com.ambientese.grupo5.model.CompanyModel;
import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.repository.AddressRepository;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.repository.EvaluationRepository;
import com.ambientese.grupo5.services.validations.company.CNPJValidation;
import com.ambientese.grupo5.services.validations.company.MandatoryFieldsValidation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private MandatoryFieldsValidation mandatoryFieldsValidation;

    @Mock
    private CNPJValidation cnpjValidation;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCompanies() {
        List<CompanyModel> expectedCompanies = Arrays.asList(
                new CompanyModel(),
                new CompanyModel(),
                new CompanyModel()
        );
        when(companyRepository.findAll()).thenReturn(expectedCompanies);

        List<CompanyModel> actualCompanies = companyService.getAllCompanies();

        Assertions.assertEquals(expectedCompanies, actualCompanies);
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void testAllPagedCompaniesWithFilter() {
        List<CompanyModel> expectedCompanies = Arrays.asList(
                new CompanyModel(),
                new CompanyModel(),
                new CompanyModel()
        );
        when(companyRepository.findAllByTradeNameStartingWithIgnoreCaseOrderByTradeNameAsc(any())).thenReturn(expectedCompanies);

        List<CompanyRegistration> actualCompanies = companyService.allPagedCompaniesWithFilter("test", 0, 10);

        Assertions.assertEquals(expectedCompanies.size(), actualCompanies.size());
        verify(companyRepository, times(1)).findAllByTradeNameStartingWithIgnoreCaseOrderByTradeNameAsc(any());
    }

    @Test
    void testCreateCompany() {
        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setTradeName("Test Company");
        companyRequest.setCnpj("26260428000167");
        companyRequest.setAddress(new AddressModel());
        companyRequest.getAddress().setCep("12345678");
        companyRequest.getAddress().setNumber(123);
        companyRequest.getAddress().setPatio("Rua A");
        companyRequest.getAddress().setComplement("Apt 456");
        companyRequest.getAddress().setCity("Sao Paulo");
        companyRequest.getAddress().setNeighborhood("Centro");
        companyRequest.getAddress().setUF("SP");

        CompanyModel companyModel = new CompanyModel();
        companyModel.setId(1L);
        companyModel.setTradeName("Test Company");
        companyModel.setCnpj("26260428000167");
        companyModel.setAddres(companyRequest.getAddress());

        when(addressRepository.save(any(AddressModel.class))).thenReturn(companyRequest.getAddress());
        when(companyRepository.save(any(CompanyModel.class))).thenReturn(companyModel);

        CompanyModel createdCompany = companyService.createCompany(companyRequest);

        Assertions.assertEquals(companyModel.getId(), createdCompany.getId());
        Assertions.assertEquals(companyModel.getTradeName(), createdCompany.getTradeName());
        Assertions.assertEquals(companyModel.getCnpj(), createdCompany.getCnpj());
        Assertions.assertEquals(companyModel.getAddres(), createdCompany.getAddres());
        verify(mandatoryFieldsValidation, times(1)).validateMandatoryFields(any(CompanyRequest.class));
        verify(cnpjValidation, times(1)).validateSingleCNPJ(any(), any());
        verify(addressRepository, times(1)).save(any(AddressModel.class));
        verify(companyRepository, times(1)).save(any(CompanyModel.class));
    }

    @Test
    void testUpdateCompany() {
        Long companyId = 1L;
        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setTradeName("Updated Company");
        companyRequest.setCnpj("26260428000167");

        CompanyModel existingCompany = new CompanyModel();
        existingCompany.setId(companyId);
        existingCompany.setTradeName("Test Company");
        existingCompany.setCnpj("26260428000167");

        CompanyModel updatedCompany = new CompanyModel();
        updatedCompany.setId(companyId);
        updatedCompany.setTradeName("Updated Company");
        updatedCompany.setCnpj("26260428000167");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(CompanyModel.class))).thenReturn(updatedCompany);

        CompanyModel result = companyService.updateCompany(companyId, companyRequest);

        Assertions.assertEquals(updatedCompany.getId(), result.getId());
        Assertions.assertEquals(updatedCompany.getTradeName(), result.getTradeName());
        Assertions.assertEquals(updatedCompany.getCnpj(), result.getCnpj());
        verify(companyRepository, times(1)).findById(companyId);
        verify(mandatoryFieldsValidation, times(1)).validateMandatoryFields(any(CompanyRequest.class));
        verify(cnpjValidation, times(1)).validateSingleCNPJ(any(), any());
        verify(companyRepository, times(1)).save(any(CompanyModel.class));
    }

    @Test
    void testDeleteCompany() {
        Long companyId = 1L;
        CompanyModel company = new CompanyModel();
        company.setId(companyId);

        List<EvaluationModel> evaluations = new ArrayList<>();
        evaluations.add(new EvaluationModel());
        evaluations.add(new EvaluationModel());

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(evaluationRepository.findByCompanyId(companyId)).thenReturn(evaluations);

        companyService.deleteCompany(companyId);

        verify(companyRepository, times(1)).findById(companyId);
        verify(evaluationRepository, times(1)).findByCompanyId(companyId);
        verify(evaluationRepository, times(2)).delete(any(EvaluationModel.class));
        verify(companyRepository, times(1)).delete(any(CompanyModel.class));
    }

    @Test
    void testDeleteCompanyNotFound() {
        Long companyId = 1L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> companyService.deleteCompany(companyId));
        verify(companyRepository, times(1)).findById(companyId);
        verify(evaluationRepository, never()).findByCompanyId(anyLong());
        verify(evaluationRepository, never()).delete(any(EvaluationModel.class));
        verify(companyRepository, never()).delete(any(CompanyModel.class));
    }

    @Test
    void testMapCompany() {
        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setTradeName("Test Company");
        companyRequest.setCompanyName("ABC Corp");
        companyRequest.setCnpj("12345678901234");

        CompanyModel companyModel = new CompanyModel();
        companyService.mapCompany(companyModel, companyRequest);

        Assertions.assertEquals("Test Company", companyModel.getTradeName());
        Assertions.assertEquals("ABC Corp", companyModel.getCompanyName());
        Assertions.assertEquals("12345678901234", companyModel.getCnpj());
    }

    @Test
    void testMapAddress() {
        CompanyRequest companyRequest = new CompanyRequest();
        AddressModel addressModel = new AddressModel();
        companyRequest.setAddress(addressModel);
        companyRequest.getAddress().setCep("12345678");
        companyRequest.getAddress().setNumber(123);
        companyRequest.getAddress().setPatio("Rua A");
        companyRequest.getAddress().setComplement("Apt 456");
        companyRequest.getAddress().setCity("Sao Paulo");
        companyRequest.getAddress().setNeighborhood("Centro");
        companyRequest.getAddress().setUF("SP");

        companyService.mapAddress(addressModel, companyRequest);

        Assertions.assertEquals("12345678", addressModel.getCep());
        Assertions.assertEquals(123, addressModel.getNumber());
        Assertions.assertEquals("Rua A", addressModel.getPatio());
        Assertions.assertEquals("Apt 456", addressModel.getComplement());
        Assertions.assertEquals("Sao Paulo", addressModel.getCity());
        Assertions.assertEquals("Centro", addressModel.getNeighborhood());
        Assertions.assertEquals("SP", addressModel.getUF());
    }

    @Test
    void testUpdateCompanyNotFound() {
        Long companyId = 1L;
        CompanyRequest companyRequest = new CompanyRequest();

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ValidationException.class, () -> companyService.updateCompany(companyId, companyRequest));
        verify(companyRepository, times(1)).findById(companyId);
        verify(mandatoryFieldsValidation, never()).validateMandatoryFields(any(CompanyRequest.class));
        verify(cnpjValidation, never()).validateSingleCNPJ(any(), any());
        verify(companyRepository, never()).save(any(CompanyModel.class));
    }
}