package com.ambientese.grupo5.Services.FuncionarioService;

import com.ambientese.grupo5.Controller.FuncionarioController.BuscarFuncionarioController;
import com.ambientese.grupo5.Exception.ValidacaoException;
import com.ambientese.grupo5.Model.CargoModel;
import com.ambientese.grupo5.Model.FuncionarioModel;
import com.ambientese.grupo5.Repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BuscarFuncionarioPorIdTeste {

    private MockMvc mockMvc;

    @Mock
    private FuncionarioRepository buscarFuncionarioService;

    @InjectMocks
    private BuscarFuncionarioController buscarFuncionarioController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(buscarFuncionarioController).build();
    }

    @Test
    public void testBuscarFuncionarioPorId() throws Exception {
        // Mock objeto de resposta
        FuncionarioModel funcionarioEncontrado = new FuncionarioModel();
        funcionarioEncontrado.setId(1L);
        funcionarioEncontrado.setNome("Funcionário Encontrado");
        CargoModel cargoModel = new CargoModel();
        cargoModel.setId(1L);
        cargoModel.setDescricao("Analista");
        funcionarioEncontrado.setCargo(cargoModel);

        // Mock comportamento do serviço
        assertEquals(1L, buscarFuncionarioService.findById(1L));
        when(buscarFuncionarioService.findById(1L).orElseThrow(() -> new ValidacaoException("Funcionário não encontrado com o ID: " + 1L)))
                .thenReturn(funcionarioEncontrado);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/Funcionario/Get/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Funcionário Encontrado"))
                .andExpect(jsonPath("$.cargo").value("Analista"))
                .andReturn();
    }
}
