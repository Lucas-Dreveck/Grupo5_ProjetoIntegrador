package com.ambientese.grupo5.Services.FuncionarioService;

import com.ambientese.grupo5.Controller.FuncionarioController.DeletarFuncionarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DeletarFuncionarioTeste {

    private MockMvc mockMvc;

    @Mock
    private DeletarFuncionarioService deletarFuncionarioService;

    @InjectMocks
    private DeletarFuncionarController deletarFuncionarioController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(deletarFuncionarioController).build();
    }

    @Test
    public void testDeletarFuncionario() throws Exception {
        // Mock comportamento do serviço
        doNothing().when(deletarFuncionarioService).deleteFuncionario(1L);

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/Funcionario/Delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
