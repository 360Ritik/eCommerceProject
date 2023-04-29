package com.example.ecommerceProject.controller;


import com.example.ecommerceProject.dto.SellerDto;
import com.example.ecommerceProject.repository.UserRepo;
import com.example.ecommerceProject.service.seller.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SellerService sellerService;

    @PostMapping("/registration")
    public ResponseEntity<String> saveCustomer(@Valid @RequestBody SellerDto sellerDto) {
        if (!sellerDto.getPassword().equals(sellerDto.getConfirmPassword())) {

            return new ResponseEntity<>("Password and Confirm password didn't match", HttpStatus.BAD_REQUEST);
        }

        if (userRepo.existsByEmail(sellerDto.getEmail())) {
            return new ResponseEntity<>("Email Already Registered", HttpStatus.BAD_REQUEST);
        }
        sellerService.generateRegisterUserToken(sellerDto.getEmail(), 15);
        sellerService.savesellerDetails(sellerDto);
        return new ResponseEntity<>("User Registered Successfully", HttpStatus.OK);
    }

    @PatchMapping("/activation")
    public ResponseEntity<String> activateSellerAccount(@RequestParam String token) {
        if (sellerService.isValidToken(token)) {
            sellerService.ChangeCustomerStatus(token);
            return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
        } else {

            return new ResponseEntity<>("This is not a valid token", HttpStatus.BAD_REQUEST);
        }
    }
}
