package com.assocation.justice.service;

import com.assocation.justice.dto.RechercheDTO;
import com.assocation.justice.dto.PageRequestData;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface RechercheService {
    RechercheDTO saveRecherche(MultipartFile file, RechercheDTO newRechercheDTO);

    RechercheDTO getRechercheById(Long id);

    PageRequestData<RechercheDTO> getAllRecherches(PageRequest pageRequest);

    RechercheDTO updateRecherche(Long conferenceId, MultipartFile file, RechercheDTO updatedRechercheDTO);

    ResponseEntity<RechercheDTO> changeRechercheVisibleState(Long id);

    boolean deleteRecherche(Long id);
}