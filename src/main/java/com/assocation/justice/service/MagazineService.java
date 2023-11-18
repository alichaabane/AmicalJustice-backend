package com.assocation.justice.service;

import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.MagazineDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface MagazineService {
    MagazineDTO saveMagazine(MultipartFile file, MagazineDTO newMagazineDTO);

    MagazineDTO getMagazineById(Long id);

    PageRequestData<MagazineDTO> getAllMagazines(PageRequest pageRequest);

    MagazineDTO updateMagazine(Long conferenceId, MultipartFile file, MagazineDTO updatedMagazineDTO);

    ResponseEntity<MagazineDTO> changeMagazineVisibleState(Long id);

    boolean deleteMagazine(Long id);
}