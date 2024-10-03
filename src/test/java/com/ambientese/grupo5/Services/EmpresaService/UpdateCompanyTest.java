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

// public class UpdateCompanyTest {

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
//     public void testUpdateCompany() throws Exception {
//         CompanyRequest companyRequest = new CompanyRequest();
//         companyRequest.setTradeName("Nova Razão Social");
//         companyRequest.setCnpj("98765432109876");

//         CompanyModel updatedCompany = new CompanyModel();
//         updatedCompany.setId(1L);
//         updatedCompany.setTradeName("Nova Razão Social");
//         updatedCompany.setCnpj("98765432109876");

//         when(companyService.updateCompany(any(Long.class), any(CompanyRequest.class)))
//                 .thenReturn(updatedCompany);

//         // Perform PUT request
//         @SuppressWarnings("unused")
//         MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/Auth/Company/{id}", 1)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{\"tradeName\":\"Nova Razão Social\",\"cnpj\":\"98765432109876\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.tradeName").value("Nova Razão Social"))
//                 .andExpect(jsonPath("$.cnpj").value("98765432109876"))
//                 .andReturn();
//     }
// }