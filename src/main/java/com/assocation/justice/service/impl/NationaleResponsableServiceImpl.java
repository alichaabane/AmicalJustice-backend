package com.assocation.justice.service.impl;

import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.entity.NationaleResponsable;
import com.assocation.justice.repository.NationaleResponsableRepository;
import com.assocation.justice.service.NationaleResponsableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
            List<NationaleResponsableDTO> nationaleResponsableDTOS = nationaleResponsables.stream()
                    .map(this::mapToDTO)
                    .sorted(Comparator.comparing(NationaleResponsableDTO::getNom)).collect(Collectors.toList());
            logger.info("Nationale Responsables list fetched successfully");
            return nationaleResponsableDTOS;
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
            }
            else {
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
