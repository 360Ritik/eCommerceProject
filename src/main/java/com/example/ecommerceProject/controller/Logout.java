package com.example.ecommerceProject.controller;

import com.example.ecommerceProject.service.customer.CustomerService;
import com.example.ecommerceProject.service.seller.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class Logout {


    @Autowired
    CustomerService customerService;

    @Autowired
    SellerService sellerService;

    @Autowired
    AuthenticationManager authenticationManager;


    @DeleteMapping("/customer")
    public ResponseEntity<String> customerLogout(@RequestParam String token) {

        customerService.logoutCustomer(token);
        return new ResponseEntity<>("logout successfully", HttpStatus.OK);
    }

    @DeleteMapping("/seller")
    public ResponseEntity<String> sellerLogout(@RequestParam String token) {

        sellerService.logoutSeller(token);
        return new ResponseEntity<>("logout successfully", HttpStatus.OK);
    }
}
