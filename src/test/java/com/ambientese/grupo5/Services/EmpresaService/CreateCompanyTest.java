// package com.ambientese.grupo5.Services.EmpresaService;

// import com.ambientese.grupo5.Controller.CompanyController;
// import com.ambientese.grupo5.DTO.CompanyRequest;
// import com.ambientese.grupo5.Model.CompanyModel;
// import com.ambientese.grupo5.Services.CompanyService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class CreateCompanyTest {

//     private MockMvc mockMvc;

//     @Mock
//     private CompanyService companyService;

//     @InjectMocks
//     private CompanyController companyController;

//     @SuppressWarnings("deprecation")
//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.initMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
//     }

//     @Test
//     public void testCreateCompany() throws Exception {
//         CompanyRequest companyRequest = new CompanyRequest();
//         companyRequest.setTradeName("Minha Empresa");
//         companyRequest.setCnpj("12345678901234");

//         CompanyModel company = new CompanyModel();
//         company.setId(1L);
//         company.setTradeName("Minha Empresa");
//         company.setCnpj("12345678901234");

//         when(companyService.createCompany(any(CompanyRequest.class))).thenReturn(company);

//         @SuppressWarnings("unused")
//         MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/Auth/Company")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{\"tradeName\":\"Minha Empresa\",\"cnpj\":\"12345678901234\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.tradeName").value("Minha Empresa"))
//                 .andExpect(jsonPath("$.cnpj").value("12345678901234"))
//                 .andReturn();
//     }
// }