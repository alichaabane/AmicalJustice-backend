package com.assocation.justice.repository;

import com.assocation.justice.dto.RegionResponsableDTO;
import com.assocation.justice.dto.ResponsableDTO;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.entity.Responsable;
import com.assocation.justice.util.enumeration.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResponsableRepository extends JpaRepository<Responsable, Long> {
    List<Responsable> findByRegionResponsable(RegionResponsableDTO regionResponsable);
}
