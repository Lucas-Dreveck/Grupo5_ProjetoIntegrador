package com.ambientese.grupo5.Services.UsuarioService;

import com.ambientese.grupo5.Controller.UsuarioController;
import com.ambientese.grupo5.Exception.ValidacaoException;
import com.ambientese.grupo5.Model.UsuarioModel;
import com.ambientese.grupo5.Repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BuscarUsuarioTeste {

    private MockMvc mockMvc;

    @Mock
    private UsuarioRepository buscarUsuarioService;

    @InjectMocks
    private UsuarioController buscarUsuarioController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(buscarUsuarioController).build();
    }

    @Test
    public void testBuscarUsuarioPorId() throws Exception {
        // Mock objeto de resposta
        UsuarioModel usuarioEncontrado = new UsuarioModel();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setLogin("Usuário Encontrado");
        usuarioEncontrado.setPassword("123");

        // Mock comportamento do serviço
        when(buscarUsuarioService.findById(1L).orElseThrow(() -> new ValidacaoException("Usuario não encontrado com o ID: " + 1L)))
                .thenReturn(usuarioEncontrado);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/Usuario/Get/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Usuário Encontrado"))
                .andExpect(jsonPath("$.email").value("usuario.encontrado@teste.com"))
                .andReturn();
    }
}
