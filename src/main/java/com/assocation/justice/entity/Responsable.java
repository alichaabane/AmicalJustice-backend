package com.assocation.justice.entity;

import com.assocation.justice.util.enumeration.CategoryResponsable;
import com.assocation.justice.util.enumeration.Region;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Order(3) // Specify the order for table creation
public class Responsable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nom; // Name of the responsable
    @Column
    private String telephone; // Telephone number of the responsable
    @Column
    private String responsableEmail; // Email of the responsable
    @Column
    private String responsableJob; // Job title of the responsable


    @Enumerated(EnumType.STRING)
    private CategoryResponsable categoryResponsable; // Enum field for responsable category


    @ManyToOne// Many Responsables can be associated with one RegionResponsable
    @JoinColumn(name = "region_responsable_id", nullable = false)
    private RegionResponsable regionResponsable;

}

