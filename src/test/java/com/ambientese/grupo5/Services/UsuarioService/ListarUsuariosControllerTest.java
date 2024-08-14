package com.ambientese.grupo5.Services.UsuarioService;

import com.ambientese.grupo5.Controller.UsuarioController.BuscarUsuarioController;
import com.ambientese.grupo5.Model.UsuarioModel;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ListarUsuariosControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ListarUsuarioService listarUsuariosService;

    @InjectMocks
    private BuscarUsuarioController listarUsuariosController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listarUsuariosController).build();
    }

    @Test
    public void testListarUsuarios() throws Exception {
        // Mock lista de usuários
        UsuarioModel usuario1 = new UsuarioModel();
        usuario1.setId(1L);
        usuario1.setLogin("Usuário 1");
        usuario1.setPassword("123");

        UsuarioModel usuario2 = new UsuarioModel();
        usuario2.setId(2L);
        usuario2.setLogin("Usuário 2");
        usuario2.setPassword("123");

        List<UsuarioModel> usuarios = Arrays.asList(usuario1, usuario2);

        // Mock comportamento do serviço
        when(listarUsuariosService.getAllUsuarios())
                .thenReturn(usuarios);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/Usuario/List")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Usuário 1"))
                .andExpect(jsonPath("$[0].email").value("usuario1@teste.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Usuário 2"))
                .andExpect(jsonPath("$[1].email").value("usuario2@teste.com"))
                .andReturn();
    }
}
