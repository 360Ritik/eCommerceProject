package com.example.ecommerceProject.model.user;

import com.example.ecommerceProject.auditing.Auditable;
import com.example.ecommerceProject.model.tokenStore.JwtToken;
import com.example.ecommerceProject.model.tokenStore.RegisterToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable<String> {


    @OneToOne(mappedBy = "user")
    RegisterToken registerToken;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private String email;
    private Boolean isDeleted = false;
    private Boolean isActive = false;
    private Boolean isLocked = false;
    private LocalDateTime passwordUpdateDate;
    private Integer invalidAttemptCount = 3;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToOne(mappedBy = "user")
    private JwtToken jwtToken;

    @OneToOne(mappedBy = "user")
    private JwtToken loginToken;

}