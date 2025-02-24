package com.ambientese.grupo5.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ambientese.grupo5.dto.EmployeeRegistration;
import com.ambientese.grupo5.dto.EmployeeRequest;
import com.ambientese.grupo5.exception.ValidationException;
import com.ambientese.grupo5.model.EmployeeModel;
import com.ambientese.grupo5.model.UserModel;
import com.ambientese.grupo5.repository.EmployeeRepository;
import com.ambientese.grupo5.repository.RoleRepository;
import com.ambientese.grupo5.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           RoleRepository roleRepository,
                           UserService userService,
                           UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<EmployeeRegistration> allPagedEmployeesWithFilter(String name, int page, int size) {
        List<EmployeeModel> employees;
        if (name != null && !name.isEmpty()) {
            employees = employeeRepository.findAllByNameStartingWithIgnoreCaseOrderByNameAsc(name);

            if (employees.isEmpty()) {
                employees = employeeRepository.findAllOrderByNameAsc().stream()
                        .filter(employee -> employee.getRole().getDescription().toString().toLowerCase().startsWith(name.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else {
            employees = employeeRepository.findAll();
        }

        List<EmployeeRegistration> result = pageEmployees(employees, page, size);

        return result;
    }

    private List<EmployeeRegistration> pageEmployees(List<EmployeeModel> employees, int page, int size) {
        int total = employees.size();
        int start = Math.min(page * size, total);
        int end = Math.min((page + 1) * size, total);

        return employees.subList(start, end).stream()
                .map(employee -> new EmployeeRegistration(
                        employee.getId(),
                        employee.getName(),
                        employee.getCpf(),
                        employee.getEmail(),
                        employee.getBirthDate(),
                        employee.getRole(),
                        employee.getUser(),
                        end == total
                ))
                .collect(Collectors.toList());
    }

    public EmployeeModel createEmployee(EmployeeRequest employeeRequest) {
        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setName(employeeRequest.getName());
        employeeModel.setCpf(employeeRequest.getCpf());
        employeeModel.setEmail(employeeRequest.getEmail());
        employeeModel.setBirthDate(employeeRequest.getBirthDate());
        employeeModel.setRole(roleRepository.findByDescription(employeeRequest.getRole()));
        employeeModel.setUser(userService.createUser(employeeRequest.getUser()));
        return employeeRepository.save(employeeModel);
    }

    @Transactional
    public EmployeeModel updateEmployee(Long id, EmployeeRequest employeeRequest) {
        EmployeeModel employeeModel = employeeRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Funcionário não encontrado com o ID: " + id));
        employeeModel.setName(employeeRequest.getName());
        employeeModel.setCpf(employeeRequest.getCpf());
        employeeModel.setEmail(employeeRequest.getEmail());
        employeeModel.setBirthDate(employeeRequest.getBirthDate());
        employeeModel.setRole(roleRepository.findByDescription(employeeRequest.getRole()));
        
        UserModel userModel = userRepository.findById(employeeModel.getUser().getId())
                .orElseThrow(() -> new ValidationException("Funcionário não encontrado com o ID: " + id));;

        userModel.setLogin(employeeRequest.getLogin());
        boolean admin;
        // if ("Administrador".equals(employeeRequest.getRole())) {
        //     admin = true;
        // } else {
        //     admin = false;
        // }
        admin = false;
        userModel.setIsAdmin(admin);

        employeeModel.setUser(userModel);
        return employeeRepository.save(employeeModel);
    }

    public void deleteEmployee(Long id) {
        EmployeeModel employee = employeeRepository.findById(id).orElse(null);
        employeeRepository.delete(employee);
    }

    public EmployeeModel getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Funcionário não encontrado com o ID: " + id));
    }
}
