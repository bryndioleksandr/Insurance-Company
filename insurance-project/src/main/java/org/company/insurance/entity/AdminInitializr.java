package org.company.insurance.entity;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Size;
import org.company.insurance.dto.UserCreationDto;
import org.company.insurance.enums.Role;
import org.company.insurance.repository.UserRepository;
import org.company.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminInitializr {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.phoneNumber}")
    private String phoneNumber;



    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializr(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializeAdmin() {
        String firstName = "Oleksandr";
        String surname = "Bryndio";
        LocalDate birthDate = LocalDate.of(2006, 3, 4);
        String role = Role.valueOf("ROLE_ADMIN").toString();
        LocalDate hireDate = null;
        String position = null;
        if (!userRepository.existsByUsername(adminUsername)) {
            UserCreationDto adminDto = new UserCreationDto(
                    adminEmail,
                    adminUsername,
                    firstName,
                    surname,
                    phoneNumber,
                    birthDate,
                    passwordEncoder.encode(adminPassword),
                    Role.valueOf(role),
                    hireDate,
                    position
            );
            userService.createUser(adminDto);
            System.out.println("Initial admin user created: " + adminUsername);
        }
    }
}