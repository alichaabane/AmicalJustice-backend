package com.assocation.justice.resource;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.service.NationaleResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
        public ResponseEntity<NationaleResponsableDTO> getNationaleResponsableById(@PathVariable Long id) {
            NationaleResponsableDTO responsableDTO = nationaleResponsableService.getNationaleResponsableById(id);
            if (responsableDTO != null) {
                return ResponseEntity.ok(responsableDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @GetMapping("")
        public ResponseEntity<PageRequestData<NationaleResponsableDTO>> getNationaleResponsables(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            PageRequest pageRequest = PageRequest.of(page, size);
            PageRequestData<NationaleResponsableDTO> nationaleResponsables = nationaleResponsableService.getAllNationaleResponsables(pageRequest);
            return ResponseEntity.ok(nationaleResponsables);
        }


        @PostMapping("")
        public ResponseEntity<NationaleResponsableDTO> createNationaleResponsable(@RequestBody NationaleResponsableDTO responsableDTO) {
            NationaleResponsableDTO createdResponsableDTO = nationaleResponsableService.createNationaleResponsable(responsableDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResponsableDTO);
        }

        @PutMapping("/{id}")
        public ResponseEntity<NationaleResponsableDTO> updateNationaleResponsable(@PathVariable Long id, @RequestBody NationaleResponsableDTO responsableDTO) {
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
