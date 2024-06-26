package com.assocation.justice.resource;

import com.assocation.justice.dto.ActualiteDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO2;
import com.assocation.justice.service.ActualiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/actualites")
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
    @GetMapping("/paginated")
    public ResponseEntity<PageRequestData<ActualiteDTO>> getAllImagesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequestData<ActualiteDTO> actualiteDTOPageRequestData = actualiteService.getAllImagesPaginated(pageRequest);
        if(actualiteDTOPageRequestData != null){
            return new ResponseEntity<>(actualiteDTOPageRequestData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //TODO
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Resource resource = new ClassPathResource("static/uploads/" + filename);

        if (!resource.exists()) {
            // Handle the case where the resource (image) is not found
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Set a default media type to handle images
                .body(resource);
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
        // Return a 404 response if changeImageVisibleState returns null
        return Objects.requireNonNullElseGet(responseEntity, () -> ResponseEntity.notFound().build()); // Return the response from changeImageVisibleState
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
