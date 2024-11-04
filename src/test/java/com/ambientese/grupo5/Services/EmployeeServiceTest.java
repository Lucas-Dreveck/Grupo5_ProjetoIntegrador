package com.ambientese.grupo5.Services;

import com.ambientese.grupo5.DTO.EmployeeRegistration;
import com.ambientese.grupo5.DTO.EmployeeRequest;
import com.ambientese.grupo5.Exception.ValidationException;
import com.ambientese.grupo5.Model.EmployeeModel;
import com.ambientese.grupo5.Model.RoleModel;
import com.ambientese.grupo5.Model.UserModel;
import com.ambientese.grupo5.Repository.EmployeeRepository;
import com.ambientese.grupo5.Repository.RoleRepository;
import com.ambientese.grupo5.Repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllPagedEmployeesWithFilterByName() {
        // Setup
        String name = "John";
        EmployeeModel employee = createSampleEmployee();
        List<EmployeeModel> employees = Arrays.asList(employee);
        
        when(employeeRepository.findAllByNameStartingWithIgnoreCaseOrderByNameAsc(name))
            .thenReturn(employees);

        // Execute
        List<EmployeeRegistration> result = employeeService.allPagedEmployeesWithFilter(name, 0, 20);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employee.getName(), result.get(0).getName());
        verify(employeeRepository).findAllByNameStartingWithIgnoreCaseOrderByNameAsc(name);
    }

    @Test
    void testAllPagedEmployeesWithFilterByRole() {
        // Setup
        String roleName = "dev";
        EmployeeModel employee = createSampleEmployee();
        List<EmployeeModel> employees = Collections.emptyList();
        List<EmployeeModel> allEmployees = Arrays.asList(employee);
        
        when(employeeRepository.findAllByNameStartingWithIgnoreCaseOrderByNameAsc(roleName))
            .thenReturn(employees);
        when(employeeRepository.findAllOrderByNameAsc())
            .thenReturn(allEmployees);

        // Execute
        List<EmployeeRegistration> result = employeeService.allPagedEmployeesWithFilter(roleName, 0, 20);

        // Verify
        assertNotNull(result);
        verify(employeeRepository).findAllByNameStartingWithIgnoreCaseOrderByNameAsc(roleName);
        verify(employeeRepository).findAllOrderByNameAsc();
    }

    @Test
    void testAllPagedEmployeesWithoutFilter() {
        // Setup
        EmployeeModel employee = createSampleEmployee();
        List<EmployeeModel> employees = Arrays.asList(employee);
        
        when(employeeRepository.findAll()).thenReturn(employees);

        // Execute
        List<EmployeeRegistration> result = employeeService.allPagedEmployeesWithFilter(null, 0, 20);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void testCreateEmployee() {
        // Setup
        EmployeeRequest request = createSampleEmployeeRequest();
        EmployeeModel employee = createSampleEmployee();
        RoleModel role = new RoleModel();
        UserModel user = new UserModel();
        
        when(roleRepository.findByDescription(any())).thenReturn(role);
        when(userService.createUser(any())).thenReturn(user);
        when(employeeRepository.save(any())).thenReturn(employee);

        // Execute
        EmployeeModel result = employeeService.createEmployee(request);

        // Verify
        assertNotNull(result);
        verify(employeeRepository).save(any());
        verify(roleRepository).findByDescription(any());
        verify(userService).createUser(any());
    }

    @Test
    void testUpdateEmployee() {
        // Setup
        Long id = 1L;
        EmployeeRequest request = createSampleEmployeeRequest();
        EmployeeModel existingEmployee = createSampleEmployee();
        RoleModel role = new RoleModel();
        UserModel user = new UserModel();
        
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(roleRepository.findByDescription(any())).thenReturn(role);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(employeeRepository.save(any())).thenReturn(existingEmployee);

        // Execute
        EmployeeModel result = employeeService.updateEmployee(id, request);

        // Verify
        assertNotNull(result);
        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(any());
        verify(roleRepository).findByDescription(any());
        verify(userRepository).findById(any());
    }

    @Test
    void testUpdateEmployeeNotFound() {
        // Setup
        Long id = 1L;
        EmployeeRequest request = createSampleEmployeeRequest();
        
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ValidationException.class, () -> employeeService.updateEmployee(id, request));
        verify(employeeRepository).findById(id);
    }

    @Test
    void testDeleteEmployee() {
        // Setup
        Long id = 1L;
        EmployeeModel employee = createSampleEmployee();
        
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any());

        // Execute
        employeeService.deleteEmployee(id);

        // Verify
        verify(employeeRepository).findById(id);
        verify(employeeRepository).delete(any());
    }

    @Test
    void testGetEmployeeById() {
        // Setup
        Long id = 1L;
        EmployeeModel employee = createSampleEmployee();
        
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        // Execute
        EmployeeModel result = employeeService.getEmployeeById(id);

        // Verify
        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        verify(employeeRepository).findById(id);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        // Setup
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ValidationException.class, () -> employeeService.getEmployeeById(id));
        verify(employeeRepository).findById(id);
    }

    @Test
    void testPageEmployeesWithMultiplePages() {
        // Setup
        List<EmployeeModel> employees = Arrays.asList(
            createSampleEmployee(),
            createSampleEmployee(),
            createSampleEmployee()
        );
        
        when(employeeRepository.findAll()).thenReturn(employees);

        // Execute
        List<EmployeeRegistration> result = employeeService.allPagedEmployeesWithFilter(null, 0, 2);

        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    private EmployeeModel createSampleEmployee() {
        EmployeeModel employee = new EmployeeModel();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setCpf("12345678901");
        employee.setEmail("john@example.com");
        employee.setBirthDate(LocalDate.now());
        
        RoleModel role = new RoleModel();
        role.setDescription("Developer");
        employee.setRole(role);
        
        UserModel user = new UserModel();
        user.setId(1L);
        employee.setUser(user);
        
        return employee;
    }

    private EmployeeRequest createSampleEmployeeRequest() {
        EmployeeRequest request = new EmployeeRequest();
        request.setName("John Doe");
        request.setCpf("12345678901");
        request.setEmail("john@example.com");
        request.setBirthDate(LocalDate.now());
        request.setRole("Developer");
        request.setLogin("johndoe");
        request.setUser(new UserModel());
        return request;
    }
}