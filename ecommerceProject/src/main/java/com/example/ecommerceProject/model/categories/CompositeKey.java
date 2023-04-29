package com.example.ecommerceProject.model.categories;

import jakarta.persistence.Embeddable;

import java.io.Serializable;


@Embeddable
public class CompositeKey implements Serializable {
    private Long categoryId;
    private Long categoryMetadataFieldId;
}
