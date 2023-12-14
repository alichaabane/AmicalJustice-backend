package com.assocation.justice.entity;

import com.assocation.justice.util.enumeration.CategoryResponsable;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Order(4) // Specify the order for table creation
public class NationaleResponsable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nom; // Name of the responsable
    @Column
    private String telephone; // Telephone number of the responsable
    @Column
    private String nationaleResponsableEmail; // Email of the responsable
    @Column
    private String nationaleResponsableJob; // Job title of the responsable
    @Column
    private String categoryNationaleResponsable; // responsable category
}
