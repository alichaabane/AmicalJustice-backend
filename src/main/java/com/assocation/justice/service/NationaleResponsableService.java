package com.assocation.justice.service;

import com.assocation.justice.dto.NationaleResponsableDTO;

import java.util.List;

public interface NationaleResponsableService {
    NationaleResponsableDTO createNationaleResponsable(NationaleResponsableDTO NationaleResponsableDTO);
    NationaleResponsableDTO getNationaleResponsableById(Long id);
    List<NationaleResponsableDTO> getAllNationaleResponsables();
    NationaleResponsableDTO updateNationaleResponsable(Long id, NationaleResponsableDTO NationaleResponsableDTO);
    void deleteNationaleResponsable(Long id);
}
