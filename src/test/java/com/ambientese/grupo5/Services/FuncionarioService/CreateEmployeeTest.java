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

// public class CreateEmployeeTest {

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
//     public void testCreateEmployee() throws Exception {
//         EmployeeRequest employee = new EmployeeRequest();
//         employee.setName("Novo Funcionário");
//         employee.setRole("Desenvolvedor");

//         EmployeeModel createEmployee = new EmployeeModel();
//         createEmployee.setId(1L);
//         createEmployee.setName("Novo Funcionário");
//         RoleModel role = new RoleModel();
//         role.setId(1L);
//         role.setDescription("Desenvolvedor");
//         createEmployee.setRole(role);

//         when(employeeService.createEmployee(any(EmployeeRequest.class)))
//                 .thenReturn(createEmployee);

//         mockMvc.perform(MockMvcRequestBuilders.post("/Auth/Employee")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{\"name\":\"Novo Funcionário\",\"role\":\"Desenvolvedor\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Novo Funcionário"))
//                 .andExpect(jsonPath("$.role").value("Desenvolvedor"))
//                 .andReturn();
//     }
// }
