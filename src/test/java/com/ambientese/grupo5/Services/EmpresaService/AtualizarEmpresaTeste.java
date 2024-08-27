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

public class AtualizarEmpresaTeste {

    private MockMvc mockMvc;

    @Mock
    private AtualizarEmpresaService atualizarEmpresaService;

    @InjectMocks
    private EmpresaController atualizarEmpresaController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(atualizarEmpresaController).build();
    }

    @Test
    public void testAtualizarEmpresa() throws Exception {
        // Mock objeto de request
        EmpresaRequest empresaRequest = new EmpresaRequest();
        empresaRequest.setNomeFantasia("Nova Razão Social");
        empresaRequest.setCnpj("98765432109876");

        // Mock objeto de resposta
        EmpresaModel empresaAtualizada = new EmpresaModel();
        empresaAtualizada.setId(1L);
        empresaAtualizada.setNomeFantasia("Nova Razão Social");
        empresaAtualizada.setCnpj("98765432109876");

        // Mock comportamento do serviço
        when(atualizarEmpresaService.atualizarEmpresa(any(Long.class), any(EmpresaRequest.class)))
                .thenReturn(empresaAtualizada);

        // Perform PUT request
        @SuppressWarnings("unused")
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/Empresa/Edit/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeFantasia\":\"Nova Razão Social\",\"cnpj\":\"98765432109876\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeFantasia").value("Nova Razão Social"))
                .andExpect(jsonPath("$.cnpj").value("98765432109876"))
                .andReturn();
    }
}