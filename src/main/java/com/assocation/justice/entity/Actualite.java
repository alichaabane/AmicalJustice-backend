package com.assocation.justice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Order(3) // Specify the order for table creation
public class Actualite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    private boolean active;

}
