package com.example.ecommerceProject.model.order;


import com.example.ecommerceProject.auditing.Auditable;
import com.example.ecommerceProject.model.cart.CustomerOrder;
import com.example.ecommerceProject.model.product.ProductVariation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderProduct extends Auditable<String> {

    @OneToMany(mappedBy = "orderProduct")
    private List<OrderStatus> orderStatus;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_gen")
    @SequenceGenerator(name = "order_gen", sequenceName = "orderProductSeq", allocationSize = 1)
    private Long id;
    private Double productQuantity;
    private Integer orderPrice;
    @ManyToOne
    private CustomerOrder order;
    @OneToOne
    private ProductVariation productVariation;
}
