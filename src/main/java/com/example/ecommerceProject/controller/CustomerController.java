package com.example.ecommerceProject.controller;

import com.example.ecommerceProject.dto.CustomerDto;
import com.example.ecommerceProject.dto.PasswordReset;
import com.example.ecommerceProject.repository.EmailSenderRepo;
import com.example.ecommerceProject.service.customer.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    final
    CustomerService customerService;

    final
    EmailSenderRepo emailSenderRepo;

    @Autowired
    public CustomerController(CustomerService customerService, EmailSenderRepo emailSenderRepo) {
        this.customerService = customerService;
        this.emailSenderRepo = emailSenderRepo;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> saveCustomer(@Valid @RequestBody CustomerDto customerDto) {
        if (!customerDto.getPassword().equals(customerDto.getConfirmPassword())) {

            return new ResponseEntity<>("Password and Confirm password didn't match", HttpStatus.BAD_REQUEST);
        }

        if (customerService.exitsEmailId(customerDto.getEmail())) {
            return new ResponseEntity<>("Email Already Registered", HttpStatus.BAD_REQUEST);
        }
        customerService.saveCustomerDetails(customerDto);
        customerService.generateRegisterUserToken(customerDto.getEmail());
        return new ResponseEntity<>("User Registered Successfully!", HttpStatus.OK);
    }

    @PatchMapping("/activation")
    public ResponseEntity<String> activateCustomerAccount(@RequestParam String token) {
        if (customerService.isValidToken(token)) {
            customerService.ChangeCustomerStatus(token);
            return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
        } else {
            customerService.resendUserToken(token);
            return new ResponseEntity<>("This is an expired token!\n we have send the new token to your email-id", HttpStatus.OK);
        }
    }

    @PatchMapping("/forgot/password")
    public ResponseEntity<String> userPasswordReset(@Valid @RequestBody PasswordReset email) {

        if (!email.getPassword().equals(email.getConfirmPassword())) {

            return new ResponseEntity<>("Password and Confirm password didn't match", HttpStatus.BAD_REQUEST);
        }
        if (customerService.passwordReset(email)) {

            return new ResponseEntity<>("Password changed Successfully!", HttpStatus.OK);
        } else {

            return new ResponseEntity<>("User doesn't Exit!", HttpStatus.BAD_REQUEST);
        }


    }
}
