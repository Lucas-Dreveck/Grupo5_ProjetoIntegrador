package com.ambientese.grupo5.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ambientese.grupo5.DTO.EmployeeRegistration;
import com.ambientese.grupo5.DTO.EmployeeRequest;
import com.ambientese.grupo5.Model.EmployeeModel;
import com.ambientese.grupo5.Services.EmployeeService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth/Employee")
@Tag(name = "Funcionario", description = "Endpoints para gerenciamento de funcionários")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable Long id) {
        EmployeeModel employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeRegistration>> findEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<EmployeeRegistration> result = employeeService.allPagedEmployeesWithFilter(name, page, size);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public EmployeeModel createEmployee (@RequestBody EmployeeRequest employeeRequest) {
        return employeeService.createEmployee(employeeRequest);
    }

    @PutMapping("/{id}")
    public EmployeeModel updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.updateEmployee(id, employeeRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }
}
