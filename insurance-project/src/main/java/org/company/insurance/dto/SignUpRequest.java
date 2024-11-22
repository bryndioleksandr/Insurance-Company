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

//    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
//    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
//    @Email(message = "Email адрес должен быть в формате user@example.com")
//    private String email;

    @Size(max = 255)
    private String password;

}
