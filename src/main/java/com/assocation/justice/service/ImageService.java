package com.assocation.justice.service;

import com.assocation.justice.dto.ImageDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ImageService {
    ImageDTO saveImage(ImageDTO imageDTO);

    ImageDTO getImageById(Long id);

    List<ImageDTO> getAllImages();

    ResponseEntity<ImageDTO> changeImageVisibleState(Long id);

    boolean deleteImage(Long id);
}