package com.assocation.justice.resource;

import com.assocation.justice.dto.NationaleResponsableDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.ResponsableDTO;
import com.assocation.justice.service.ResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/responsables")
public class ResponsableResource {
        private final ResponsableService responsableService;

        @Autowired
        public ResponsableResource(ResponsableService responsableService) {
            this.responsableService = responsableService;
        }

        @GetMapping("/{id}")
        public ResponseEntity<ResponsableDTO> getResponsableById(@PathVariable Long id) {
            ResponsableDTO responsableDTO = responsableService.getResponsableById(id);
            if (responsableDTO != null) {
                return ResponseEntity.ok(responsableDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

    @GetMapping("")
    public ResponseEntity<List<ResponsableDTO>> getResponsables() {
        List<ResponsableDTO> responsableDTOS = responsableService.getAllResponsables();
        return ResponseEntity.ok(responsableDTOS);
    }

        @GetMapping("/paginated")
        public ResponseEntity<PageRequestData<ResponsableDTO>> getResponsablesPaginated(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            PageRequest pageRequest = PageRequest.of(page, size);
            PageRequestData<ResponsableDTO> responsables = responsableService.getAllResponsablesPaginated(pageRequest);
            if(responsables != null){
                return new ResponseEntity<>(responsables, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }


        @PostMapping("")
        public ResponseEntity<ResponsableDTO> createResponsable(@RequestBody ResponsableDTO responsableDTO) {
            ResponsableDTO createdResponsableDTO = responsableService.createResponsable(responsableDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResponsableDTO);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ResponsableDTO> updateResponsable(@PathVariable Long id, @RequestBody ResponsableDTO responsableDTO) {
            ResponsableDTO updatedResponsableDTO = responsableService.updateResponsable(id, responsableDTO);
            if (updatedResponsableDTO != null) {
                return ResponseEntity.ok(updatedResponsableDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteResponsable(@PathVariable Long id) {
            responsableService.deleteResponsable(id);
            return ResponseEntity.noContent().build();
        }
    }
