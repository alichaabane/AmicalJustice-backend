package com.assocation.justice.repository;

import com.assocation.justice.entity.Conference;
import com.assocation.justice.entity.Recherche;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechercheRepository extends JpaRepository<Recherche, Long> {
}
