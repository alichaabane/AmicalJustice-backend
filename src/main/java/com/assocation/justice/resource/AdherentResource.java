package com.assocation.justice.resource;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.service.AdherentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/adherents")
@RequiredArgsConstructor
public class AdherentResource {

    private final AdherentService adherentService;

    @PostMapping
    public ResponseEntity<AdherentDTO> createAdherent(@RequestBody AdherentDTO adherentDTO) {
        AdherentDTO createdAdherent = adherentService.createAdherent(adherentDTO);
        if (createdAdherent != null) {
            return new ResponseEntity<>(createdAdherent, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{cin}")
    public ResponseEntity<AdherentDTO> getAdherentById(@PathVariable Integer cin) {
        AdherentDTO adherent = adherentService.getAdherentById(cin);
        if (adherent != null) {
            return new ResponseEntity<>(adherent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<AdherentDTO>> getAllAdherents() {
        List<AdherentDTO> adherents = adherentService.getAllAdherents();
        return new ResponseEntity<>(adherents, HttpStatus.OK);
    }

    @PutMapping("/{cin}")
    public ResponseEntity<AdherentDTO> updateAdherent(@PathVariable Integer cin, @RequestBody AdherentDTO adherentDTO) {
        AdherentDTO updatedAdherent = adherentService.updateAdherent(cin, adherentDTO);
        if (updatedAdherent != null) {
            return new ResponseEntity<>(updatedAdherent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cin}")
    public ResponseEntity<Void> deleteAdherent(@PathVariable Integer cin) {
        adherentService.deleteAdherent(cin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/region-responsable")
    public ResponseEntity<List<AdherentDTO>> getAdherentsByRegionResponsable(@RequestBody Map<String, Long> requestBody) {
        Long regionResponsableId = requestBody.get("regionResponsableId");
        List<AdherentDTO> adherents = adherentService.getAdherentsByRegionResponsable(regionResponsableId);
        return new ResponseEntity<>(adherents, HttpStatus.OK);
    }
}
