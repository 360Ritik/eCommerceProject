package com.example.ecommerceProject.model.user;

import com.example.ecommerceProject.model.cart.Cart;
import com.example.ecommerceProject.model.cart.CustomerOrder;
import com.example.ecommerceProject.model.product.ProductReview;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {

    private Long contact;

    @OneToMany(mappedBy = "customer")
    private List<ProductReview> productReview;

    @OneToMany(mappedBy = "customer")
    private List<Cart> cart;

    @OneToMany(mappedBy = "customer")
    private List<CustomerOrder> customerOrders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return getId() != null && Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
