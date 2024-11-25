package org.company.insurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @Size(min = 5, max = 50)
    @NotBlank
    private String username;

    @Size(min = 5, max = 255)
    @NotBlank
    @Email(message = "Email format user@example.com")
    private String email;

    @Size(max = 255)
    private String password;

}
