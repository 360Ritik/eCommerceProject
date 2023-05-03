package com.example.ecommerceProject.service.seller;

import com.example.ecommerceProject.dto.PasswordReset;
import com.example.ecommerceProject.dto.SellerDto;
import com.example.ecommerceProject.enums.Authority;
import com.example.ecommerceProject.model.tokenStore.RegisterToken;
import com.example.ecommerceProject.model.user.Address;
import com.example.ecommerceProject.model.user.Role;
import com.example.ecommerceProject.model.user.Seller;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.EmailSenderRepo;
import com.example.ecommerceProject.repository.RegisterTokenRepo;
import com.example.ecommerceProject.repository.RoleRepo;
import com.example.ecommerceProject.repository.UserRepo;
import com.example.ecommerceProject.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class SellerService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    RegisterTokenRepo registerTokenRepo;
    @Autowired
    EmailSenderRepo emailSenderRepo;
    private String token = null;

    @Transactional

    public void savesellerDetails(SellerDto sellerDto) {

        Seller seller = new Seller();
        seller.setFirstName(sellerDto.getFirstName());
        seller.setMiddleName(sellerDto.getMiddleName());
        seller.setLastName(sellerDto.getLastName());
        seller.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        seller.setEmail(sellerDto.getEmail());

        seller.setCompanyName(sellerDto.getCompanyName());
        seller.setCompanyContact(sellerDto.getCompanyContact());
        seller.setGst(sellerDto.getGst());

        //Seller Address
        Address address = sellerDto.getAddresses();
        seller.setAddresses(Collections.singletonList(address));
        address.setUser(seller);

        // Seller Role
        Role role = roleRepo.findByAuthority(Authority.SELLER);
        seller.setRoles(Collections.singletonList(role));
        //seller.setRoles(List.of(role));


        //Created by
        seller.setCreatedBy(sellerDto.getFirstName());

        //Register UUId Token generation

        RegisterToken sellerToken = new RegisterToken();
        UUID uuid = UUID.randomUUID();
        sellerToken.setUuidToken(uuid.toString());
        token = uuid.toString();
        sellerToken.setUser(seller);
        long expirationTimeMillis = new Date().getTime() + (60 * 1000);
        Date expirationDate = new Date(expirationTimeMillis);
        sellerToken.setValid(expirationDate);
        seller.setRegisterToken(sellerToken);


        userRepo.save(seller);


    }

    public void generateRegisterUserToken(String email) {
        emailSenderRepo.sendSimpleEmail(email, token, "customer");
    }


    public Boolean isValidToken(String token) {
        return registerTokenRepo.existsRegisterTokenByUuidToken(token) && !jwtService.isTokenTimeExpired(token);
    }

    @Transactional
    public void resendSellerToken(String token) {
        if (registerTokenRepo.existsRegisterTokenByUuidToken(token) && jwtService.isTokenTimeExpired(token)) {
            RegisterToken registerToken = registerTokenRepo.getRegisterTokenByUuidToken(token);
            Seller seller = (Seller) userRepo.getReferenceById(registerToken.getUser().getId());

            UUID uuid = UUID.randomUUID();
            registerToken.setUuidToken(uuid.toString());
            token = uuid.toString();

            long expirationTimeMillis = new Date().getTime() + (60 * 1000);
            Date expirationDate = new Date(expirationTimeMillis);
            registerToken.setValid(expirationDate);
            seller.setRegisterToken(registerToken);
            registerTokenRepo.save(registerToken);

            userRepo.save(seller);

            emailSenderRepo.sendSimpleEmail(seller.getEmail(), token, "seller");
        }


    }

    public void ChangeCustomerStatus(String token) {
        RegisterToken registerToken = registerTokenRepo.getRegisterTokenByUuidToken(token);
        User user = userRepo.getReferenceById(registerToken.getUser().getId());
        user.setIsActive(true);
        userRepo.save(user);
    }

    public String generateLoginUserToken(String email, Long hours) {
        String token = jwtService.generateToken(email, hours);
        emailSenderRepo.sendSimpleEmail(email, token, "seller");

        return token;
    }

    public boolean isActiveUser(String email) {
        User user = userRepo.getByEmail(email);
        return user.getIsActive();
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
}
