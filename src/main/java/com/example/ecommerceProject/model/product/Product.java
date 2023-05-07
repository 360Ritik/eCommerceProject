package com.example.ecommerceProject.model.product;

import com.example.ecommerceProject.auditing.Auditable;
import com.example.ecommerceProject.model.categories.Category;
import com.example.ecommerceProject.model.user.Seller;
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
public class Product extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name = "product_generator", sequenceName = "product_seq", allocationSize = 1)
    private Long id;
    private String productName;
    private String productDescription;
    private String brand;
    @ManyToOne
    private Category category;

    @ManyToOne
    private Seller seller;
    private Boolean isCancellable;
    private Boolean isReturnable;
    private Boolean isActive;
    private Boolean isDeleted;


    @OneToMany(mappedBy = "product")
    private List<ProductReview> productReviews;
}
