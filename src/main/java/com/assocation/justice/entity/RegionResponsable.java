package com.assocation.justice.entity;

import com.assocation.justice.util.enumeration.Region;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Order(2) // Specify the order for table creation
public class RegionResponsable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nom;

    @Enumerated(EnumType.STRING) // Use EnumType.STRING for the primary key
    private Region region; // Enum field for responsable region

    @Column
    private String longitude;

    @Column
    private String fax;

    @Column
    private String latitude;

    @OneToMany(mappedBy = "regionResponsable", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Responsable> responsables = new ArrayList<>();

}