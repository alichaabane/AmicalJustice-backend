package com.assocation.justice.service;

import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.dto.PageRequestData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface NationaleResponsableService {
    NationaleResponsableDTO createNationaleResponsable(NationaleResponsableDTO NationaleResponsableDTO);
    NationaleResponsableDTO getNationaleResponsableById(Long id);
    PageRequestData<NationaleResponsableDTO> getAllNationaleResponsables(PageRequest pageRequest);
    NationaleResponsableDTO updateNationaleResponsable(Long id, NationaleResponsableDTO NationaleResponsableDTO);
    void deleteNationaleResponsable(Long id);
}
