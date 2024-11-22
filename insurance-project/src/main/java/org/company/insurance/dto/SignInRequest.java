package org.company.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequest {
    @Size(min = 5, max = 50)
    @NotBlank
    private String username;

    @Size(min = 8, max = 255)
    @NotBlank
    private String password;
}
