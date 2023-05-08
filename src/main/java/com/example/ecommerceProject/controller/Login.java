package com.example.ecommerceProject.controller;


import com.example.ecommerceProject.dto.CustomerRegisterDto;
import com.example.ecommerceProject.dto.LoginDto;
import com.example.ecommerceProject.dto.PasswordChangeDto;
import com.example.ecommerceProject.dto.ResponseDto;
import com.example.ecommerceProject.repository.EmailSenderRepo;
import com.example.ecommerceProject.repository.LoginService;
import com.example.ecommerceProject.repository.PasswordService;
import com.example.ecommerceProject.repository.UserRepo;
import com.example.ecommerceProject.service.customer.CustomerService;
import com.example.ecommerceProject.service.seller.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
public class Login {

    final
    CustomerService customerService;
    final
    SellerService sellerService;

    final
    UserRepo userRepo;
    final PasswordService passwordService;


    final
    EmailSenderRepo emailSenderRepo;

    final AuthenticationManager authenticationManager;

    final LoginService loginService;

    public Login(CustomerService customerService, SellerService sellerService, UserRepo userRepo, PasswordService passwordService, EmailSenderRepo emailSenderRepo, AuthenticationManager authenticationManager, LoginService loginService) {
        this.customerService = customerService;
        this.sellerService = sellerService;
        this.userRepo = userRepo;
        this.passwordService = passwordService;
        this.emailSenderRepo = emailSenderRepo;
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
    }

    @PostMapping("registration/customer")
    public ResponseEntity<ResponseDto> registerCustomer(@Valid @RequestBody CustomerRegisterDto customerRegisterDto,
                                                        @RequestHeader(name = "Accept-Language",
                                                                required = false) Locale locale) {
        return customerService.registerNewCustomer(customerRegisterDto, locale);
    }

    @PutMapping("activation/customer")
    public ResponseEntity<ResponseDto> activateCustomerAccount(@RequestHeader(name = "token") String token) {
        return customerService.activatingCustomer(token);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        return loginService.login(loginDto);
    }

    @PutMapping("/reset/password")
    public ResponseEntity<ResponseDto> resetPassword(@RequestParam String token, @Valid @RequestBody PasswordChangeDto forgetPasswordDto) {
        return passwordService.resetPassword(token, forgetPasswordDto);
    }

    @PostMapping("/receiveToken/{email}")
    public ResponseEntity<ResponseDto> receiveTokenUrl(@PathVariable String email) {
        return passwordService.resetLink(email);
    }


}
