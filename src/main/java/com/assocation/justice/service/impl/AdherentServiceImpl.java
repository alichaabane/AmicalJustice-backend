package com.assocation.justice.service.impl;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.entity.Adherent;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.repository.AdherentRepository;
import com.assocation.justice.repository.RegionResponsableRepository;
import com.assocation.justice.service.AdherentService;
import com.assocation.justice.util.enumeration.SituationFamiliale;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        if(adherent != null) {
            adherent = adherentRepository.save(adherent);
            logger.info("Adherent created successfully");
            return mapToAdherentDTO(adherent);
        } else {
            logger.error("Error in creation of adherent");
            return null;
        }
    }

    @Override
    public AdherentDTO getAdherentById(Integer cin) {
        // Retrieve the Adherent entity by id
        Adherent adherent = adherentRepository.findById(cin).orElse(null);
        if(adherent != null) {
            logger.info("Fetching Adherent with CIN = " + adherent.getCin().toString().substring(4));
            return mapToAdherentDTO(adherent);
        }
        else {
            logger.error("Error in fetching Adherent with CIN = " + cin.toString().substring(4));
            return null;
        }
    }

    @Override
    public List<AdherentDTO> getAllAdherents() {
        List<Adherent> adherents = adherentRepository.findAll();
        logger.info("Fetching All Adherents successfully");
        return adherents.stream().map(this::mapToAdherentDTO).collect(Collectors.toList());
    }

    @Override
    public AdherentDTO updateAdherent(Integer cin, AdherentDTO adherentDTO) {
        Adherent existingAdherent = adherentRepository.findById(cin).orElse(null);
        if (existingAdherent != null) {
            logger.info("updating Adherent with CIN = " + cin.toString().substring(4) + " with success");
            Adherent updatedAdherent = mapToAdherent(adherentDTO);
            updatedAdherent.setCin(existingAdherent.getCin());
            // Add necessary logic for updating the adherent
            updatedAdherent = adherentRepository.save(updatedAdherent);
            return mapToAdherentDTO(updatedAdherent);
        }
        logger.info("Error during the update of adherent with CIN = " + cin.toString().substring(4));
        return null; // or handle the case where the adherent doesn't exist
    }

    @Override
    public void deleteAdherent(Integer cin) {
        // Add logic for deleting the adherent by id
        logger.info("Adherent deleted successfully");
        adherentRepository.deleteById(cin);
    }

    @Override
    public List<AdherentDTO> getAdherentsByRegionResponsable(Long regionResponsableId) {
        List<Adherent> adherents = adherentRepository.findAllByRegionResponsableId(regionResponsableId);
        return adherents.stream()
                .map(this::mapToAdherentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Workbook exportAdherentListToExcel(List<AdherentDTO> adherents) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Adherents");

        // Add headers
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("بطاقة التعريف الوطنية");
        headerRow.createCell(1).setCellValue("الأسم و اللقب");
        headerRow.createCell(2).setCellValue("تاريخ الميلاد");
        headerRow.createCell(3).setCellValue("مكان الميلاد");
        headerRow.createCell(4).setCellValue("الرقم الوطني");
        headerRow.createCell(5).setCellValue("رقم التسجيل");
        headerRow.createCell(6).setCellValue("الوظيفة");
        headerRow.createCell(7).setCellValue("المقر المنخرط به ");
        headerRow.createCell(8).setCellValue("الحالة العائلية");
        headerRow.createCell(9).setCellValue("مستوى التعليم للابن الأول"); //TODO
        headerRow.createCell(10).setCellValue("مستوى التعليم للابن الثاني");
        headerRow.createCell(11).setCellValue("مستوى التعليم للابن الثالث");
        headerRow.createCell(12).setCellValue("مستوى التعليم للابن الرابع");
        headerRow.createCell(13).setCellValue("مستوى التعليم للابن الخامس");
        headerRow.createCell(14).setCellValue("مستوى التعليم للابن السادس");
        // Add more headers as needed

        // Add data to the Excel file
        int rowNum = 1;
        for (AdherentDTO adherent : adherents) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(adherent.getCin());
            row.createCell(1).setCellValue(adherent.getNom());
            row.createCell(2).setCellValue(adherent.getBirthday());
            row.createCell(3).setCellValue(adherent.getBirthdayPlace());
            row.createCell(4).setCellValue(adherent.getMatricule());
            row.createCell(5).setCellValue(adherent.getNumeroInscription());
            row.createCell(6).setCellValue(adherent.getAdherentJob());
            row.createCell(7).setCellValue(adherent.getRegionResponsibleId());
            row.createCell(8).setCellValue(adherent.getSituationFamiliale());
            row.createCell(9).setCellValue(adherent.getChild1EducationLevel());
            row.createCell(10).setCellValue(adherent.getChild2EducationLevel());
            row.createCell(11).setCellValue(adherent.getChild3EducationLevel());
            row.createCell(12).setCellValue(adherent.getChild4EducationLevel());
            row.createCell(13).setCellValue(adherent.getChild5EducationLevel());
            row.createCell(14).setCellValue(adherent.getChild6EducationLevel());
            // Add more data fields as needed
        }

        // Adjust column width
        for (int i = 0; i < 15; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
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