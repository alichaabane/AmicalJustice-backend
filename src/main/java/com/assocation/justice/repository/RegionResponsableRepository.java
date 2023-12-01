package com.assocation.justice.repository;

import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.entity.Responsable;
import com.assocation.justice.util.enumeration.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionResponsableRepository extends JpaRepository<RegionResponsable, Long> {
    RegionResponsable findByRegion(Region region);
    RegionResponsable findByNom(String nom);
    List<RegionResponsable> findAllByRegion(Region region);

}
