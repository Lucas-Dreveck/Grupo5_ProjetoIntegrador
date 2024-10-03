// package com.ambientese.grupo5.Services.FuncionarioService;

// import com.ambientese.grupo5.Controller.EmployeeController;
// import com.ambientese.grupo5.DTO.EmployeeRegistration;
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

// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// public class ListEmployeeTest {

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
//     public void testListEmployee() throws Exception {
//         EmployeeModel employee1 = new EmployeeModel();
//         employee1.setId(1L);
//         employee1.setName("Funcionário 1");

//         RoleModel role = new RoleModel();
//         role.setId(1L);
//         role.setDescription("Desenvolvedor");
//         employee1.setRole(role);

//         EmployeeModel employee2 = new EmployeeModel();
//         employee2.setId(2L);
//         employee2.setName("Funcionário 2");

//         RoleModel role2 = new RoleModel();
//         role.setId(1L);
//         role.setDescription("Analista");
//         employee2.setRole(role2);

//         List<EmployeeModel> employees = Arrays.asList(employee1, employee2);

//         List<EmployeeRegistration> employees2 = employees.stream().map(employee ->
//                 new EmployeeRegistration(
//                         employee.getId(),
//                         employee.getName(),
//                         employee.getCpf(),
//                         employee.getEmail(),
//                         employee.getBirthDate(),
//                         employee.getRole(),
//                         employee.getUser(),
//                         true
//                 ))
//                 .collect(Collectors.toList());

//         when(employeeService.allPagedEmployeesWithFilter(null, 0, 10))
//                 .thenReturn(employees2);

//         mockMvc.perform(MockMvcRequestBuilders.get("/Auth/Employee")
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$[0].id").value(1))
//                 .andExpect(jsonPath("$[0].name").value("Funcionário 1"))
//                 .andExpect(jsonPath("$[0].role").value("Desenvolvedor"))
//                 .andExpect(jsonPath("$[1].id").value(2))
//                 .andExpect(jsonPath("$[1].name").value("Funcionário 2"))
//                 .andExpect(jsonPath("$[1].role").value("Analista"))
//                 .andReturn();
//     }
// }
