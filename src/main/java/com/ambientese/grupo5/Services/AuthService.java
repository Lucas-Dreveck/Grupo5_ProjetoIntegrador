package com.ambientese.grupo5.Services;

import java.security.SecureRandom;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.Model.EmployeeModel;
import com.ambientese.grupo5.Model.UserModel;
import com.ambientese.grupo5.Repository.EmployeeRepository;
import com.ambientese.grupo5.Repository.UserRepository;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final JavaMailSender mailSender;
    private final JWTUtil jwtUtil;

    public AuthService(UserRepository userRepository, EmployeeRepository employeeRepository, 
                       JavaMailSender mailSender, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
    }

    public boolean authenticate(String login, String password) {
        UserModel user = userRepository.findByLogin(login);
        if (user == null) {
            EmployeeModel employee = employeeRepository.findByEmail(login);
            if (employee != null) {
                user = employee.getUser();
            }
        }
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    public String login(String login, String plainTextPassword) {
        if (authenticate(login, plainTextPassword)) {
            UserModel user = userRepository.findByLogin(login);
            if (user == null) {
                EmployeeModel employee = employeeRepository.findByEmail(login);
                user = employee.getUser();
            }
            EmployeeModel employee = employeeRepository.findByUserId(user.getId());
            
            String role = (employee != null) ? employee.getRole().getDescription() : "Admin";
            return jwtUtil.generateToken(login, user.getIsAdmin(), role);
        }
        return null;
    }

    public DecodedJWT validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public ResponseEntity<String> forgotPassword(String email) {
        EmployeeModel employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não encontrado");
        }
        UserModel user = employee.getUser();
        String code = generateRecoveryCode();
        user.setRecoveryCode(code);
        userRepository.save(user);

        sendRecoveryEmail(email, code);

        return ResponseEntity.status(HttpStatus.OK).body("Email de recuperação enviado com sucesso");
    }

    private String generateRecoveryCode() {
        SecureRandom secureRandom = new SecureRandom();
        int code = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendRecoveryEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de recuperação de senha");
        message.setText("Seu código para recuperação da senha: " + code);
        mailSender.send(message);
    }

    public ResponseEntity<String> resetPassword(String email, String recoveryCode, String newPassword) {
        EmployeeModel employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não encontrado");
        }
        UserModel user = employee.getUser();

        if (!recoveryCode.equals(user.getRecoveryCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de recuperação inválido");
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setRecoveryCode(null); // Clear the recovery code after successful reset
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("Senha alterada com sucesso");
    }
}
