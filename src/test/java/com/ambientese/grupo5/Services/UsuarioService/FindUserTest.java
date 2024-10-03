// package com.ambientese.grupo5.Services.UsuarioService;

// import com.ambientese.grupo5.Controller.UserController;
// import com.ambientese.grupo5.Exception.ValidationException;
// import com.ambientese.grupo5.Model.UserModel;
// import com.ambientese.grupo5.Repository.UserRepository;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class FindUserTest {

//     private MockMvc mockMvc;

//     @Mock
//     private UserRepository userRepository;

//     @InjectMocks
//     private UserController userController;

//     @SuppressWarnings("deprecation")
//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.initMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//     }

//     @Test
//     public void testFindUserById() throws Exception {
//         UserModel userFound = new UserModel();
//         userFound.setId(1L);
//         userFound.setLogin("Usuário Encontrado");
//         userFound.setPassword("123");

//         when(userRepository.findById(1L).orElseThrow(() -> new ValidationException("Usuario não encontrado com o ID: " + 1L)))
//                 .thenReturn(userFound);

//         mockMvc.perform(MockMvcRequestBuilders.get("/Auth/User/{id}", 1)
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Usuário Encontrado"))
//                 .andExpect(jsonPath("$.email").value("usuario.encontrado@teste.com"))
//                 .andReturn();
//     }
// }
