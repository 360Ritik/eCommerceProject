package com.example.ecommerceProject.model.tokenStore;

import com.example.ecommerceProject.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    @OneToOne
    User user;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwt_generator")
    @SequenceGenerator(name = "jwt_generator", sequenceName = "jwt_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    private String token;
    private LocalDateTime expireTime;
}
