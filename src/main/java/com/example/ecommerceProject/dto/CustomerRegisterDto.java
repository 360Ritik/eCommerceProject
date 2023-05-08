package com.example.ecommerceProject.dto;


import com.example.ecommerceProject.model.user.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerRegisterDto {


    private String firstName;
    private String middleName;
    private String lastName;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one digit, and one special character (@$!%*#?&)")
    private String password;
    @Email(message = "it should be email type")
    private String email;
    private List<Address> addresses;
    private String confirmPassword;
    private Long contact;


}
