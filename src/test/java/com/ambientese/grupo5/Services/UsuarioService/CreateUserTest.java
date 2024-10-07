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

// public class CreateUserTest {

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
//     public void testCreateUser() throws Exception {
//         UserModel user = new UserModel();
//         user.setLogin("Novo Usuário");
//         user.setPassword("123");

//         UserModel createdUser = new UserModel();
//         createdUser.setId(1L);
//         createdUser.setLogin("Novo Usuário");
//         createdUser.setPassword("123");

//         when(userRepository.findById(1L).orElseThrow(() -> new ValidationException("Usuario não encontrado com o ID: " + 1L)))
//                 .thenReturn(createdUser);

//         mockMvc.perform(MockMvcRequestBuilders.post("/Auth/User")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{\"name\":\"Novo Usuário\",\"email\":\"novo.user@teste.com\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Novo Usuário"))
//                 .andExpect(jsonPath("$.email").value("novo.user@teste.com"))
//                 .andReturn();
//     }
// }
