package com.example.ecommerceProject.service.seller;

import com.example.ecommerceProject.dto.SellerDto;
import com.example.ecommerceProject.enums.Authority;
import com.example.ecommerceProject.model.user.Address;
import com.example.ecommerceProject.model.user.Role;
import com.example.ecommerceProject.model.user.Seller;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.EmailSenderRepo;
import com.example.ecommerceProject.repository.RoleRepo;
import com.example.ecommerceProject.repository.UserRepo;
import com.example.ecommerceProject.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
    EmailSenderRepo emailSenderRepo;

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
        userRepo.save(seller);


    }

    public void generateRegisterUserToken(String email, Integer time) {
        String token = jwtService.generateToken(email, time);
        emailSenderRepo.sendSimpleEmail(email, token, "seller");
    }

    public String generateLoginUserToken(String email, Integer time) {
        return jwtService.generateToken(email, time);
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


    public boolean isActiveUser(String email) {
        User user = userRepo.getByEmail(email);
        return user.getIsActive();
    }
}
