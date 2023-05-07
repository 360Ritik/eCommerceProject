package com.example.ecommerceProject.model.user;


import com.example.ecommerceProject.model.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User {

    private String gst;
    private Long companyContact;
    private String companyName;

    @OneToMany(mappedBy = "seller")
    private List<Product> products;


}
