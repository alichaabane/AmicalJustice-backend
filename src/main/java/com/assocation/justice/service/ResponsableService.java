package com.assocation.justice.service;

import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.ResponsableDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ResponsableService {
    ResponsableDTO createResponsable(ResponsableDTO responsableDTO);
    ResponsableDTO getResponsableById(Long id);
    List<ResponsableDTO> getAllResponsables();
    PageRequestData<ResponsableDTO> getAllResponsablesPaginated(PageRequest pageRequest);
    ResponsableDTO updateResponsable(Long id, ResponsableDTO responsableDTO);
    void deleteResponsable(Long id);
    List<ResponsableDTO> getResponsablesByRegionResponsable(RegionResponsableDTO regionResponsable);
}
