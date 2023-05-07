package com.example.ecommerceProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordReset {
    @Email(message = "it should be email type")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one digit, and one special character (@$!%*#?&)")
    private String password;
    private String confirmPassword;
}
