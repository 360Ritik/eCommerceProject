package com.example.ecommerceProject.model.cart;


import com.example.ecommerceProject.model.product.ProductVariation;
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
public class Cart {

    @EmbeddedId
    CartCustomerKey cartCustomerKey;
    @ManyToOne
    @MapsId("customer")
    private Customer customer;

    @ManyToOne
    @MapsId("productVariation")
    private ProductVariation productVariation;
    private Integer quantity;
    private Boolean isWishListItem;
}
