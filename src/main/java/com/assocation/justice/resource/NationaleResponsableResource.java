package com.assocation.justice.resource;

import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.service.NationaleResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/nationale-responsables")
public class NationaleResponsableResource {
        private final NationaleResponsableService nationaleResponsableService;

        @Autowired
        public NationaleResponsableResource(NationaleResponsableService nationaleResponsableService) {
            this.nationaleResponsableService = nationaleResponsableService;
        }

        @GetMapping("/{id}")
        public ResponseEntity<NationaleResponsableDTO> getResponsableById(@PathVariable Long id) {
            NationaleResponsableDTO responsableDTO = nationaleResponsableService.getNationaleResponsableById(id);
            if (responsableDTO != null) {
                return ResponseEntity.ok(responsableDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @GetMapping("")
        public ResponseEntity<List<NationaleResponsableDTO>> getResponsables() {
            List<NationaleResponsableDTO> responsableDTOS = nationaleResponsableService.getAllNationaleResponsables();
            return ResponseEntity.ok(responsableDTOS);
        }


        @PostMapping("")
        public ResponseEntity<NationaleResponsableDTO> createResponsable(@RequestBody NationaleResponsableDTO responsableDTO) {
            NationaleResponsableDTO createdResponsableDTO = nationaleResponsableService.createNationaleResponsable(responsableDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResponsableDTO);
        }

        @PutMapping("/{id}")
        public ResponseEntity<NationaleResponsableDTO> updateResponsable(@PathVariable Long id, @RequestBody NationaleResponsableDTO responsableDTO) {
            NationaleResponsableDTO updatedResponsableDTO = nationaleResponsableService.updateNationaleResponsable(id, responsableDTO);
            if (updatedResponsableDTO != null) {
                return ResponseEntity.ok(updatedResponsableDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteResponsable(@PathVariable Long id) {
            nationaleResponsableService.deleteNationaleResponsable(id);
            return ResponseEntity.noContent().build();
        }
    }
