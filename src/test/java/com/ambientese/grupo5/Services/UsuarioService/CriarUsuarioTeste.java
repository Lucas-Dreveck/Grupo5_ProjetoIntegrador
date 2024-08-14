package com.ambientese.grupo5.Services.UsuarioService;

import com.ambientese.grupo5.Controller.UsuarioController.CriarUsuarioController;
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

public class CriarUsuarioTeste {

    private MockMvc mockMvc;

    @Mock
    private UsuarioRepository criarUsuarioService;

    @InjectMocks
    private CriarUsuarioController criarUsuarioController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(criarUsuarioController).build();
    }

    @Test
    public void testCriarUsuario() throws Exception {
        // Mock objeto de request
        UsuarioModel usuarioRequest = new UsuarioModel();
        usuarioRequest.setLogin("Novo Usuário");
        usuarioRequest.setPassword("123");

        // Mock objeto de resposta
        UsuarioModel usuarioCriado = new UsuarioModel();
        usuarioCriado.setId(1L);
        usuarioCriado.setLogin("Novo Usuário");
        usuarioCriado.setPassword("123");

        // Mock comportamento do serviço
        when(criarUsuarioService.findById(1L).orElseThrow(() -> new ValidacaoException("Usuario não encontrado com o ID: " + 1L)))
                .thenReturn(usuarioCriado);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/Usuario/Add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Novo Usuário\",\"email\":\"novo.usuario@teste.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Usuário"))
                .andExpect(jsonPath("$.email").value("novo.usuario@teste.com"))
                .andReturn();
    }
}
