package com.example.ecommerceProject.model.categories;

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
public class CategoryMetaDataFieldValues {

    private String separatedValues;

    @EmbeddedId
    private CompositeKey compositeKey;

    @ManyToOne
    @MapsId("categoryId")
    private Category category;
    @ManyToOne
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;


}
