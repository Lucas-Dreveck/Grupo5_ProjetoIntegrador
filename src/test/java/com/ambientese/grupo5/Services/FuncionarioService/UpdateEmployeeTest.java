// package com.ambientese.grupo5.Services.FuncionarioService;

// import com.ambientese.grupo5.Controller.EmployeeController;
// import com.ambientese.grupo5.DTO.EmployeeRequest;
// import com.ambientese.grupo5.Model.RoleModel;
// import com.ambientese.grupo5.Services.EmployeeService;
// import com.ambientese.grupo5.Model.EmployeeModel;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class UpdateEmployeeTest {

//     private MockMvc mockMvc;

//     @Mock
//     private EmployeeService employeeService;

//     @InjectMocks
//     private EmployeeController atualizarFuncionarioController;

//     @SuppressWarnings("deprecation")
//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.initMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(atualizarFuncionarioController).build();
//     }

//     @Test
//     public void testUpdateEmployee() throws Exception {
//         EmployeeRequest employeeRequest = new EmployeeRequest();
//         employeeRequest.setName("Novo Nome");
//         employeeRequest.setRole("Analista de Dados");

//         EmployeeModel updatedEmployee = new EmployeeModel();
//         updatedEmployee.setId(1L);
//         updatedEmployee.setName("Novo Nome");
//         RoleModel roleModel = new RoleModel();
//         roleModel.setId(1L);
//         roleModel.setDescription("Analista de Dados");
//         updatedEmployee.setRole(roleModel);

//         when(employeeService.updateEmployee(any(Long.class), any(EmployeeRequest.class)))
//                 .thenReturn(updatedEmployee);

//         mockMvc.perform(MockMvcRequestBuilders.put("/Auth/Employee/{id}", 1)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{\"name\":\"Novo Nome\",\"role\":\"Analista de Dados\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Novo Nome"))
//                 .andExpect(jsonPath("$.role").value("Analista de Dados"))
//                 .andReturn();
//     }
// }