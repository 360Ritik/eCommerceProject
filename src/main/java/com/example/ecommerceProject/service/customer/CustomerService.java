package com.example.ecommerceProject.service.customer;


import com.example.ecommerceProject.dto.CustomerRegisterDto;
import com.example.ecommerceProject.dto.PasswordReset;
import com.example.ecommerceProject.dto.ResponseDto;
import com.example.ecommerceProject.enums.Authority;
import com.example.ecommerceProject.model.tokenStore.JwtToken;
import com.example.ecommerceProject.model.tokenStore.RegisterToken;
import com.example.ecommerceProject.model.user.Address;
import com.example.ecommerceProject.model.user.Customer;
import com.example.ecommerceProject.model.user.Role;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.*;
import com.example.ecommerceProject.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
@Transactional
public class CustomerService implements CustomerRepo {

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
    final ModelMapper modelMapper;


    final ResponseService responseService;

    final MessageSource messageSource;
    final
    JwtTokenRepo jwtTokenRepo;

    final EmailSenderRepo sendSimpleEmail;


    @Autowired
    public CustomerService(UserRepo userRepo, RoleRepo roleRepo, JwtService jwtService, RegisterTokenRepo registerTokenRepo, EmailSenderRepo emailSenderRepo, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ResponseService responseService, MessageSource messageSource, JwtTokenRepo jwtTokenRepo, EmailSenderRepo sendSimpleEmail) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtService = jwtService;
        this.registerTokenRepo = registerTokenRepo;
        this.emailSenderRepo = emailSenderRepo;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.responseService = responseService;
        this.messageSource = messageSource;
        this.jwtTokenRepo = jwtTokenRepo;
        this.sendSimpleEmail = sendSimpleEmail;
    }


    public Boolean isValidToken(String token) {
        return registerTokenRepo.existsRegisterTokenByUuidToken(token) && !jwtService.isTokenTimeExpired(token);
    }


    public void resendUserToken(String token) {
        if (registerTokenRepo.existsRegisterTokenByUuidToken(token) && jwtService.isTokenTimeExpired(token)) {
            RegisterToken registerToken = registerTokenRepo.getRegisterTokenByUuidToken(token);
            Customer customer = (Customer) userRepo.getReferenceById(registerToken.getUser().getId());


            String uUidToken = UUID.randomUUID().toString();
            registerToken.setUuidToken(uUidToken);


            registerToken.setValid(LocalDateTime.now().plusHours(3));
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

//    public void generateRegisterUserToken(String email) {
//        emailSenderRepo.sendSimpleEmail(email, token, "customer");
//    }

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

            user.setPasswordUpdateDate(LocalDateTime.now());
            userRepo.save(user);
            return true;
        }
        return false;
    }


    public void logoutCustomer(String token) {
        JwtToken jwtToken = jwtTokenRepo.findByToken(token);
        jwtTokenRepo.deleteById(jwtToken.getId());

    }

    @Override
    public ResponseEntity<ResponseDto> registerNewCustomer(CustomerRegisterDto customerRegisterDto, Locale locale) {
        log.info("Registration with this email: {}", customerRegisterDto.getEmail());
        if (checkForEmail(customerRegisterDto)) {
            String message = messageSource.getMessage("register.email", null, locale);
            return responseService.badRequest(null, message);
        }
        if (!customerRegisterDto.getPassword().equals(customerRegisterDto.getConfirmPassword())) {
            String message = messageSource.getMessage("register.password", null, locale);
            return responseService.badRequest(null, message);
        }
        createCustomer(customerRegisterDto, locale);
        String message = messageSource.getMessage("register.message", null, locale);
        return responseService.created(null, message);

    }

    @Override
    public void createCustomer(CustomerRegisterDto customerDto, Locale locale) {
        Customer customer = modelMapper.map(customerDto, Customer.class);

//        customer.setFirstName(customerDto.getFirstName());
//        customer.setMiddleName(customerDto.getMiddleName());
//        customer.setLastName(customerDto.getLastName());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
//        customer.setEmail(customerDto.getEmail());
//        customer.setContact(customerDto.getContact());

        Role role = roleRepo.findByAuthority(Authority.CUSTOMER);
        customer.setRoles(Collections.singletonList(role));


        //Customer Address
        ArrayList<Address> list = new ArrayList<>();
        for (Address address : customerDto.getAddresses()) {
            list.add(address);
            customer.setAddresses(list);
            address.setUser(customer);
        }
        userRepo.save(customer);

        // Generate UUid Token

        String token = createUuid(customer);


        //   customer.setRegisterToken(registerToken);
        String messageOfBody = messageSource.getMessage("register.email.message", null, locale);
        String messageOfSubject = messageSource.getMessage("register.email.subject", null, locale);
        String body = messageOfBody + "http://localhost:8080/auth/activation/customer?token=" + token;
        emailSenderRepo.sendSimpleEmail(customer.getEmail(), body, messageOfSubject);

    }


    @Override
    public Boolean checkForEmail(CustomerRegisterDto customerRegisterDto) {
        return userRepo.existsByEmail(customerRegisterDto.getEmail());
    }

    @Override
    public <T> String createUuid(T object) {
        RegisterToken registerToken = new RegisterToken();
        String token = UUID.randomUUID().toString();
        registerToken.setUuidToken(token);

        if (object instanceof Customer customer) {
            registerToken.setUser(customer);
        } else if (object instanceof User user) {
            registerToken.setUser(user);
        }

        registerToken.setValid(LocalDateTime.now().plusHours(3));

        registerTokenRepo.save(registerToken);
        return token;
    }

    @Override
    public ResponseEntity<ResponseDto> activatingCustomer(String token) {
        log.info("Activating account with this token: {}", token);
        if (checkForValidToken(token)) {
            return responseService.success(null, "Account has been activated");
        }
        return responseService.badRequest(null, "Token is incorrect or expired");
    }

    private boolean checkForValidToken(String token) {

        RegisterToken registerToken = registerTokenRepo.findByUuidToken(token).orElse(null);
        User user = userRepo.findById(registerToken.getUser().getId()).orElse(null);
        if (user != null && registerToken.getValid().isAfter(LocalDateTime.now())) {

            user.setIsActive(true);
            userRepo.save(user);
            registerTokenRepo.deleteById(registerToken.getId());
            return true;
        } else if (!Objects.isNull(user)) {
            registerTokenRepo.deleteById(registerToken.getId());
            User user1 = userRepo.findById(registerToken.getUser().getId()).orElse(null);

            String userToken = createUuid(user1);

            String body = "To activate your account, please verify your email on given link " +
                    "http://localhost:8080/auth/activation/customer?token=" + userToken;
            emailSenderRepo.sendSimpleEmail(user.getEmail(), body, "Activation Mail");
        }
        return false;
    }

}
