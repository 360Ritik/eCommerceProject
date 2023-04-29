package com.example.ecommerceProject.model.order;

import com.example.ecommerceProject.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderStatus {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_generator")
    @SequenceGenerator(name = "status_generator", sequenceName = "status_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @ManyToOne
    private OrderProduct orderProduct;

    private String transitionComments;
    private Date transitionDate;

    @Enumerated(EnumType.STRING)
    private Status fromStatus;

    @Enumerated(EnumType.STRING)
    private Status toStatus;


}
