package com.example.ecommerceProject.model.product;

import com.example.ecommerceProject.auditing.Auditable;
import com.example.ecommerceProject.model.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ProductVariation extends Auditable<String> {

    @ManyToOne
    Product product;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product1_generator")
    @SequenceGenerator(name = "product1_generator", sequenceName = "product_seq", allocationSize = 1)
    private Long id;
    private Integer quantityAvailable;
    private Double price;
    private Boolean isActive;
    @OneToMany(mappedBy = "productVariation")
    private List<Cart> carts;

    @Column(columnDefinition = "json")
    private String metaData;
}
