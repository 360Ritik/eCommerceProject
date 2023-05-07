package com.example.ecommerceProject.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_generator")
    @SequenceGenerator(name = "address_generator", sequenceName = "address_seq", allocationSize = 1)
    private Long id;

    private String city;
    private String state;
    private String country;
    private String addressLine;
    private Integer zipCode;
    private String label;

    @ManyToOne(cascade = CascadeType.ALL)
//    @JsonIgnore
    private User user;

}
