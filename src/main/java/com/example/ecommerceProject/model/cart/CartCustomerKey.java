package com.example.ecommerceProject.model.cart;

import jakarta.persistence.Embeddable;

import java.io.Serializable;


@Embeddable
public class CartCustomerKey implements Serializable {
    private Long customer;
    private Long productVariation;
}
