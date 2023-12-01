package com.assocation.justice.resource;

import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.MagazineDTO;
import com.assocation.justice.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/magazines")
public class MagazineResource {
    private final MagazineService magazineService;

    @Autowired
    public MagazineResource(MagazineService magazineService) {
        this.magazineService = magazineService;
    }


    @GetMapping("/magazine/{id}")
    public ResponseEntity<MagazineDTO> getImageById(@PathVariable Long id) {
        MagazineDTO magazineDTO = magazineService.getMagazineById(id);
        if (magazineDTO != null) {
            return ResponseEntity.ok(magazineDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MagazineDTO> updateMagazine(@PathVariable Long id,
                                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                                        @RequestParam("title") String title,
                                                        @RequestParam("active") boolean active) {
        MagazineDTO updatedMagazineDTO = new MagazineDTO();
        updatedMagazineDTO.setTitle(title);
        updatedMagazineDTO.setActive(active);
        updatedMagazineDTO.setUpdatedAt(LocalDateTime.now());

        MagazineDTO resultDTO = magazineService.updateMagazine(id, file, updatedMagazineDTO);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("")
    public ResponseEntity<PageRequestData<MagazineDTO>> getAllMagazines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequestData<MagazineDTO> magazinePage = magazineService.getAllMagazines(pageRequest);
        return ResponseEntity.ok(magazinePage);
    }

    @PostMapping("/upload")
    public MagazineDTO uploadMagazine(@RequestParam("file") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("active") boolean active) {
        MagazineDTO newMagazineDTO = new MagazineDTO();
        newMagazineDTO.setTitle(title);
        newMagazineDTO.setActive(active);
        newMagazineDTO.setCreatedAt(LocalDateTime.now());
        newMagazineDTO.setUpdatedAt(LocalDateTime.now());

        return magazineService.saveMagazine(file, newMagazineDTO);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<MagazineDTO> toggleVisibleState(@PathVariable Long id) {
        ResponseEntity<MagazineDTO> responseEntity = magazineService.changeMagazineVisibleState(id);
        if (responseEntity != null) {
            return responseEntity; // Return the response from changeImageVisibleState
        } else {
            return ResponseEntity.notFound().build(); // Return a 404 response if changeImageVisibleState returns null
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<?> getMagazine(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get("src", "main", "resources", "static", "magazines", filename);
        UrlResource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


    @GetMapping("/video/{filename:.+}")
    public ResponseEntity<?> getVideo(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get("src", "main", "resources", "static", "videos", filename + ".mp4");
        UrlResource resource = new UrlResource(filePath.toUri());

        // Set the content type for MP4 video
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("video/mp4"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagazine(@PathVariable Long id) {
        boolean deleted = magazineService.deleteMagazine(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204 - No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 - Not Found
        }
    }

}
