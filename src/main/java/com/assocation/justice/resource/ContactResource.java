package com.assocation.justice.resource;

import com.assocation.justice.dto.ConferenceDTO;
import com.assocation.justice.dto.ContactDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactResource {

    private final ContactService contactService;

    @Autowired
    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO contactDTO1 = contactService.saveContact(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactDTO1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        ContactDTO contactDTO = contactService.getContactById(id);
        if (contactDTO != null) {
            return new ResponseEntity<>(contactDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/regionResponsable")
    public ResponseEntity<PageRequestData<ContactDTO>> getContactByRegionResponsableId(@RequestParam Long regionResponsableId,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequestData<ContactDTO> contacts = contactService.getContactsByRegionResponsableId(regionResponsableId, pageRequest);
        if (contacts != null) {
            return new ResponseEntity<>(contacts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<PageRequestData<ContactDTO>> getAllConferences(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequestData<ContactDTO> conferencePage = contactService.getAllContacts(pageRequest);
        return ResponseEntity.ok(conferencePage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        boolean deleted = contactService.deleteContact(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204 - No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 - Not Found
        }
    }


    // Helper method to map from ContactDTO to Contact
}
