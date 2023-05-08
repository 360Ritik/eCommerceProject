package com.example.ecommerceProject.repository;

import com.example.ecommerceProject.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ResponseService {
    ResponseEntity<ResponseDto> success(Object body, String message);

    ResponseEntity<ResponseDto> created(Object body, String message);

    ResponseEntity<ResponseDto> badRequest(Object body, String message);

    ResponseEntity<ResponseDto> locked(Object body, String message);
}