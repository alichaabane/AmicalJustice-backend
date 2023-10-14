package com.assocation.justice.resource;

import com.assocation.justice.dto.ImageDTO;
import com.assocation.justice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/images")
public class ImageResource {
    private final ImageService imageService;

    @Autowired
    public ImageResource(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id) {
        ImageDTO imageDTO = imageService.getImageById(id);
        if (imageDTO != null) {
            return ResponseEntity.ok(imageDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        List<ImageDTO> imageDTOs = imageService.getAllImages();
        return ResponseEntity.ok(imageDTOs);
    }

    @PostMapping("")
    public ResponseEntity<ImageDTO> createImage(@RequestBody ImageDTO imageDTO) {
        ImageDTO createdImageDTO = imageService.saveImage(imageDTO);
        return ResponseEntity.ok(createdImageDTO);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<ImageDTO> toggleVisibleState(@PathVariable Long id) {
        ResponseEntity<ImageDTO> responseEntity = imageService.changeImageVisibleState(id);
        if (responseEntity != null) {
            return responseEntity; // Return the response from changeImageVisibleState
        } else {
            return ResponseEntity.notFound().build(); // Return a 404 response if changeImageVisibleState returns null
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@PathVariable Long id, @RequestBody ImageDTO imageDTO) {
        // Fetch the existing Image entity by ID
        ImageDTO existingImage = imageService.getImageById(id);

        if (existingImage != null) {
            // Update the existingImage with data from the imageDTO
            existingImage.setName(imageDTO.getName()); // Replace with the actual field names
            existingImage.setText(imageDTO.getText());
            existingImage.setTitle(imageDTO.getTitle());
            existingImage.setActive(imageDTO.isActive());
            // Update other fields as needed

            // Save the updated Image entity back to the repository
            ImageDTO updatedImage = imageService.saveImage(existingImage);

            if (updatedImage != null) {
                return ResponseEntity.ok(updatedImage); // Return the updated DTO
            } else {
                return ResponseEntity.badRequest().build(); // Or another appropriate HTTP status code
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        boolean deleted = imageService.deleteImage(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204 - No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 - Not Found
        }
    }

}
