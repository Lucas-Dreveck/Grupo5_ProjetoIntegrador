package com.ambientese.grupo5.Services.EmpresaService;

import com.ambientese.grupo5.Controller.EmpresaController;
import com.ambientese.grupo5.DTO.EmpresaRequest;
import com.ambientese.grupo5.Model.EmpresaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CriarEmpresasTeste {

    private MockMvc mockMvc;

    @Mock
    private CriarEmpresaService criarEmpresaService;

    @InjectMocks
    private EmpresaController criarEmpresaController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(criarEmpresaController).build();
    }

    @Test
    public void testCriarEmpresa() throws Exception {
        EmpresaRequest empresaRequest = new EmpresaRequest();
        empresaRequest.setNomeFantasia("Minha Empresa");
        empresaRequest.setCnpj("12345678901234");

        EmpresaModel empresaModel = new EmpresaModel();
        empresaModel.setId(1L);
        empresaModel.setNomeFantasia("Minha Empresa");
        empresaModel.setCnpj("12345678901234");

        when(criarEmpresaService.criarEmpresa(any(EmpresaRequest.class))).thenReturn(empresaModel);

        @SuppressWarnings("unused")
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/Empresa/Add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeFantasia\":\"Minha Empresa\",\"cnpj\":\"12345678901234\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeFantasia").value("Minha Empresa"))
                .andExpect(jsonPath("$.cnpj").value("12345678901234"))
                .andReturn();
    }
}