package com.assocation.justice.entity;

import com.assocation.justice.util.enumeration.SituationFamiliale;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

    // Constructors, getters, and setters
}
