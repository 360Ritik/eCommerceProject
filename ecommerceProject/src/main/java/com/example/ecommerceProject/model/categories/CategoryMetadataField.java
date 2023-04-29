package com.example.ecommerceProject.model.categories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CategoryMetadataField {
    String name;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
    @SequenceGenerator(name = "category_generator", sequenceName = "category_field_seq", allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "categoryMetadataField")
    private List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues;
}
