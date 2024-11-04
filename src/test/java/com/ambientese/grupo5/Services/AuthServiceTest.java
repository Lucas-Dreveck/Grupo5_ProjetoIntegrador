package com.ambientese.grupo5.Services;

import com.ambientese.grupo5.Model.EmployeeModel;
import com.ambientese.grupo5.Model.RoleModel;
import com.ambientese.grupo5.Model.UserModel;
import com.ambientese.grupo5.Repository.EmployeeRepository;
import com.ambientese.grupo5.Repository.UserRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_WithValidUserCredentials_ReturnsTrue() {
        // Arrange
        String login = "test@test.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        UserModel user = new UserModel();
        user.setLogin(login);
        user.setPassword(hashedPassword);
        
        when(userRepository.findByLogin(login)).thenReturn(user);

        // Act
        boolean result = authService.authenticate(login, password);

        // Assert
        assertTrue(result);
        verify(userRepository).findByLogin(login);
    }

    @Test
    void authenticate_WithValidEmployeeCredentials_ReturnsTrue() {
        // Arrange
        String email = "employee@test.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        UserModel user = new UserModel();
        user.setPassword(hashedPassword);
        
        EmployeeModel employee = new EmployeeModel();
        employee.setEmail(email);
        employee.setUser(user);
        
        when(userRepository.findByLogin(email)).thenReturn(null);
        when(employeeRepository.findByEmail(email)).thenReturn(employee);

        // Act
        boolean result = authService.authenticate(email, password);

        // Assert
        assertTrue(result);
        verify(employeeRepository).findByEmail(email);
    }

    @Test
    void authenticate_WithInvalidCredentials_ReturnsFalse() {
        // Arrange
        String login = "test@test.com";
        String password = "wrongpassword";
        
        when(userRepository.findByLogin(login)).thenReturn(null);
        when(employeeRepository.findByEmail(login)).thenReturn(null);

        // Act
        boolean result = authService.authenticate(login, password);

        // Assert
        assertFalse(result);
    }

    @Test
    void login_WithValidUserCredentials_ReturnsToken() {
        // Arrange
        String login = "test@test.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String expectedToken = "valid.jwt.token";
        
        UserModel user = new UserModel();
        user.setId(1L);
        user.setLogin(login);
        user.setPassword(hashedPassword);
        user.setIsAdmin(true);
        
        when(userRepository.findByLogin(login)).thenReturn(user);
        when(jwtUtil.generateToken(login, true, "Admin")).thenReturn(expectedToken);

        // Act
        String result = authService.login(login, password);

        // Assert
        assertEquals(expectedToken, result);
    }

    @Test
    void login_WithValidEmployeeCredentials_ReturnsToken() {
        // Arrange
        String email = "employee@test.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String expectedToken = "valid.jwt.token";
        
        UserModel user = new UserModel();
        user.setId(1L);
        user.setPassword(hashedPassword);
        user.setIsAdmin(false);
        
        RoleModel role = new RoleModel();
        role.setDescription("Employee");
        
        EmployeeModel employee = new EmployeeModel();
        employee.setEmail(email);
        employee.setUser(user);
        employee.setRole(role);
        
        when(userRepository.findByLogin(email)).thenReturn(null);
        when(employeeRepository.findByEmail(email)).thenReturn(employee);
        when(employeeRepository.findByUserId(user.getId())).thenReturn(employee);
        when(jwtUtil.generateToken(email, false, "Employee")).thenReturn(expectedToken);

        // Act
        String result = authService.login(email, password);

        // Assert
        assertEquals(expectedToken, result);
    }

    @Test
    void login_WithInvalidCredentials_ReturnsNull() {
        // Arrange
        String login = "test@test.com";
        String password = "wrongpassword";
        
        when(userRepository.findByLogin(login)).thenReturn(null);
        when(employeeRepository.findByEmail(login)).thenReturn(null);

        // Act
        String result = authService.login(login, password);

        // Assert
        assertNull(result);
    }

    @Test
    void validateToken_ReturnsDecodedJWT() {
        // Arrange
        String token = "valid.jwt.token";
        DecodedJWT expectedJWT = mock(DecodedJWT.class);
        when(jwtUtil.validateToken(token)).thenReturn(expectedJWT);

        // Act
        DecodedJWT result = authService.validateToken(token);

        // Assert
        assertEquals(expectedJWT, result);
        verify(jwtUtil).validateToken(token);
    }

    @Test
    void forgotPassword_WithValidEmail_SendsRecoveryEmail() {
        // Arrange
        String email = "test@test.com";
        UserModel user = new UserModel();
        EmployeeModel employee = new EmployeeModel();
        employee.setUser(user);
        
        when(employeeRepository.findByEmail(email)).thenReturn(employee);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        ResponseEntity<String> response = authService.forgotPassword(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email de recuperação enviado com sucesso", response.getBody());
        verify(userRepository).save(user);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void forgotPassword_WithInvalidEmail_ReturnsNotFound() {
        // Arrange
        String email = "nonexistent@test.com";
        when(employeeRepository.findByEmail(email)).thenReturn(null);

        // Act
        ResponseEntity<String> response = authService.forgotPassword(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Email não encontrado", response.getBody());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void resetPassword_WithValidData_ResetsPassword() {
        // Arrange
        String email = "test@test.com";
        String recoveryCode = "123456";
        String newPassword = "newpassword123";
        
        UserModel user = new UserModel();
        user.setRecoveryCode(recoveryCode);
        
        EmployeeModel employee = new EmployeeModel();
        employee.setUser(user);
        
        when(employeeRepository.findByEmail(email)).thenReturn(employee);

        // Act
        ResponseEntity<String> response = authService.resetPassword(email, recoveryCode, newPassword);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Senha alterada com sucesso", response.getBody());
        assertNull(user.getRecoveryCode());
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_WithInvalidEmail_ReturnsNotFound() {
        // Arrange
        String email = "nonexistent@test.com";
        when(employeeRepository.findByEmail(email)).thenReturn(null);

        // Act
        ResponseEntity<String> response = authService.resetPassword(email, "123456", "newpassword");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Email não encontrado", response.getBody());
    }

    @Test
    void resetPassword_WithInvalidRecoveryCode_ReturnsBadRequest() {
        // Arrange
        String email = "test@test.com";
        String recoveryCode = "123456";
        String invalidCode = "654321";
        
        UserModel user = new UserModel();
        user.setRecoveryCode(recoveryCode);
        
        EmployeeModel employee = new EmployeeModel();
        employee.setUser(user);
        
        when(employeeRepository.findByEmail(email)).thenReturn(employee);

        // Act
        ResponseEntity<String> response = authService.resetPassword(email, invalidCode, "newpassword");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Código de recuperação inválido", response.getBody());
    }
}