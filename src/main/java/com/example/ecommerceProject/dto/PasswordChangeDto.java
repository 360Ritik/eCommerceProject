package com.example.ecommerceProject.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PasswordChangeDto {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,15}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one digit, and one special character (@$!%*#?&)")
    private String password;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,15}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one digit, and one special character (@$!%*#?&)")
    private String confirmPassword;

    // Custom validation method for password and confirmPassword
    @AssertTrue(message = "Password and Confirm Password must match")
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}