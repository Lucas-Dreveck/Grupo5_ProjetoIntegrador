// package com.ambientese.grupo5.Services.UsuarioService;

// import com.ambientese.grupo5.Controller.UserController;
// import com.ambientese.grupo5.Model.UserModel;
// import com.ambientese.grupo5.Services.UserService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import java.util.Arrays;
// import java.util.List;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class ListUsersTest {

//     private MockMvc mockMvc;

//     @Mock
//     private UserService userService;

//     @InjectMocks
//     private UserController userController;

//     @SuppressWarnings("deprecation")
//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.initMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//     }

//     @Test
//     public void testListarUsuarios() throws Exception {
//         UserModel user1 = new UserModel();
//         user1.setId(1L);
//         user1.setLogin("Usuário 1");
//         user1.setPassword("123");

//         UserModel user2 = new UserModel();
//         user2.setId(2L);
//         user2.setLogin("Usuário 2");
//         user2.setPassword("123");

//         List<UserModel> users = Arrays.asList(user1, user2);

//         when(userService.getAllUsers())
//                 .thenReturn(users);

//         mockMvc.perform(MockMvcRequestBuilders.get("/Auth/User")
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$[0].id").value(1))
//                 .andExpect(jsonPath("$[0].name").value("Usuário 1"))
//                 .andExpect(jsonPath("$[0].email").value("user1@teste.com"))
//                 .andExpect(jsonPath("$[1].id").value(2))
//                 .andExpect(jsonPath("$[1].name").value("Usuário 2"))
//                 .andExpect(jsonPath("$[1].email").value("user2@teste.com"))
//                 .andReturn();
//     }
// }
