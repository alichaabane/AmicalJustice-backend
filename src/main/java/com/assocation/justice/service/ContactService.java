package com.assocation.justice.service;

import com.assocation.justice.dto.ContactDTO;
import com.assocation.justice.entity.Contact;

import java.util.List;

public interface ContactService {
    ContactDTO saveContact(ContactDTO contactDTO);
    ContactDTO getContactById(Long id);
    List<ContactDTO> getAllContacts();
    // Add other methods as needed
}