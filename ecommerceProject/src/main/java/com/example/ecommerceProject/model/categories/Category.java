package com.example.ecommerceProject.model.categories;

import com.example.ecommerceProject.model.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @ManyToOne
    private Category parent_id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_gen")
    @SequenceGenerator(name = "category_gen", sequenceName = "category_sequence", allocationSize = 1)
    private Long id;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<CategoryMetaDataFieldValues> categoryMetaDataFieldValue;

    @OneToMany(mappedBy = "category")
    private List<Product> products;


}
