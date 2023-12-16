package com.assocation.justice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdherentDTO {
    private Integer cin;
    private String nom;
    private String birthday;
    private String birthdayPlace;
    private String matricule;
    private String numeroInscription;
    private String adherentJob;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long regionResponsibleId;
    private String situationFamiliale;
    private String child1EducationLevel;
    private String child2EducationLevel;
    private String child3EducationLevel;
    private String child4EducationLevel;
    private String child5EducationLevel;
    private String child6EducationLevel;
}
