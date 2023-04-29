package com.example.ecommerceProject.model.product;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ProductCompositeKey implements Serializable {

    private Long product;
    private Long customer;
}
