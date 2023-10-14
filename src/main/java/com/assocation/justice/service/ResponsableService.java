package com.assocation.justice.service;

import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.ResponsableDTO;

import java.util.List;

public interface ResponsableService {
    ResponsableDTO createResponsable(ResponsableDTO responsableDTO);
    ResponsableDTO getResponsableById(Long id);
    List<ResponsableDTO> getAllResponsables();
    ResponsableDTO updateResponsable(Long id, ResponsableDTO responsableDTO);
    void deleteResponsable(Long id);
    List<ResponsableDTO> getResponsablesByRegionResponsable(RegionResponsableDTO regionResponsable);
}
