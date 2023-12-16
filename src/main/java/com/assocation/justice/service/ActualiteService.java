package com.assocation.justice.service;

import com.assocation.justice.dto.ActualiteDTO;
import com.assocation.justice.dto.PageRequestData;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ActualiteService {
    ActualiteDTO saveImage(MultipartFile file, ActualiteDTO newImageDTO);
    ActualiteDTO getActualiteById(Long id);

    List<ActualiteDTO> getAllImages();
    PageRequestData<ActualiteDTO> getAllImagesPaginated(PageRequest pageRequest);

    ActualiteDTO updateImage(Long imageId, MultipartFile file, ActualiteDTO updatedImageDTO);

    ResponseEntity<ActualiteDTO> changeImageVisibleState(Long id);

    boolean deleteImage(Long id);
}
