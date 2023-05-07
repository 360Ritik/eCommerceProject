package com.example.ecommerceProject.controller;


import com.example.ecommerceProject.dto.LoginDto;
import com.example.ecommerceProject.service.customer.CustomerService;
import com.example.ecommerceProject.service.seller.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class Login {

    @Autowired
    CustomerService customerService;
    @Autowired
    SellerService sellerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/customer")
    public ResponseEntity<String> authenticateCustomerAndGetToken(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated() && customerService.isActiveUser(loginDto.getEmail())) {
            customerService.generateLoginUserToken(loginDto.getEmail(), 24L);
            return new ResponseEntity<>("login successfully", HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("user not found !");
        }
    }

    @PostMapping("/seller")
    public ResponseEntity<String> authenticateSellerAndGetToken(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated() && sellerService.isActiveUser(loginDto.getEmail())) {
            sellerService.generateLoginUserToken(loginDto.getEmail(), 24L);
            return new ResponseEntity<>("login Successfully!", HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }


}
