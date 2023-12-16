package com.assocation.justice.service.impl;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.entity.Adherent;
import com.assocation.justice.entity.NationaleResponsable;
import com.assocation.justice.repository.NationaleResponsableRepository;
import com.assocation.justice.service.NationaleResponsableService;
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
public class NationaleResponsableServiceImpl implements NationaleResponsableService {
    private final Logger logger = LoggerFactory.getLogger(NationaleResponsableServiceImpl.class);
    private final NationaleResponsableRepository nationaleResponsableRepository;

    @Autowired
    public NationaleResponsableServiceImpl(NationaleResponsableRepository nationaleResponsableRepository) {
        this.nationaleResponsableRepository = nationaleResponsableRepository;
    }

    @Override
    public NationaleResponsableDTO createNationaleResponsable(NationaleResponsableDTO nationaleResponsableDTO) {
        // Retrieve the RegionResponsable entity
        // Create the Responsable entity
        NationaleResponsable nationaleResponsable = mapToEntity(nationaleResponsableDTO);
        nationaleResponsable = nationaleResponsableRepository.save(nationaleResponsable);
        return mapToDTO(nationaleResponsable);
    }

    @Override
    public NationaleResponsableDTO getNationaleResponsableById(Long id) {
        NationaleResponsable nationaleResponsable = nationaleResponsableRepository.findById(id).orElse(null);
        return nationaleResponsable != null ? mapToDTO(nationaleResponsable) : null;
    }

    @Override
    public List<NationaleResponsableDTO> getAllNationaleResponsables() {
        List<NationaleResponsable> nationaleResponsables = nationaleResponsableRepository.findAll();
        logger.info("Fetching All nationale responsables successfully");
        return nationaleResponsables.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PageRequestData<NationaleResponsableDTO> getAllNationaleResponsablesPaginated(PageRequest pageRequest) {
        Page<NationaleResponsable> nationaleResponsablePage = nationaleResponsableRepository.findAll(pageRequest);
        PageRequestData<NationaleResponsableDTO> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(nationaleResponsablePage.map(this::mapToDTO).getContent());
        customPageResponse.setTotalPages(nationaleResponsablePage.getTotalPages());
        customPageResponse.setTotalElements(nationaleResponsablePage.getTotalElements());
        customPageResponse.setNumber(nationaleResponsablePage.getNumber());
        customPageResponse.setSize(nationaleResponsablePage.getSize());
        logger.info("Fetching All nationale responsables of Page N° " + pageRequest.getPageNumber());
        return customPageResponse;
    }


    @Override
    public NationaleResponsableDTO updateNationaleResponsable(Long id, NationaleResponsableDTO responsableDTO) {
        NationaleResponsable existingResponsable = nationaleResponsableRepository.findById(id).orElse(null);
        if (existingResponsable != null) {
            existingResponsable.setNom(responsableDTO.getNom());
            existingResponsable.setTelephone(responsableDTO.getTelephone());
            existingResponsable.setNationaleResponsableEmail(responsableDTO.getNationaleResponsableEmail());
            existingResponsable.setNationaleResponsableJob(responsableDTO.getNationaleResponsableJob());
            existingResponsable.setCategoryNationaleResponsable(responsableDTO.getCategoryNationaleResponsable());
            existingResponsable = nationaleResponsableRepository.save(existingResponsable);
            return mapToDTO(existingResponsable);
        } else {
            return null;
        }
    }

    @Override
    public void deleteNationaleResponsable(Long id) {
        nationaleResponsableRepository.deleteById(id);
    }

    public NationaleResponsable mapToEntity(NationaleResponsableDTO responsableDTO) {
        NationaleResponsable nationaleResponsable = new NationaleResponsable();
        nationaleResponsable.setId(responsableDTO.getId());
        nationaleResponsable.setNom(responsableDTO.getNom());
        nationaleResponsable.setTelephone(responsableDTO.getTelephone());
        nationaleResponsable.setNationaleResponsableEmail(responsableDTO.getNationaleResponsableEmail());
        nationaleResponsable.setNationaleResponsableJob(responsableDTO.getNationaleResponsableJob());
        nationaleResponsable.setCategoryNationaleResponsable(responsableDTO.getCategoryNationaleResponsable());
        return nationaleResponsable;
    }

    public NationaleResponsableDTO mapToDTO(NationaleResponsable nationaleResponsable) {
        NationaleResponsableDTO nationaleResponsableDTO = new NationaleResponsableDTO();
        nationaleResponsableDTO.setId(nationaleResponsable.getId());
        nationaleResponsableDTO.setNom(nationaleResponsable.getNom());
        nationaleResponsableDTO.setTelephone(nationaleResponsable.getTelephone());
        nationaleResponsableDTO.setNationaleResponsableEmail(nationaleResponsable.getNationaleResponsableEmail());
        nationaleResponsableDTO.setNationaleResponsableJob(nationaleResponsable.getNationaleResponsableJob());
        nationaleResponsableDTO.setCategoryNationaleResponsable(nationaleResponsable.getCategoryNationaleResponsable());
        return nationaleResponsableDTO;
    }

}
