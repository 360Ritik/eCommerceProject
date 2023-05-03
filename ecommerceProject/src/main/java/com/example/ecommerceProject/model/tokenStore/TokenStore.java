package com.example.ecommerceProject.model.tokenStore;

import com.example.ecommerceProject.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class TokenStore {

    @OneToOne
    User user;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private Boolean isDeleted;
    private String token;

}
