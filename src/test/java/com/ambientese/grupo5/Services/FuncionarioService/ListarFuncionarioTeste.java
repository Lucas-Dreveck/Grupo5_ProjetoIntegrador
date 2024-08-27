package com.ambientese.grupo5.Services.FuncionarioService;

import com.ambientese.grupo5.Controller.FuncionarioController;
import com.ambientese.grupo5.DTO.FuncionarioCadastro;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ListarFuncionarioTeste {

    private MockMvc mockMvc;

    @Mock
    private ListarFuncionarioService listarFuncionariosService;

    @InjectMocks
    private FuncionarioController listarFuncionariosController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listarFuncionariosController).build();
    }

    @Test
    public void testListarFuncionarios() throws Exception {
        // Mock lista de funcionários
        FuncionarioModel funcionario1 = new FuncionarioModel();
        funcionario1.setId(1L);
        funcionario1.setNome("Funcionário 1");

        CargoModel cargoModel = new CargoModel();
        cargoModel.setId(1L);
        cargoModel.setDescricao("Desenvolvedor");
        funcionario1.setCargo(cargoModel);

        FuncionarioModel funcionario2 = new FuncionarioModel();
        funcionario2.setId(2L);
        funcionario2.setNome("Funcionário 2");

        CargoModel cargoModel2 = new CargoModel();
        cargoModel.setId(1L);
        cargoModel.setDescricao("Analista");
        funcionario2.setCargo(cargoModel2);

        List<FuncionarioModel> funcionarios = Arrays.asList(funcionario1, funcionario2);

        List<FuncionarioCadastro> funcionarios2 = funcionarios.stream().map(funcionario ->
                new FuncionarioCadastro(
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getCpf(),
                        funcionario.getEmail(),
                        funcionario.getDataNascimento(),
                        funcionario.getCargo(),
                        funcionario.getUsuario(),
                        true
                ))
                .collect(Collectors.toList());

        // Mock comportamento do serviço
        when(listarFuncionariosService.allPagedFuncionariosWithFilter(null, 0, 10))
                .thenReturn(funcionarios2);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/Funcionario/List")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Funcionário 1"))
                .andExpect(jsonPath("$[0].cargo").value("Desenvolvedor"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Funcionário 2"))
                .andExpect(jsonPath("$[1].cargo").value("Analista"))
                .andReturn();
    }
}
