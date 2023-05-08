package com.example.ecommerceProject.repository;

import com.example.ecommerceProject.dto.CustomerRegisterDto;
import com.example.ecommerceProject.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Repository
public interface CustomerRepo {


    void createCustomer(CustomerRegisterDto customerDto, Locale locale);

    Boolean checkForEmail(CustomerRegisterDto customerRegisterDto);

    <T> String createUuid(T type);

    ResponseEntity<ResponseDto> activatingCustomer(String token);

    ResponseEntity<ResponseDto> registerNewCustomer(CustomerRegisterDto customerRegisterDto, Locale locale);
}
