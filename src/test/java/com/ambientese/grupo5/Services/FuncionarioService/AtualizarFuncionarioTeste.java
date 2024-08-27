package com.ambientese.grupo5.Services.FuncionarioService;

import com.ambientese.grupo5.Controller.FuncionarioController;
import com.ambientese.grupo5.DTO.FuncionarioRequest;
import com.ambientese.grupo5.Model.CargoModel;
import com.ambientese.grupo5.Model.FuncionarioModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AtualizarFuncionarioTeste {

    private MockMvc mockMvc;

    @Mock
    private AtualizarFuncionarioService atualizarFuncionarioService;

    @InjectMocks
    private FuncionarioController atualizarFuncionarioController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(atualizarFuncionarioController).build();
    }

    @Test
    public void testAtualizarFuncionario() throws Exception {
        // Mock objeto de request
        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        funcionarioRequest.setNome("Novo Nome");
        funcionarioRequest.setCargo("Analista de Dados");

        // Mock objeto de resposta
        FuncionarioModel funcionarioAtualizado = new FuncionarioModel();
        funcionarioAtualizado.setId(1L);
        funcionarioAtualizado.setNome("Novo Nome");
        CargoModel cargoModel = new CargoModel();
        cargoModel.setId(1L);
        cargoModel.setDescricao("Analista de Dados");
        funcionarioAtualizado.setCargo(cargoModel);

        // Mock comportamento do serviço
        when(atualizarFuncionarioService.atualizarFuncionario(any(Long.class), any(FuncionarioRequest.class)))
                .thenReturn(funcionarioAtualizado);

        // Perform PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/Funcionario/Edit/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Novo Nome\",\"cargo\":\"Analista de Dados\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Nome"))
                .andExpect(jsonPath("$.cargo").value("Analista de Dados"))
                .andReturn();
    }
}