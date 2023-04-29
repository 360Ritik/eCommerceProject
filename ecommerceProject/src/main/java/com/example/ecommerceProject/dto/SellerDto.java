package com.example.ecommerceProject.dto;


import com.example.ecommerceProject.model.user.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SellerDto {

    private String firstName;
    private String middleName;
    private String lastName;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one digit, and one special character (@$!%*#?&)")
    private String password;
    @Email(message = "it should be email type")
    private String email;
    private Address addresses;
    private String confirmPassword;

    private String gst;
    private Long companyContact;
    private String companyName;

}
