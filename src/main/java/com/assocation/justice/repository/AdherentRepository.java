package com.assocation.justice.repository;

import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.entity.Adherent;
import com.assocation.justice.entity.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdherentRepository extends JpaRepository<Adherent, Integer> {
    List<Adherent> findAllByRegionResponsableId(Long regionResponsableId);
}
