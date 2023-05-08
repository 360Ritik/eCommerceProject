package com.example.ecommerceProject.controller;

import com.example.ecommerceProject.service.customer.CustomerService;
import com.example.ecommerceProject.service.seller.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class Logout {


    final
    CustomerService customerService;

    final
    SellerService sellerService;

    final
    AuthenticationManager authenticationManager;

    @Autowired
    public Logout(CustomerService customerService, SellerService sellerService, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.sellerService = sellerService;
        this.authenticationManager = authenticationManager;
    }


    @DeleteMapping("/customer")
    public ResponseEntity<String> customerLogout(@RequestHeader String token) {

        customerService.logoutCustomer(token);
        return new ResponseEntity<>("logout successfully", HttpStatus.OK);
    }

    @DeleteMapping("/seller")
    public ResponseEntity<String> sellerLogout(@RequestHeader String token) {

        sellerService.logoutSeller(token);
        return new ResponseEntity<>("logout successfully", HttpStatus.OK);
    }
}
