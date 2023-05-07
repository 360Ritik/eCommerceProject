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
    private Address addresses;     //^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$”
    private String confirmPassword;


    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message = " must constain two digits, followed by five uppercase letters, four digits, one uppercase letter, one digit or uppercase letter that is not zero, the letter 'Z', and one more digit or uppercase letter")
    private String gst;
    private Long companyContact;
    private String companyName;

}
