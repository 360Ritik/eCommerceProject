package com.example.ecommerceProject.repository;

import com.example.ecommerceProject.dto.PasswordChangeDto;
import com.example.ecommerceProject.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordService {
    ResponseEntity<ResponseDto> resetLink(String email);

    ResponseEntity<ResponseDto> resetPassword(String token, PasswordChangeDto forgetPasswordDto);
}