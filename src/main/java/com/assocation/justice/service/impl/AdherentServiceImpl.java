package com.assocation.justice.service.impl;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.entity.Adherent;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.entity.Responsable;
import com.assocation.justice.repository.AdherentRepository;
import com.assocation.justice.repository.RegionResponsableRepository;
import com.assocation.justice.service.AdherentService;
import com.assocation.justice.util.enumeration.SituationFamiliale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//TODO
@Service
public class AdherentServiceImpl implements AdherentService {
    private final Logger logger = LoggerFactory.getLogger(AdherentServiceImpl.class);
    private final AdherentRepository adherentRepository;
    private final RegionResponsableRepository regionResponsableRepository;

    @Autowired
    public AdherentServiceImpl(AdherentRepository adherentRepository, RegionResponsableRepository regionResponsableRepository) {
        this.adherentRepository = adherentRepository;
        this.regionResponsableRepository = regionResponsableRepository;
    }


    @Override
    public AdherentDTO createAdherent(AdherentDTO adherentDTO) {
        Adherent adherent = mapToAdherent(adherentDTO);
        // Add logic for saving the adherent to the database
        // Assuming adherentRepository is the repository for Adherent entities
        adherent = adherentRepository.save(adherent);
        return mapToAdherentDTO(adherent);
    }

    @Override
    public AdherentDTO getAdherentById(Integer cin) {
        // Retrieve the Adherent entity by id
        Adherent adherent = adherentRepository.findById(cin).orElse(null);
        if(adherent != null) {
            return mapToAdherentDTO(adherent);
        }
        else {
            return null;
        }
    }

    @Override
    public List<AdherentDTO> getAllAdherents() {
        List<Adherent> adherents = adherentRepository.findAll();
        return adherents.stream().map(this::mapToAdherentDTO).collect(Collectors.toList());
    }

    @Override
    public AdherentDTO updateAdherent(Integer cin, AdherentDTO adherentDTO) {
        Adherent existingAdherent = adherentRepository.findById(cin).orElse(null);
        if (existingAdherent != null) {
            Adherent updatedAdherent = mapToAdherent(adherentDTO);
            updatedAdherent.setCin(existingAdherent.getCin());
            // Add necessary logic for updating the adherent
            updatedAdherent = adherentRepository.save(updatedAdherent);
            return mapToAdherentDTO(updatedAdherent);
        }
        return null; // or handle the case where the adherent doesn't exist
    }

    @Override
    public void deleteAdherent(Integer cin) {
        // Add logic for deleting the adherent by id
        adherentRepository.deleteById(cin);
    }

    @Override
    public List<AdherentDTO> getAdherentsByRegionResponsable(RegionResponsableDTO regionResponsable) {
        List<Adherent> adherents = adherentRepository.findByRegionResponsable(regionResponsable);
        return adherents.stream()
                .map(this::mapToAdherentDTO)
                .collect(Collectors.toList());
    }


    public AdherentDTO mapToAdherentDTO(Adherent adherent) {
        AdherentDTO adherentDTO = new AdherentDTO();
        adherentDTO.setCin(adherent.getCin());
        adherentDTO.setNom(adherent.getNom());
        adherentDTO.setBirthday(adherent.getBirthday().toString());
        adherentDTO.setBirthdayPlace(adherent.getBirthdayPlace());
        adherentDTO.setMatricule(adherent.getMatricule());
        adherentDTO.setNumeroInscription(adherent.getNumeroInscription());
        adherentDTO.setAdherentJob(adherent.getAdherentJob());
        adherentDTO.setRegionResponsibleId(adherent.getRegionResponsable().getId());
        adherentDTO.setSituationFamiliale(adherent.getSituationFamiliale().toString());
        adherentDTO.setChild1EducationLevel(adherent.getChild1EducationLevel());
        adherentDTO.setChild2EducationLevel(adherent.getChild2EducationLevel());
        adherentDTO.setChild3EducationLevel(adherent.getChild3EducationLevel());
        adherentDTO.setChild4EducationLevel(adherent.getChild4EducationLevel());
        adherentDTO.setChild5EducationLevel(adherent.getChild5EducationLevel());
        adherentDTO.setChild6EducationLevel(adherent.getChild6EducationLevel());
        return adherentDTO;
    }

    public Adherent mapToAdherent(AdherentDTO adherentDTO) {
        Adherent adherent = new Adherent();
        adherent.setCin(adherentDTO.getCin());
        adherent.setNom(adherentDTO.getNom());
        adherent.setBirthday(LocalDate.parse(adherentDTO.getBirthday()));
        adherent.setBirthdayPlace(adherentDTO.getBirthdayPlace());
        adherent.setMatricule(adherentDTO.getMatricule());
        adherent.setNumeroInscription(adherentDTO.getNumeroInscription());
        adherent.setAdherentJob(adherentDTO.getAdherentJob());

        // RegionResponsable setup
        RegionResponsable regionResponsable = new RegionResponsable();
        regionResponsable.setId(adherentDTO.getRegionResponsibleId());
        adherent.setRegionResponsable(regionResponsable);

        // SituationFamiliale setup
        switch (adherentDTO.getSituationFamiliale()) {
            case "أعزب":
                adherent.setSituationFamiliale(SituationFamiliale.أعزب);
                break;
            case "متزوج":
                adherent.setSituationFamiliale(SituationFamiliale.متزوج);
                break;
            case "أرمل":
                adherent.setSituationFamiliale(SituationFamiliale.أرمل);
                break;
            case "مطلق":
                adherent.setSituationFamiliale(SituationFamiliale.مطلق);
                break;
            default:
                // Handle the default case as needed
                break;
        }

        adherent.setChild1EducationLevel(adherentDTO.getChild1EducationLevel());
        adherent.setChild2EducationLevel(adherentDTO.getChild2EducationLevel());
        adherent.setChild3EducationLevel(adherentDTO.getChild3EducationLevel());
        adherent.setChild4EducationLevel(adherentDTO.getChild4EducationLevel());
        adherent.setChild5EducationLevel(adherentDTO.getChild5EducationLevel());
        adherent.setChild6EducationLevel(adherentDTO.getChild6EducationLevel());

        return adherent;
    }

}