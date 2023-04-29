package com.example.ecommerceProject.service.customer;


import com.example.ecommerceProject.dto.CustomerDto;
import com.example.ecommerceProject.enums.Authority;
import com.example.ecommerceProject.model.user.Address;
import com.example.ecommerceProject.model.user.Customer;
import com.example.ecommerceProject.model.user.Role;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.EmailSenderRepo;
import com.example.ecommerceProject.repository.RoleRepo;
import com.example.ecommerceProject.repository.UserRepo;
import com.example.ecommerceProject.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;


@Service
public class CustomerService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;

    @Autowired
    JwtService jwtService;

    @Autowired
    EmailSenderRepo emailSenderRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    public void saveCustomerDetails(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setMiddleName(customerDto.getMiddleName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setEmail(customerDto.getEmail());
        customer.setContact(customerDto.getContact());
//        customer.setAddresses(customerDto.getAddresses());

        // Customer role
        Role role = roleRepo.findByAuthority(Authority.CUSTOMER);
        customer.setRoles(Collections.singletonList(role));

        //Created by
        customer.setCreatedBy(customerDto.getFirstName());

        //Customer Address
        ArrayList<Address> list = new ArrayList<>();
        for (Address address : customerDto.getAddresses()) {
            list.add(address);
            customer.setAddresses(list);
            address.setUser(customer);
        }


        userRepo.save(customer);
    }

    public Boolean exitsEmailId(String emailId) {
        return userRepo.existsByEmail(emailId);
    }

    public Boolean isValidToken(String Token) {
        String userEmail = jwtService.extractUsername(Token);
        boolean status1 = !(jwtService.isTokenExpired(Token));
        boolean status2 = userRepo.existsByEmail(userEmail);
        System.out.println(status1 + " " + status2);
        return status1 && status2;
    }

    public void ChangeCustomerStatus(String token) {
        String customerEmail = jwtService.extractUsername(token);
        User user = userRepo.getByEmail(customerEmail);
        user.setIsActive(true);
        userRepo.save(user);
    }

    public void generateRegisterUserToken(String email, Integer time) {
        String token = jwtService.generateToken(email, time);
        emailSenderRepo.sendSimpleEmail(email, token, "customer");
    }

    public String generateLoginUserToken(String email, Integer time) {
        return jwtService.generateToken(email, time);
    }

    public boolean isActiveUser(String email) {
        User user = userRepo.getByEmail(email);
        return user.getIsActive();
    }
}
