package com.assocation.justice.repository;

import com.assocation.justice.entity.Actualite;
import com.assocation.justice.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}
