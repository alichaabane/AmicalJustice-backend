package com.assocation.justice.service;

import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.RegionResponsableDTO2;
import com.assocation.justice.dto.ResponsableDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RegionResponsableService {
    RegionResponsableDTO2 createRegionResponsable(RegionResponsableDTO2 regionResponsableDTO);
    RegionResponsableDTO2 getRegionResponsableById(Long id);
    List<RegionResponsableDTO> getAllRegionResponsableByRegion(String region);
    RegionResponsableDTO2 updateRegionResponsable(Long id, RegionResponsableDTO2 regionResponsableDTO);
    void deleteRegionResponsable(Long id);
    PageRequestData<RegionResponsableDTO2> getAllRegionResponsablesPaginated(PageRequest pageRequest);
    List<RegionResponsableDTO2> getAllRegionResponsables();
}
