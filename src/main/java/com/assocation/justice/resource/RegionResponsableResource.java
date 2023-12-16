package com.assocation.justice.resource;

import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.RegionResponsableDTO2;
import com.assocation.justice.dto.ResponsableDTO;
import com.assocation.justice.service.RegionResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/regionsResponsable")
public class RegionResponsableResource {

    private final RegionResponsableService regionResponsableService;

    @Autowired
    public RegionResponsableResource(RegionResponsableService regionResponsableService) {
        this.regionResponsableService = regionResponsableService;
    }

    @GetMapping("")
    public ResponseEntity<List<RegionResponsableDTO2>> getRegionResponsables() {
        List<RegionResponsableDTO2> regionResponsables = regionResponsableService.getAllRegionResponsables();
        if (regionResponsables != null) {
            return new ResponseEntity<>(regionResponsables, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<PageRequestData<RegionResponsableDTO2>> getRegionResponsablesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequestData<RegionResponsableDTO2> regionResponsables = regionResponsableService.getAllRegionResponsablesPaginated(pageRequest);
        if(regionResponsables != null){
            return new ResponseEntity<>(regionResponsables, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/region")
    public ResponseEntity<List<RegionResponsableDTO>> getAllRegionResponsablesByRegionName(@RequestBody String region) {
        List<RegionResponsableDTO> regionResponsables = regionResponsableService.getAllRegionResponsableByRegion(region);
        if(regionResponsables != null){
            return new ResponseEntity<>(regionResponsables, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponsableDTO2> getRegionResponsable(@PathVariable Long id) {
        RegionResponsableDTO2 regionResponsable = regionResponsableService.getRegionResponsableById(id);
        if (regionResponsable != null) {
            return new ResponseEntity<>(regionResponsable, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   @PostMapping("")
    public ResponseEntity<RegionResponsableDTO2> createRegionResponsable(@RequestBody RegionResponsableDTO2 regionResponsableDTO) {
        RegionResponsableDTO2 createdRegionResponsable = regionResponsableService.createRegionResponsable(regionResponsableDTO);
        if(createdRegionResponsable != null) {
            return new ResponseEntity<>(createdRegionResponsable, HttpStatus.CREATED);
        } else {
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponsableDTO2> updateRegionResponsable(@PathVariable Long id, @RequestBody RegionResponsableDTO2 regionResponsableDTO) {
        RegionResponsableDTO2 updatedRegionResponsable = regionResponsableService.updateRegionResponsable(id, regionResponsableDTO);
        if (updatedRegionResponsable != null) {
            return new ResponseEntity<>(updatedRegionResponsable, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegionResponsable(@PathVariable Long id) {
        regionResponsableService.deleteRegionResponsable(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
