package com.assocation.justice.resource;

import com.assocation.justice.dto.ActualiteDTO;
import com.assocation.justice.service.ActualiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/actualites")
public class ActualitesResource {
    private final ActualiteService actualiteService;

    @Autowired
    public ActualitesResource(ActualiteService actualiteService) {
        this.actualiteService = actualiteService;
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<ActualiteDTO> getImageById(@PathVariable Long id) {
        ActualiteDTO actualiteDTO = actualiteService.getActualiteById(id);
        if (actualiteDTO != null) {
            return ResponseEntity.ok(actualiteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ActualiteDTO>> getAllImages() {
        List<ActualiteDTO> imageDTOs = actualiteService.getAllImages();
        return ResponseEntity.ok(imageDTOs);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Resource resource = new ClassPathResource("static/uploads/" + filename);
        String mediaTypeString = determineMediaType(filename);

        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(mediaTypeString) ) // Adjust content type based on your image type
                .body(resource);
    }

    private String determineMediaType(String filename) {
        if (filename.toLowerCase().endsWith(".webp")) {
            return "image/webp";
        } else {
            // Default to JPEG if the format is not recognized
            return "image/jpeg";
        }
    }


    @PostMapping("/upload")
    public ActualiteDTO uploadImage(@RequestParam("file") MultipartFile file,
                                @RequestParam("title") String title,
                                @RequestParam("text") String text,
                                @RequestParam("active") boolean active) {
        ActualiteDTO newImageDTO = new ActualiteDTO();
        newImageDTO.setTitle(title);
        newImageDTO.setText(text);
        newImageDTO.setActive(active);

        return actualiteService.saveImage(file, newImageDTO);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<ActualiteDTO> toggleVisibleState(@PathVariable Long id) {
        ResponseEntity<ActualiteDTO> responseEntity = actualiteService.changeImageVisibleState(id);
        if (responseEntity != null) {
            return responseEntity; // Return the response from changeImageVisibleState
        } else {
            return ResponseEntity.notFound().build(); // Return a 404 response if changeImageVisibleState returns null
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ActualiteDTO> updateImage(@PathVariable Long id,
                                                @RequestParam(value = "file", required = false) MultipartFile file,
                                                @RequestParam("title") String title,
                                                @RequestParam("text") String text,
                                                @RequestParam("active") boolean active) {
        ActualiteDTO updatedImageDTO = new ActualiteDTO();
        updatedImageDTO.setTitle(title);
        updatedImageDTO.setText(text);
        updatedImageDTO.setActive(active);

        ActualiteDTO resultDTO = actualiteService.updateImage(id, file, updatedImageDTO);
        return ResponseEntity.ok(resultDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        boolean deleted = actualiteService.deleteImage(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204 - No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 - Not Found
        }
    }

}
