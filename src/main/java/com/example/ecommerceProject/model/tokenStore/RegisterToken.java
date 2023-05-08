package com.example.ecommerceProject.model.tokenStore;

import com.example.ecommerceProject.model.user.User;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Enabled
@Data
@NoArgsConstructor
@Entity
public class RegisterToken {

    @OneToOne
    User user;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "register_generator")
    @SequenceGenerator(name = "register_generator", sequenceName = "register_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    private String uuidToken;
    private LocalDateTime valid;


}
