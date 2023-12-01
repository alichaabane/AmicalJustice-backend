package com.assocation.justice.service.impl;

import com.assocation.justice.dto.ContactDTO;
import com.assocation.justice.entity.Contact;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.repository.ContactRepository;
import com.assocation.justice.repository.RegionResponsableRepository;
import com.assocation.justice.service.ContactService;
import com.assocation.justice.util.enumeration.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    private final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;
    private final RegionResponsableRepository regionResponsableRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, RegionResponsableRepository regionResponsableRepository) {
        this.contactRepository = contactRepository;
        this.regionResponsableRepository = regionResponsableRepository;
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
    public List<ContactDTO> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        List<ContactDTO> contactDTOS = contacts.stream()
                .map(this::mapToDTO)
                .sorted(Comparator.comparing(ContactDTO::getRegionResponsableName)).collect(Collectors.toList());
        logger.info("contacts list fetched successfully");
        return contactDTOS;
    }

    // Helper method to map ContactDTO to Contact entity
    public Contact mapToEntity(ContactDTO contactDTO, RegionResponsable regionResponsable) {
        Contact contact = new Contact();
        contact.setId(contactDTO.getId());
        contact.setNom(contactDTO.getNom());
        contact.setTelephone(contactDTO.getTelephone());
        contact.setEmail(contactDTO.getEmail());
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
        contactDTO.setMessage(contact.getMessage());
        contactDTO.setRegionResponsableCity(contact.getRegionResponsable().getRegion());
        contactDTO.setRegionResponsableName(contact.getRegionResponsable().getNom());

        return contactDTO;
    }

    // Implement other methods if needed
}
