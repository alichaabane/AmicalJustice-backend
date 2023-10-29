package com.assocation.justice.service;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.ResponsableDTO;

import java.util.List;

public interface AdherentService {
    AdherentDTO createAdherent(AdherentDTO adherentDTO);
    AdherentDTO getAdherentById(Long id);
    List<AdherentDTO> getAllAdherents();
    AdherentDTO updateAdherent(Long id, AdherentDTO adherentDTO);
    void deleteAdherent(Long id);
    List<AdherentDTO> getAdherentsByRegionResponsable(RegionResponsableDTO regionResponsable);
}
