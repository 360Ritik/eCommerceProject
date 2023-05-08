package com.example.ecommerceProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";

    public TokenResponseDto(String token) {

        accessToken = token;
    }

}