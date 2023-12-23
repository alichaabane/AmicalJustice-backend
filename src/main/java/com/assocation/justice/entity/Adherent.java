package com.assocation.justice.entity;

import com.assocation.justice.util.enumeration.SituationFamiliale;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Adherent {

    @Id
    private Integer cin;

    private String nom;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthday;

    private String birthdayPlace;

    private String matricule;

    private String numeroInscription;

    private String adherentJob;

    @ManyToOne
    @JoinColumn(name = "region_responsable_id", nullable = false)
    private RegionResponsable regionResponsable;

    @Enumerated(EnumType.STRING)
    private SituationFamiliale situationFamiliale;

    private String child1EducationLevel;
    private String child2EducationLevel;
    private String child3EducationLevel;
    private String child4EducationLevel;
    private String child5EducationLevel;
    private String child6EducationLevel;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Constructors, getters, and setters
}
