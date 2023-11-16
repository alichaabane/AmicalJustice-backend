package com.assocation.justice.resource;

import com.assocation.justice.dto.ActualiteDTO;
import com.assocation.justice.dto.RechercheDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.service.ActualiteService;
import com.assocation.justice.service.RechercheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/recherches")
public class RechercheResource {
    private final RechercheService rechercheService;

    @Autowired
    public RechercheResource(RechercheService rechercheService) {
        this.rechercheService = rechercheService;
    }


    @GetMapping("/recherche/{id}")
    public ResponseEntity<RechercheDTO> getImageById(@PathVariable Long id) {
        RechercheDTO rechercheDTO = rechercheService.getRechercheById(id);
        if (rechercheDTO != null) {
            return ResponseEntity.ok(rechercheDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RechercheDTO> updateRecherche(@PathVariable Long id,
                                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                                        @RequestParam("title") String title,
                                                        @RequestParam("active") boolean active) {
        RechercheDTO updatedRechercheDTO = new RechercheDTO();
        updatedRechercheDTO.setTitle(title);
        updatedRechercheDTO.setActive(active);
        updatedRechercheDTO.setUpdatedAt(LocalDateTime.now());

        RechercheDTO resultDTO = rechercheService.updateRecherche(id, file, updatedRechercheDTO);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("")
    public ResponseEntity<PageRequestData<RechercheDTO>> getAllRecherches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequestData<RechercheDTO> recherchePage = rechercheService.getAllRecherches(pageRequest);
        return ResponseEntity.ok(recherchePage);
    }

    @PostMapping("/upload")
    public RechercheDTO uploadRecherche(@RequestParam("file") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("active") boolean active) {
        RechercheDTO newRechercheDTO = new RechercheDTO();
        newRechercheDTO.setTitle(title);
        newRechercheDTO.setActive(active);
        newRechercheDTO.setCreatedAt(LocalDateTime.now());
        newRechercheDTO.setUpdatedAt(LocalDateTime.now());

        return rechercheService.saveRecherche(file, newRechercheDTO);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<RechercheDTO> toggleVisibleState(@PathVariable Long id) {
        ResponseEntity<RechercheDTO> responseEntity = rechercheService.changeRechercheVisibleState(id);
        if (responseEntity != null) {
            return responseEntity; // Return the response from changeImageVisibleState
        } else {
            return ResponseEntity.notFound().build(); // Return a 404 response if changeImageVisibleState returns null
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<?> getRecherche(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get("src", "main", "resources", "static", "recherches", filename);
        UrlResource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecherche(@PathVariable Long id) {
        boolean deleted = rechercheService.deleteRecherche(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204 - No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 - Not Found
        }
    }

}
