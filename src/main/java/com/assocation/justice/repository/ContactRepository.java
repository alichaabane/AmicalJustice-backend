package com.assocation.justice.repository;

import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.entity.Contact;
import com.assocation.justice.entity.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByRegionResponsable(RegionResponsableDTO regionResponsable);
}
