package com.assocation.justice.service;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.ResponsableDTO;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AdherentService {
    AdherentDTO createAdherent(AdherentDTO adherentDTO);
    AdherentDTO getAdherentById(Integer cin);
    PageRequestData<AdherentDTO> getAllAdherentsByPage(PageRequest pageRequest);
    List<AdherentDTO> getAllAdherents();
    public AdherentDTO updateAdherent(Integer cin, AdherentDTO adherentDTO);
    void deleteAdherent(Integer cin);
    List<AdherentDTO> getAdherentsByRegionResponsable(Long regionResponsableId);
    Workbook exportAdherentListToExcel(List<AdherentDTO> adherents);
}
