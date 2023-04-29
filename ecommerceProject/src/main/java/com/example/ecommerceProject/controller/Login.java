package com.example.ecommerceProject.controller;


import com.example.ecommerceProject.dto.LoginDto;
import com.example.ecommerceProject.service.customer.CustomerService;
import com.example.ecommerceProject.service.seller.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String authenticateCustomerAndGetToken(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated() && customerService.isActiveUser(loginDto.getEmail())) {
            return customerService.generateLoginUserToken(loginDto.getEmail(), 300);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/seller")
    public String authenticateSellerAndGetToken(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated() && sellerService.isActiveUser(loginDto.getEmail())) {
            return sellerService.generateLoginUserToken(loginDto.getEmail(), 300);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }


}
