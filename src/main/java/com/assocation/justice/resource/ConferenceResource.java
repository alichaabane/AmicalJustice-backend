package com.assocation.justice.resource;

import com.assocation.justice.dto.ActualiteDTO;
import com.assocation.justice.dto.ConferenceDTO;
import com.assocation.justice.service.ActualiteService;
import com.assocation.justice.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/conferences")
public class ConferenceResource {
    private final ConferenceService conferenceService;

    @Autowired
    public ConferenceResource(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }


    @GetMapping("/conference/{id}")
    public ResponseEntity<ConferenceDTO> getImageById(@PathVariable Long id) {
        ConferenceDTO conferenceDTO = conferenceService.getConferenceById(id);
        if (conferenceDTO != null) {
            return ResponseEntity.ok(conferenceDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConferenceDTO> updateConference(@PathVariable Long id,
                                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                                    @RequestParam("title") String title,
                                                    @RequestParam("active") boolean active) {
        ConferenceDTO updatedConferenceDTO = new ConferenceDTO();
        updatedConferenceDTO.setTitle(title);
        updatedConferenceDTO.setActive(active);
        updatedConferenceDTO.setUpdatedAt(LocalDateTime.now());

        ConferenceDTO resultDTO = conferenceService.updateConference(id, file, updatedConferenceDTO);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("")
    public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        List<ConferenceDTO> imageDTOs = conferenceService.getAllConferences();
        return ResponseEntity.ok(imageDTOs);
    }

    @PostMapping("/upload")
    public ConferenceDTO uploadConference(@RequestParam("file") MultipartFile file,
                                          @RequestParam("title") String title,
                                          @RequestParam("active") boolean active) {
        ConferenceDTO newConferenceDTO = new ConferenceDTO();
        newConferenceDTO.setTitle(title);
        newConferenceDTO.setActive(active);
        newConferenceDTO.setCreatedAt(LocalDateTime.now());
        newConferenceDTO.setUpdatedAt(LocalDateTime.now());

        return conferenceService.saveConference(file, newConferenceDTO);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<ConferenceDTO> toggleVisibleState(@PathVariable Long id) {
        ResponseEntity<ConferenceDTO> responseEntity = conferenceService.changeConferenceVisibleState(id);
        if (responseEntity != null) {
            return responseEntity; // Return the response from changeImageVisibleState
        } else {
            return ResponseEntity.notFound().build(); // Return a 404 response if changeImageVisibleState returns null
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getConference(@PathVariable String filename) throws IOException {
        Resource resource = new ClassPathResource("static/conferences/" + filename);
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_PDF) // Adjust content type based on your image type
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConference(@PathVariable Long id) {
        boolean deleted = conferenceService.deleteConference(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204 - No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 - Not Found
        }
    }

}
