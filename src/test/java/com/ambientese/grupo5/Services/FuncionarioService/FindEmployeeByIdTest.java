// package com.ambientese.grupo5.Services.FuncionarioService;

// import com.ambientese.grupo5.Controller.EmployeeController;
// import com.ambientese.grupo5.Exception.ValidationException;
// import com.ambientese.grupo5.Model.RoleModel;
// import com.ambientese.grupo5.Model.EmployeeModel;
// import com.ambientese.grupo5.Repository.EmployeeRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class FindEmployeeByIdTest {

//     private MockMvc mockMvc;

//     @Mock
//     private EmployeeRepository employeeRepository;

//     @InjectMocks
//     private EmployeeController employeeController;

//     @SuppressWarnings("deprecation")
//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.initMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
//     }

//     @Test
//     public void testFindEmployeeById() throws Exception {
//         EmployeeModel employeeFound = new EmployeeModel();
//         employeeFound.setId(1L);
//         employeeFound.setName("Funcionário Encontrado");
//         RoleModel role = new RoleModel();
//         role.setId(1L);
//         role.setDescription("Analista");
//         employeeFound.setRole(role);

//         assertEquals(1L, employeeRepository.findById(1L));
//         when(employeeRepository.findById(1L).orElseThrow(() -> new ValidationException("Funcionário não encontrado com o ID: " + 1L)))
//                 .thenReturn(employeeFound);

//         mockMvc.perform(MockMvcRequestBuilders.get("/Auth/Employee/{id}", 1)
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Funcionário Encontrado"))
//                 .andExpect(jsonPath("$.role").value("Analista"))
//                 .andReturn();
//     }
// }
