// package com.ambientese.grupo5.Services.FuncionarioService;

// import com.ambientese.grupo5.Controller.EmployeeController;
// import com.ambientese.grupo5.Services.EmployeeService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import static org.mockito.Mockito.doNothing;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class DeleteEmployeeTest {

//     private MockMvc mockMvc;

//     @Mock
//     private EmployeeService employeeService;

//     @InjectMocks
//     private EmployeeController employeeController;

//     @SuppressWarnings("deprecation")
//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.initMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
//     }

//     @Test
//     public void testDeleteEmployee() throws Exception {
//         // Mock comportamento do serviço
//         doNothing().when(employeeService).deleteEmployee(1L);

//         // Perform DELETE request
//         mockMvc.perform(MockMvcRequestBuilders.delete("/Auth/Employee/{id}", 1)
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andReturn();
//     }
// }
