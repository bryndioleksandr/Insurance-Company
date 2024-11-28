package org.company.insurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @Size(min = 4, max = 50)
    @NotBlank
    private String username;

    @Size(min = 5, max = 255)
    @NotBlank
    @Email(message = "Email format user@example.com")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email should be valid")
    private String email;

    @Size(min = 8, max = 255)
    private String password;

}
