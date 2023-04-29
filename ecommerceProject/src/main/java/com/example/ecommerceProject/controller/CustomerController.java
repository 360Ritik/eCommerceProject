package com.example.ecommerceProject.controller;

import com.example.ecommerceProject.dto.CustomerDto;
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


    @Autowired
    CustomerService customerService;

    @Autowired
    EmailSenderRepo emailSenderRepo;

    @PostMapping("/registration")
    public ResponseEntity<String> saveCustomer(@Valid @RequestBody CustomerDto customerDto) {
        if (!customerDto.getPassword().equals(customerDto.getConfirmPassword())) {

            return new ResponseEntity<>("Password and Confirm password didn't match", HttpStatus.BAD_REQUEST);
        }

        if (customerService.exitsEmailId(customerDto.getEmail())) {
            return new ResponseEntity<>("Email Already Registered", HttpStatus.BAD_REQUEST);
        }

        customerService.generateRegisterUserToken(customerDto.getEmail(), 15);
        customerService.saveCustomerDetails(customerDto);
        return new ResponseEntity<>("User Registered Successfully!", HttpStatus.OK);
    }

    @PatchMapping("/activation")
    public ResponseEntity<String> activateCustomerAccount(@RequestParam String token) {
        if (customerService.isValidToken(token)) {
            customerService.ChangeCustomerStatus(token);
            return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
        } else {

            return new ResponseEntity<>("This is not a valid token", HttpStatus.BAD_REQUEST);
        }
    }
}
