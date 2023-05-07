package com.example.ecommerceProject.model.cart;

import com.example.ecommerceProject.model.user.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_generator")
    @SequenceGenerator(name = "customer_generator", sequenceName = "customer_product", allocationSize = 1)
    private Long id;
    private Double amountPaid;
    private Date dateCreated;
    private String customerAddress;
    private String AddressCountry;
    private String addressLine;
    private Integer zipcode;
    private String addressLabel;
    @ManyToOne
    private Customer customer;


}
