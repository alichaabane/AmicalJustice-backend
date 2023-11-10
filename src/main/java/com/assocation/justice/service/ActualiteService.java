package com.assocation.justice.service;

import com.assocation.justice.dto.ActualiteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ActualiteService {
    ActualiteDTO saveImage(MultipartFile file, ActualiteDTO newImageDTO);
    ActualiteDTO getActualiteById(Long id);

    List<ActualiteDTO> getAllImages();

    ActualiteDTO updateImage(Long imageId, MultipartFile file, ActualiteDTO updatedImageDTO);

    ResponseEntity<ActualiteDTO> changeImageVisibleState(Long id);

    boolean deleteImage(Long id);
}