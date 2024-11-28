package org.company.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.company.insurance.service.EmailService;
import org.company.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;
    private final UserService service;

    public EmailController(UserService service) {
        this.service = service;
    }

    @GetMapping("/send-test-email")
    @PreAuthorize("hasRole('ROLE_AGENT')")
    public String sendTestEmail() {
        emailService.sendTestEmail();
        return "Test email sent!";
    }
    @GetMapping("/admin")
    @Operation(summary = "ADMIN only is able to access this endpoint")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }
}
