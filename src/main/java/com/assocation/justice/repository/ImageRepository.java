package com.assocation.justice.repository;

import com.assocation.justice.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    String findImageByName(String name);
}
