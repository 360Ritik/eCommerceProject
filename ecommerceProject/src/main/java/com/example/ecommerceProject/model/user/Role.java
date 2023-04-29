package com.example.ecommerceProject.model.user;


import com.example.ecommerceProject.enums.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
    @SequenceGenerator(name = "role_generator", sequenceName = "role_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private List<User> users;


}
