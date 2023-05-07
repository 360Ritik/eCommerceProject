package com.example.ecommerceProject.controller;


import com.example.ecommerceProject.dto.LoginDto;
import com.example.ecommerceProject.service.customer.CustomerService;
import com.example.ecommerceProject.service.seller.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class Login {

    final
    CustomerService customerService;
    final
    SellerService sellerService;

    private final AuthenticationManager authenticationManager;

    public Login(CustomerService customerService, SellerService sellerService, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.sellerService = sellerService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/customer")
    public ResponseEntity<String> authenticateCustomerAndGetToken(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated() && customerService.isActiveUser(loginDto.getEmail())) {
            String loginToken = customerService.generateLoginUserToken(loginDto.getEmail(), 24L);
            return new ResponseEntity<>(loginToken, HttpStatus.OK);
        }  if (!customerService.isActiveUser(loginDto.getEmail())) {
            return new ResponseEntity<>(customerService.getLoginToken(loginDto.getEmail()), HttpStatus.OK);
        } {
            return new ResponseEntity<>("user not found !", HttpStatus.OK);
        }
    }

    @PostMapping("/seller")
    public ResponseEntity<String> authenticateSellerAndGetToken( @Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated() && sellerService.isActiveUser(loginDto.getEmail())) {
            String loginToken = sellerService.generateLoginUserToken(loginDto.getEmail(), 24L);
            return new ResponseEntity<>(loginToken, HttpStatus.OK);
        } else if (!sellerService.isActiveUser(loginDto.getEmail())) {
            return new ResponseEntity<>(sellerService.getLoginToken(loginDto.getEmail()), HttpStatus.OK);
        } else {

            return new ResponseEntity<>("user not found !", HttpStatus.BAD_REQUEST);

        }
    }


}
