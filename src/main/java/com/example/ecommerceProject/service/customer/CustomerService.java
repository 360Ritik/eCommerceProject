package com.example.ecommerceProject.service.customer;


import com.example.ecommerceProject.dto.CustomerDto;
import com.example.ecommerceProject.dto.PasswordReset;
import com.example.ecommerceProject.enums.Authority;
import com.example.ecommerceProject.model.tokenStore.JwtToken;
import com.example.ecommerceProject.model.tokenStore.RegisterToken;
import com.example.ecommerceProject.model.user.*;
import com.example.ecommerceProject.repository.*;
import com.example.ecommerceProject.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;


@Service
@Transactional
public class CustomerService {

    final
    UserRepo userRepo;
    final
    RoleRepo roleRepo;
    final
    JwtService jwtService;
    final
    RegisterTokenRepo registerTokenRepo;
    final
    EmailSenderRepo emailSenderRepo;
    final
    PasswordEncoder passwordEncoder;

    final
    JwtTokenRepo jwtTokenRepo;


    private String token = null;

    @Autowired
    public CustomerService(UserRepo userRepo, RoleRepo roleRepo, JwtService jwtService, RegisterTokenRepo registerTokenRepo, EmailSenderRepo emailSenderRepo, PasswordEncoder passwordEncoder, JwtTokenRepo jwtTokenRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtService = jwtService;
        this.registerTokenRepo = registerTokenRepo;
        this.emailSenderRepo = emailSenderRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenRepo = jwtTokenRepo;
    }

    public void saveCustomerDetails(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setMiddleName(customerDto.getMiddleName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setEmail(customerDto.getEmail());
        customer.setContact(customerDto.getContact());

        Role role = roleRepo.findByAuthority(Authority.CUSTOMER);
        customer.setRoles(Collections.singletonList(role));

        //Created by
        // customer.setCreatedBy(customerDto.getFirstName());

        //Customer Address
        ArrayList<Address> list = new ArrayList<>();
        for (Address address : customerDto.getAddresses()) {
            list.add(address);
            customer.setAddresses(list);
            address.setUser(customer);
        }

        // Generate UUid Token
        RegisterToken registerToken = new RegisterToken();
        UUID uuid = UUID.randomUUID();
        registerToken.setUuidToken(uuid.toString());
        token = uuid.toString();
        registerToken.setUser(customer);
        long expirationTimeMillis = new Date().getTime() + (60 * 1000);
        Date expirationDate = new Date(expirationTimeMillis);
        registerToken.setValid(expirationDate);

        customer.setRegisterToken(registerToken);


        userRepo.save(customer);

    }

    public Boolean exitsEmailId(String emailId) {
        return userRepo.existsByEmail(emailId);
    }

    public Boolean isValidToken(String token) {
        return registerTokenRepo.existsRegisterTokenByUuidToken(token) && !jwtService.isTokenTimeExpired(token);
    }

    @Modifying
    public void resendUserToken(String token) {
        if (registerTokenRepo.existsRegisterTokenByUuidToken(token) && jwtService.isTokenTimeExpired(token)) {
            RegisterToken registerToken = registerTokenRepo.getRegisterTokenByUuidToken(token);
            Customer customer = (Customer) userRepo.getReferenceById(registerToken.getUser().getId());


            UUID uuid = UUID.randomUUID();
            registerToken.setUuidToken(uuid.toString());
            token = uuid.toString();
            long expirationTimeMillis = new Date().getTime() + (60 * 1000);
            Date expirationDate = new Date(expirationTimeMillis);
            registerToken.setValid(expirationDate);
            customer.setRegisterToken(registerToken);
            registerTokenRepo.save(registerToken);

            userRepo.save(customer);

            emailSenderRepo.sendSimpleEmail(customer.getEmail(), token, "customer");
        }


    }

    public void ChangeCustomerStatus(String token) {
        RegisterToken registerToken = registerTokenRepo.getRegisterTokenByUuidToken(token);
        User user = userRepo.getReferenceById(registerToken.getUser().getId());
        user.setIsActive(true);
        userRepo.save(user);
    }

    public void generateRegisterUserToken(String email) {
        emailSenderRepo.sendSimpleEmail(email, token, "customer");
    }

    public String generateLoginUserToken(String email, Long hours) {

        User user = userRepo.getByEmail(email);
        String loginToken = jwtService.generateToken(email, hours);
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(loginToken);
        jwtToken.setUser(user);
        jwtTokenRepo.save(jwtToken);
        userRepo.save(user);

        return loginToken;


    }

    public boolean isActiveUser(String email) {
        User user = userRepo.getByEmail(email);
        return user.getIsActive() && !jwtTokenRepo.existsAllByUserId(user.getId());
    }

    public String getLoginToken(String email) {
        User user = userRepo.getByEmail(email);
        JwtToken jwtToken = jwtTokenRepo.getByUserId(user.getId());
        return jwtToken.getToken();

    }


    public Boolean passwordReset(PasswordReset passwordReset) {

        if (userRepo.existsByEmail(passwordReset.getEmail())) {
            User user = userRepo.getByEmail(passwordReset.getEmail());
            user.setPassword(passwordEncoder.encode(passwordReset.getPassword()));
            userRepo.save(user);
            return true;
        }
        return false;
    }


    public void logoutCustomer(String token) {
        JwtToken jwtToken = jwtTokenRepo.findByToken(token);
        jwtTokenRepo.deleteById(jwtToken.getId());

    }
}
