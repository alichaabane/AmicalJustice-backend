package com.assocation.justice.service;

import com.assocation.justice.dto.ContactDTO;
import com.assocation.justice.dto.PageRequestData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ContactService {
    ContactDTO saveContact(ContactDTO contactDTO);
    ContactDTO getContactById(Long id);
    PageRequestData<ContactDTO> getContactsByRegionResponsableId(Long regionResponsableId, PageRequest pageRequest);
    PageRequestData<ContactDTO> getAllContacts(PageRequest pageRequest);
    boolean deleteContact(Long id);

    // Add other methods as needed
}