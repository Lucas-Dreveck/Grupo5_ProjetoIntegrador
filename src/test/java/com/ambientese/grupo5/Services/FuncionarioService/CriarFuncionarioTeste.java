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

public class CriarFuncionarioTeste {

    private MockMvc mockMvc;

    @Mock
    private CriarFuncionarioService criarFuncionarioService;

    @InjectMocks
    private FuncionarioController criarFuncionarioController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(criarFuncionarioController).build();
    }

    @Test
    public void testCriarFuncionario() throws Exception {
        // Mock objeto de request
        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        funcionarioRequest.setNome("Novo Funcionário");
        funcionarioRequest.setCargo("Desenvolvedor");

        // Mock objeto de resposta
        FuncionarioModel funcionarioCriado = new FuncionarioModel();
        funcionarioCriado.setId(1L);
        funcionarioCriado.setNome("Novo Funcionário");
        CargoModel cargoModel = new CargoModel();
        cargoModel.setId(1L);
        cargoModel.setDescricao("Desenvolvedor");
        funcionarioCriado.setCargo(cargoModel);

        // Mock comportamento do serviço
        when(criarFuncionarioService.criarFuncionario(any(FuncionarioRequest.class)))
                .thenReturn(funcionarioCriado);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/Funcionario/Add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Novo Funcionário\",\"cargo\":\"Desenvolvedor\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Funcionário"))
                .andExpect(jsonPath("$.cargo").value("Desenvolvedor"))
                .andReturn();
    }
}
