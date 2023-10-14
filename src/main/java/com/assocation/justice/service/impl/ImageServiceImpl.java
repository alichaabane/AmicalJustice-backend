package com.assocation.justice.service.impl;

import com.assocation.justice.dto.ImageDTO;
import com.assocation.justice.entity.Image;
import com.assocation.justice.repository.ImageRepository;
import com.assocation.justice.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    private final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public ImageDTO saveImage(ImageDTO newImageDTO) {
        Image newImage = mapImageDTOToEntity(newImageDTO);
        Image savedImage = imageRepository.save(newImage);
        logger.info("Image created successfully");
        return mapImageToDTO(savedImage);
    }

    @Override
    public ImageDTO getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with ID: " + id));
        logger.info("the image fetched successfully");
        return mapImageToDTO(image);
    }

    @Override
    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        List<ImageDTO> imageDTOs = images.stream()
                .map(this::mapImageToDTO)
                .collect(Collectors.toList());
        logger.info("Image list fetched successfully");
        return imageDTOs;
    }

    @Override
    @Transactional
    public boolean deleteImage(Long id) {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            imageRepository.delete(imageOptional.get());
            logger.info("Image deleted successfully");
            return true; // Deletion was successful
        }
        logger.error("Error in deleting image");
        return false; // Image with the given ID was not found
    }

    public ResponseEntity<ImageDTO> changeImageVisibleState(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(IllegalArgumentException::new);
        image.setActive(!image.isActive());
        // Save the updated image back to the repository
        image = imageRepository.save(image);
        logger.info("Image N° " + imageId + " change its visibility");
        // Return a ResponseEntity with the updated image and an HTTP status code
        return ResponseEntity.ok(mapImageToDTO(image));
    }

    public Image mapImageDTOToEntity(ImageDTO imageDTO) {
        Image image = new Image();
        image.setId(imageDTO.getId());
        image.setActive(imageDTO.isActive());
        image.setTitle(imageDTO.getTitle());
        image.setText(imageDTO.getText());
        image.setName(imageDTO.getName());
        return image;
    }


    public ImageDTO mapImageToDTO(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setActive(image.isActive());
        dto.setText(image.getText());
        dto.setTitle(image.getTitle());
        dto.setName(image.getName());
        return dto;
    }
}
