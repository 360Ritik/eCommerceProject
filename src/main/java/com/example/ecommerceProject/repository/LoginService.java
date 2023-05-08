package com.example.ecommerceProject.repository;

import com.example.ecommerceProject.dto.LoginDto;
import com.example.ecommerceProject.dto.ResponseDto;
import com.example.ecommerceProject.model.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginService {


    ResponseEntity<ResponseDto> login(LoginDto loginDto);

    String generateJwtToken(LoginDto loginDto, User user);
}