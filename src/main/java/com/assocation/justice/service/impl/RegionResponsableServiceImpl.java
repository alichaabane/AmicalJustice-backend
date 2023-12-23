package com.assocation.justice.service.impl;

import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.RegionResponsableDTO2;
import com.assocation.justice.dto.ResponsableDTO;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.entity.Responsable;
import com.assocation.justice.repository.RegionResponsableRepository;
import com.assocation.justice.service.RegionResponsableService;
import com.assocation.justice.util.enumeration.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionResponsableServiceImpl implements RegionResponsableService {

    private final Logger logger = LoggerFactory.getLogger(RegionResponsableServiceImpl.class);
    private final RegionResponsableRepository regionResponsableRepository;

    @Autowired
    public RegionResponsableServiceImpl(RegionResponsableRepository regionResponsableRepository) {
        this.regionResponsableRepository = regionResponsableRepository;
    }

    @Override
    public List<RegionResponsableDTO2> getAllRegionResponsables() {
        logger.info("Fetching all region responsables.");
        List<RegionResponsable> regionResponsables = regionResponsableRepository.findAll();
        List<RegionResponsableDTO2> result = regionResponsables.stream()
                .map(this::mapToRegionResponsableDTO2)
                .collect(Collectors.toList());
        logger.info("Fetched all region responsables successfully.");
        return result;
    }

    @Override
    public PageRequestData<RegionResponsableDTO2> getAllRegionResponsablesPaginated(PageRequest pageRequest) {
        Page<RegionResponsable> regionResponsablePage = regionResponsableRepository.findAll(pageRequest);
        PageRequestData<RegionResponsableDTO2> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(regionResponsablePage.map(this::mapToRegionResponsableDTO2).getContent());
        customPageResponse.setTotalPages(regionResponsablePage.getTotalPages());
        customPageResponse.setTotalElements(regionResponsablePage.getTotalElements());
        customPageResponse.setNumber(regionResponsablePage.getNumber());
        customPageResponse.setSize(regionResponsablePage.getSize());
        logger.info("Fetching All region responsables of Page N° " + pageRequest.getPageNumber());
        return customPageResponse;
    }

    @Override
    public RegionResponsableDTO2 getRegionResponsableById(Long id) {
        logger.info("Fetching region responsable by id: {}", id);
        RegionResponsable regionResponsable = regionResponsableRepository.findById(id).orElse(null);
        RegionResponsableDTO2 result = (regionResponsable != null) ? mapToRegionResponsableDTO2(regionResponsable) : null;
        logger.info("Fetched region responsable successfully.");
        return result;
    }

    @Override
    public RegionResponsableDTO2 createRegionResponsable(RegionResponsableDTO2 regionResponsableDTO) {
        logger.info("Creating region responsable.");
        RegionResponsable regionResponsable = mapToRegionResponsableEntity2(regionResponsableDTO);
        regionResponsable = regionResponsableRepository.save(regionResponsable);
        RegionResponsableDTO2 result = mapToRegionResponsableDTO2(regionResponsable);
        logger.info("Created region responsable successfully.");
        return result;
    }

    @Override
    public RegionResponsableDTO2 updateRegionResponsable(Long id, RegionResponsableDTO2 regionResponsableDTO) {
        logger.info("Updating region responsable with id: {}", id);
        RegionResponsable existingRegionResponsable = regionResponsableRepository.findById(id).orElse(null);
        RegionResponsableDTO2 result;
        if (existingRegionResponsable != null) {
            // Update the existing RegionResponsable with data from the DTO
            existingRegionResponsable.setNom(regionResponsableDTO.getNom());
            existingRegionResponsable.setFax(regionResponsableDTO.getFax());
            existingRegionResponsable.setLongitude(regionResponsableDTO.getLongitude());
            existingRegionResponsable.setLatitude(regionResponsableDTO.getLatitude());
            existingRegionResponsable.setRegion(regionResponsableDTO.getRegion());
            existingRegionResponsable = regionResponsableRepository.save(existingRegionResponsable);
            result = mapToRegionResponsableDTO2(existingRegionResponsable);
            logger.info("Updated region responsable successfully.");
        } else {
            result = null; // RegionResponsable with the specified id not found
            logger.error("Failed to update region responsable. RegionResponsable with id {} not found.", id);
        }
        return result;
    }

    @Override
    public void deleteRegionResponsable(Long id) {
        logger.info("Deleting region responsable with id: {}", id);
        regionResponsableRepository.findById(id).ifPresent(regionResponsable -> {
            regionResponsableRepository.delete(regionResponsable);
            logger.info("Deleted region responsable with id: {}", id);
        });
    }

    @Override
    public List<RegionResponsableDTO> getAllRegionResponsableByRegion(String region) {
        Region region1 = Region.valueOf(region);
        List<RegionResponsable> regionResponsables = this.regionResponsableRepository.findAllByRegion(region1);
        if(regionResponsables != null) {
            return regionResponsables.stream().map(this::mapToRegionResponsableDTO).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /* DTO 2 */
    public RegionResponsableDTO2 mapToRegionResponsableDTO2(RegionResponsable regionResponsable) {
        RegionResponsableDTO2 regionResponsableDTO = new RegionResponsableDTO2();
        regionResponsableDTO.setId(regionResponsable.getId());
        regionResponsableDTO.setNom(regionResponsable.getNom());
        regionResponsableDTO.setLongitude(regionResponsable.getLongitude());
        regionResponsableDTO.setLatitude(regionResponsable.getLatitude());
        regionResponsableDTO.setRegion(regionResponsable.getRegion());
        regionResponsableDTO.setFax(regionResponsable.getFax());
        return regionResponsableDTO;
    }

    public RegionResponsable mapToRegionResponsableEntity2(RegionResponsableDTO2 regionResponsableDTO) {
        RegionResponsable regionResponsable = new RegionResponsable();
        regionResponsable.setNom(regionResponsableDTO.getNom());
        regionResponsable.setLongitude(regionResponsableDTO.getLongitude());
        regionResponsable.setLatitude(regionResponsableDTO.getLatitude());
        regionResponsable.setRegion(regionResponsableDTO.getRegion());
        regionResponsable.setFax(regionResponsableDTO.getFax());
        return regionResponsable;
    }

    /* DTO 1 */
    public Responsable mapToResponsableEntity(ResponsableDTO responsableDTO) {
        Responsable responsable = new Responsable();
        responsable.setId(responsableDTO.getId());
        responsable.setNom(responsableDTO.getNom());
        responsable.setTelephone(responsableDTO.getTelephone());
        responsable.setResponsableEmail(responsableDTO.getResponsableEmail());
        responsable.setResponsableJob(responsableDTO.getResponsableJob());
        responsable.setCategoryResponsable(responsableDTO.getCategoryResponsable());
        return responsable;
    }

    public RegionResponsable mapToRegionResponsableEntity(RegionResponsableDTO regionResponsableDTO) {
        RegionResponsable regionResponsable = new RegionResponsable();
        regionResponsable.setNom(regionResponsableDTO.getNom());
        regionResponsable.setLongitude(regionResponsableDTO.getLongitude());
        regionResponsable.setLatitude(regionResponsableDTO.getLatitude());
        regionResponsable.setRegion(regionResponsableDTO.getRegion());
        regionResponsable.setRegion(regionResponsableDTO.getRegion());
        regionResponsable.setFax(regionResponsableDTO.getFax());
        // Map the associated ResponsableDTOs to Responsables
        List<Responsable> responsables = regionResponsableDTO.getResponsables()
                .stream()
                .map(this::mapToResponsableEntity)
                .collect(Collectors.toList());
        regionResponsable.setResponsables(responsables);
        return regionResponsable;
    }

    public RegionResponsableDTO mapToRegionResponsableDTO(RegionResponsable regionResponsable) {
        RegionResponsableDTO regionResponsableDTO = new RegionResponsableDTO();
        regionResponsableDTO.setId(regionResponsable.getId());
        regionResponsableDTO.setNom(regionResponsable.getNom());
        regionResponsableDTO.setLongitude(regionResponsable.getLongitude());
        regionResponsableDTO.setLatitude(regionResponsable.getLatitude());
        regionResponsableDTO.setRegion(regionResponsable.getRegion());
        regionResponsableDTO.setFax(regionResponsable.getFax());

        // Map the associated Responsables to ResponsableDTOs
        List<ResponsableDTO> responsableDTOs = regionResponsable.getResponsables()
                .stream()
                .map(this::mapToResponsableDTO)
                .collect(Collectors.toList());

        regionResponsableDTO.setResponsables(responsableDTOs);

        return regionResponsableDTO;
    }

    public ResponsableDTO mapToResponsableDTO(Responsable responsable) {
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
