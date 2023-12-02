package com.assocation.justice.service.impl;

import com.assocation.justice.dto.ContactDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.RegionResponsableDTO2;
import com.assocation.justice.entity.Contact;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.repository.ContactRepository;
import com.assocation.justice.repository.RegionResponsableRepository;
import com.assocation.justice.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    private final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;
    private final RegionResponsableRepository regionResponsableRepository;
    private final RegionResponsableServiceImpl regionResponsableService;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, RegionResponsableRepository regionResponsableRepository, RegionResponsableServiceImpl regionResponsableService) {
        this.contactRepository = contactRepository;
        this.regionResponsableRepository = regionResponsableRepository;
        this.regionResponsableService = regionResponsableService;
    }

    @Override
    public ContactDTO saveContact(ContactDTO contactDTO) {
        String regionResponsableName = contactDTO.getRegionResponsableName();
        RegionResponsable regionResponsable = regionResponsableRepository.findByNom(regionResponsableName);

        if (regionResponsable != null) {
            // Create the Responsable entity
            Contact contact = mapToEntity(contactDTO, regionResponsable);
            contact = contactRepository.save(contact);
            return mapToDTO(contact);
        } else {
            // Handle the case when the specified RegionResponsable doesn't exist
            // You can throw an exception or handle it as needed.
            return null;
        }    }

    @Override
    public ContactDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id).orElse(null);
        return contact != null ? mapToDTO(contact) : null;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageRequestData<ContactDTO> getContactsByRegionResponsableId(Long regionResponsableId, PageRequest pageRequest) {
        Optional<RegionResponsable> regionResponsable = this.regionResponsableRepository.findById(regionResponsableId);
        System.out.println(regionResponsable.isPresent());
        if(regionResponsable.isPresent()) {
        Page<Contact> contacts = this.contactRepository.findAllByRegionResponsable((regionResponsable.get()), PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()));

            return getContactDTOPageRequestData(contacts);
        }
        return null;
    }

    @Override
    public PageRequestData<ContactDTO> getAllContacts(PageRequest pageRequest) {
        Page<Contact> contactPage = contactRepository.findAll(pageRequest);
        return getContactDTOPageRequestData(contactPage);
    }

    private PageRequestData<ContactDTO> getContactDTOPageRequestData(Page<Contact> contacts) {
        PageRequestData<ContactDTO> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(contacts.map(this::mapToDTO).getContent().stream()
                .sorted(Comparator.comparing(ContactDTO::getRegionResponsableName)).collect(Collectors.toList()));
        customPageResponse.setTotalPages(contacts.getTotalPages());
        customPageResponse.setTotalElements(contacts.getTotalElements());
        customPageResponse.setNumber(contacts.getNumber());
        customPageResponse.setSize(contacts.getSize());

        return customPageResponse;
    }

    // Helper method to map ContactDTO to Contact entity
    public Contact mapToEntity(ContactDTO contactDTO, RegionResponsable regionResponsable) {
        Contact contact = new Contact();
        contact.setId(contactDTO.getId());
        contact.setNom(contactDTO.getNom());
        contact.setTelephone(contactDTO.getTelephone());
        contact.setEmail(contactDTO.getEmail());
        contact.setCreatedAt(LocalDateTime.now());
        contact.setMessage(contactDTO.getMessage());
        contact.setRegionResponsable(regionResponsable); // Set the RegionResponsable reference

        return contact;
    }

    public ContactDTO mapToDTO(Contact contact) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contact.getId());
        contactDTO.setNom(contact.getNom());
        contactDTO.setTelephone(contact.getTelephone());
        contactDTO.setEmail(contact.getEmail());
        contactDTO.setCreatedAt(LocalDateTime.now());
        contactDTO.setMessage(contact.getMessage());
        contactDTO.setRegionResponsableCity(contact.getRegionResponsable().getRegion());
        contactDTO.setRegionResponsableName(contact.getRegionResponsable().getNom());

        return contactDTO;
    }


    @Override
    @Transactional
    public boolean deleteContact(Long id) {
        Optional<Contact> actualiteOptional = contactRepository.findById(id);
        if (actualiteOptional.isPresent()) {
            logger.info("Contact deleted successfully id = " + actualiteOptional.get().getId());
            contactRepository.delete(actualiteOptional.get());
            return true; // Deletion was successful
        }
        logger.error("Error in deleting Contact");
        return false; // Conference with the given ID was not found
    }
    // Implement other methods if needed
}
