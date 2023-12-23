package com.assocation.justice.service.impl;

import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.ResponsableDTO;
import com.assocation.justice.entity.NationaleResponsable;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.entity.Responsable;
import com.assocation.justice.repository.RegionResponsableRepository;
import com.assocation.justice.repository.ResponsableRepository;
import com.assocation.justice.service.ResponsableService;
import com.assocation.justice.util.enumeration.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponsableServiceImpl implements ResponsableService {
    private final Logger logger = LoggerFactory.getLogger(ResponsableServiceImpl.class);
    private final ResponsableRepository responsableRepository;
    private final RegionResponsableRepository regionResponsableRepository;

    @Autowired
    public ResponsableServiceImpl(ResponsableRepository responsableRepository, RegionResponsableRepository regionResponsableRepository) {
        this.responsableRepository = responsableRepository;
        this.regionResponsableRepository = regionResponsableRepository;
    }

    @Override
    public ResponsableDTO createResponsable(ResponsableDTO responsableDTO) {
        // Retrieve the RegionResponsable entity
        Region regionResponsableCity = responsableDTO.getRegionResponsableCity();
        RegionResponsable regionResponsable = regionResponsableRepository.findByRegion(regionResponsableCity);

        if (regionResponsable != null) {
            // Create the Responsable entity
            Responsable responsable = mapToEntity(responsableDTO, regionResponsable);
            responsable = responsableRepository.save(responsable);
            return mapToDTO(responsable);
        } else {
            // Handle the case when the specified RegionResponsable doesn't exist
            // You can throw an exception or handle it as needed.
            return null;
        }
    }

    @Override
    public ResponsableDTO getResponsableById(Long id) {
        Responsable responsable = responsableRepository.findById(id).orElse(null);
        return responsable != null ? mapToDTO(responsable) : null;
    }

    @Override
    public List<ResponsableDTO> getAllResponsables() {
        List<Responsable> responsables = responsableRepository.findAll();
        List<ResponsableDTO> responsableDTOS = responsables.stream()
                .map(this::mapToDTO)
                .sorted(Comparator.comparing(ResponsableDTO::getRegionResponsableName)).collect(Collectors.toList());
        logger.info("Responsables list fetched successfully");
        return responsableDTOS;
    }

    @Override
    public PageRequestData<ResponsableDTO> getAllResponsablesPaginated(PageRequest pageRequest) {
        Page<Responsable> responsablePage = responsableRepository.findAll(pageRequest);
        PageRequestData<ResponsableDTO> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(responsablePage.map(this::mapToDTO).getContent());
        customPageResponse.setTotalPages(responsablePage.getTotalPages());
        customPageResponse.setTotalElements(responsablePage.getTotalElements());
        customPageResponse.setNumber(responsablePage.getNumber());
        customPageResponse.setSize(responsablePage.getSize());
        logger.info("Fetching All responsables of Page N° " + pageRequest.getPageNumber());
        return customPageResponse;
    }

    public List<ResponsableDTO> getResponsablesByRegionResponsable(RegionResponsableDTO regionResponsable) {
        List<Responsable> responsables = responsableRepository.findByRegionResponsable(regionResponsable);
        return responsables.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ResponsableDTO updateResponsable(Long id, ResponsableDTO responsableDTO) {
        Responsable existingResponsable = responsableRepository.findById(id).orElse(null);
        if (existingResponsable != null) {
            existingResponsable.setNom(responsableDTO.getNom());
            existingResponsable.setTelephone(responsableDTO.getTelephone());
            existingResponsable.setResponsableEmail(responsableDTO.getResponsableEmail());
            existingResponsable.setResponsableJob(responsableDTO.getResponsableJob());
            existingResponsable.setCategoryResponsable(responsableDTO.getCategoryResponsable());

            // Now handle the regionResponsable reference
            String regionResponsableName = responsableDTO.getRegionResponsableName();
            if (regionResponsableName != null) {
                RegionResponsable regionResponsable = regionResponsableRepository.findByNom(regionResponsableName);
                if (regionResponsable != null) {
                    existingResponsable.setRegionResponsable(regionResponsable);
                }  // Handle the case when the specified RegionResponsable doesn't exist
                // You can throw an exception or handle it as needed.

            }
            existingResponsable = responsableRepository.save(existingResponsable);

            return mapToDTO(existingResponsable);
        } else {
            return null;
        }
    }

    @Override
    public void deleteResponsable(Long id) {
        responsableRepository.deleteById(id);
    }

    public Responsable mapToEntity(ResponsableDTO responsableDTO, RegionResponsable regionResponsable) {
        Responsable responsable = new Responsable();
        responsable.setId(responsableDTO.getId());
        responsable.setNom(responsableDTO.getNom());
        responsable.setTelephone(responsableDTO.getTelephone());
        responsable.setResponsableEmail(responsableDTO.getResponsableEmail());
        responsable.setResponsableJob(responsableDTO.getResponsableJob());
        responsable.setCategoryResponsable(responsableDTO.getCategoryResponsable());
        responsable.setRegionResponsable(regionResponsable); // Set the RegionResponsable reference

        return responsable;
    }

    public ResponsableDTO mapToDTO(Responsable responsable) {
        ResponsableDTO responsableDTO = new ResponsableDTO();
        responsableDTO.setId(responsable.getId());
        responsableDTO.setNom(responsable.getNom());
        responsableDTO.setTelephone(responsable.getTelephone());
        responsableDTO.setResponsableEmail(responsable.getResponsableEmail());
        responsableDTO.setResponsableJob(responsable.getResponsableJob());
        responsableDTO.setCategoryResponsable(responsable.getCategoryResponsable());
        responsableDTO.setRegionResponsableCity(responsable.getRegionResponsable().getRegion());
        responsableDTO.setRegionResponsableName(responsable.getRegionResponsable().getNom());

        return responsableDTO;
    }

}
