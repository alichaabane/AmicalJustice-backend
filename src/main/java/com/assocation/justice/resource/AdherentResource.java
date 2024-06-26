package com.assocation.justice.resource;

import com.assocation.justice.dto.AdherentDTO;
import com.assocation.justice.dto.ConferenceDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.service.AdherentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/adherents")
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

    @GetMapping("/export-xlsx")
    public void exportAdherentList(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=adherents.xlsx");

        List<AdherentDTO> adherents = adherentService.getAllAdherents(); // Replace with your data retrieval logic
        try {
            Workbook workbook = adherentService.exportAdherentListToExcel(adherents);
            workbook.write(response.getOutputStream());
        }
        catch (Exception e) {
            System.out.println("Error in parsing excel file - adherent : " + e);
        }
    }

    @GetMapping
    public ResponseEntity<List<AdherentDTO>> getAllAdherents() {
        List<AdherentDTO> adherents = adherentService.getAllAdherents();
        return new ResponseEntity<>(adherents, HttpStatus.OK);
    }

    @GetMapping("/paginated")
    public ResponseEntity<PageRequestData<AdherentDTO>> getAllAdherentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequestData<AdherentDTO> adherentsPage = adherentService.getAllAdherentsByPage(pageRequest);
        return ResponseEntity.ok(adherentsPage);
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
