package com.example.ecommerceProject.controller;


import com.example.ecommerceProject.dto.PasswordReset;
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
        sellerService.savesellerDetails(sellerDto);
        sellerService.generateRegisterUserToken(sellerDto.getEmail());
        return new ResponseEntity<>("User Registered Successfully", HttpStatus.OK);
    }

    @PatchMapping("/activation")
    public ResponseEntity<String> activateSellerAccount(@RequestParam String token) {
        if (sellerService.isValidToken(token)) {
            sellerService.ChangeCustomerStatus(token);
            return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
        } else {
            sellerService.resendSellerToken(token);
            return new ResponseEntity<>("This is an expired token!\n we have send the new token to your email-id", HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<String> userPasswordReset(@Valid @RequestBody PasswordReset email) {

        if (!email.getPassword().equals(email.getConfirmPassword())) {

            return new ResponseEntity<>("Password and Confirm password didn't match", HttpStatus.BAD_REQUEST);
        }
        if (sellerService.passwordReset(email)) {

            return new ResponseEntity<>("Password changed Successfully!", HttpStatus.OK);
        } else {

            return new ResponseEntity<>("User doesn't Exit!", HttpStatus.BAD_REQUEST);
        }


    }
}
