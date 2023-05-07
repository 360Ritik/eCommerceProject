package com.example.ecommerceProject.model.product;

import com.example.ecommerceProject.model.user.Customer;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductReview {

    @EmbeddedId
    ProductCompositeKey productCompositeKey;
    private String productReview;
    private Integer rating;
    @ManyToOne
    @MapsId("product")
    private Product product;
    @ManyToOne
    @MapsId("customer")
    private Customer customer;

}
