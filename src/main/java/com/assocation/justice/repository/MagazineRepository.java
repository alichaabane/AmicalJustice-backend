package com.assocation.justice.repository;

import com.assocation.justice.entity.Magazine;
import com.assocation.justice.entity.Recherche;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {
}