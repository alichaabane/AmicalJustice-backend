package com.assocation.justice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nom; // Name of the sender
    @Column
    private String telephone; // Telephone number of the sender
    @Column
    private String email; // Email of the sender
    @Column(columnDefinition = "TEXT")
    private String message; // Message from the sender

    @ManyToOne// Many sender can be associated with one RegionResponsable
    @JoinColumn(name = "region_responsable_id", nullable = false)
    private RegionResponsable regionResponsable;

}
