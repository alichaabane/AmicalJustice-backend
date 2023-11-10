package com.assocation.justice.repository;

import com.assocation.justice.entity.Actualite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActualiteRepository extends JpaRepository<Actualite, Long> {
    String findImageByName(String name);
}
