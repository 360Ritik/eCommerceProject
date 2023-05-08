package com.example.ecommerceProject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private HttpStatus code;
    private String message;
    private T body;

//    public ResponseDto(String message,HttpStatus code) {
//        this.message = message;
//        this.code = code;
//    }
//
//    public ResponseDto(HttpStatus code,T body) {
//        this.code = code;
//        this.body = body;
//    }
}