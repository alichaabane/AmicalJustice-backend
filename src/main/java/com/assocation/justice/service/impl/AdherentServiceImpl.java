package com.assocation.justice.service.impl;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.repository.AdherentRepository;
import com.assocation.justice.repository.RegionResponsableRepository;
import com.assocation.justice.service.AdherentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return null;
    }

    @Override
    public AdherentDTO getAdherentById(Long id) {
        return null;
    }

    @Override
    public List<AdherentDTO> getAllAdherents() {
        return null;
    }

    @Override
    public AdherentDTO updateAdherent(Long id, AdherentDTO adherentDTO) {
        return null;
    }

    @Override
    public void deleteAdherent(Long id) {

    }

    @Override
    public List<AdherentDTO> getAdherentsByRegionResponsable(RegionResponsableDTO regionResponsable) {
        return null;
    }
}