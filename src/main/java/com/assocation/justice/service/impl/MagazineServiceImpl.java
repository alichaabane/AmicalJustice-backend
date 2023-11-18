package com.assocation.justice.service.impl;

import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.dto.MagazineDTO;
import com.assocation.justice.entity.Magazine;
import com.assocation.justice.repository.MagazineRepository;
import com.assocation.justice.service.MagazineService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class MagazineServiceImpl implements MagazineService {
    private final Logger logger = LoggerFactory.getLogger(MagazineServiceImpl.class);
    private final MagazineRepository magazineRepository;

    @Value("${app.magazine.dir}")
    private String uploadMagazineDir;

    @Autowired
    public MagazineServiceImpl(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @Override
    @Transactional
    public MagazineDTO saveMagazine(MultipartFile file, MagazineDTO newMagazineDTO) {
        try {
            String fileName = newMagazineDTO.getName() != null ? newMagazineDTO.getName() : file.getOriginalFilename();
            assert fileName != null;
            fileName = fileName.replaceAll("\\s+", "_").trim();

            Path copyLocation = Paths.get(uploadMagazineDir + File.separator + StringUtils.cleanPath(Objects.requireNonNull(fileName)));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            // Set the filename in the DTO
            newMagazineDTO.setName(fileName);
            newMagazineDTO.setCreatedAt(LocalDateTime.now());
            newMagazineDTO.setUpdatedAt(LocalDateTime.now());

            // Save the image information to the database
            Magazine saveMagazineToDatabase = saveMagazineToDatabase(newMagazineDTO);

            // Map the saved image to DTO for response
            return mapMagazineToDTO(saveMagazineToDatabase);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the file", e);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving classpath resource", e);
        }
    }

    private Magazine saveMagazineToDatabase(MagazineDTO magazineDTO) {
        Magazine magazine = new Magazine();
        magazine.setId(magazineDTO.getId());
        magazine.setName(magazineDTO.getName());
        magazine.setTitle(magazineDTO.getTitle());
        magazine.setActive(magazineDTO.isActive());
        magazine.setCreatedAt(LocalDateTime.now());
        magazine.setUpdatedAt(LocalDateTime.now());
        return magazineRepository.save(magazine);
    }

    public MagazineDTO mapMagazineToDTO(Magazine magazine) {
        MagazineDTO dto = new MagazineDTO();
        dto.setId(magazine.getId());
        dto.setActive(magazine.isActive());
        dto.setTitle(magazine.getTitle());
        dto.setName(magazine.getName());
        dto.setCreatedAt(magazine.getCreatedAt());
        dto.setUpdatedAt(magazine.getUpdatedAt());
        return dto;
    }

    @Override
    public MagazineDTO getMagazineById(Long id) {
        Magazine magazine = magazineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Magazine not found with ID: " + id));
        logger.info("Magazine fetched successfully");
        return mapMagazineToDTO(magazine);
    }

    @Override
    public PageRequestData<MagazineDTO> getAllMagazines(PageRequest pageRequest) {
        logger.info("Magazine list fetched successfully");
        Page<Magazine> magazinePage = magazineRepository.findAll(pageRequest);
        PageRequestData<MagazineDTO> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(magazinePage.map(this::mapMagazineToDTO).getContent());
        customPageResponse.setTotalPages(magazinePage.getTotalPages());
        customPageResponse.setTotalElements(magazinePage.getTotalElements());
        customPageResponse.setNumber(magazinePage.getNumber());
        customPageResponse.setSize(magazinePage.getSize());

        return customPageResponse;
    }

    @Override
    public MagazineDTO updateMagazine(Long magazineId, MultipartFile file, MagazineDTO updatedMagazineDTO) {
        Magazine existingMagazine = magazineRepository.findById(magazineId)
                .orElseThrow(() -> new RuntimeException("Magazine not found with id: " + magazineId));
        try {
            if (file != null) {
                // A new file is provided, update the file
                Files.createDirectories(Path.of(uploadMagazineDir));

                String fileName = updatedMagazineDTO.getName() != null ? updatedMagazineDTO.getName() : file.getOriginalFilename();
                assert fileName != null;
                fileName = fileName.replaceAll("\\s+", "_").trim() ;

                Path filePath = Path.of(uploadMagazineDir, fileName);

                // Check if the existing file should be deleted
                if (existingMagazine.getName() != null && !existingMagazine.getName().isEmpty()) {
                    Path existingFilePath = Path.of(uploadMagazineDir, existingMagazine.getName());
                    Files.deleteIfExists(existingFilePath);
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingMagazine.setName(fileName);
            }

            // Update other fields
            existingMagazine.setTitle(updatedMagazineDTO.getTitle());
            existingMagazine.setActive(updatedMagazineDTO.isActive());
            existingMagazine.setUpdatedAt(LocalDateTime.now());

            Magazine updatedMagazine = magazineRepository.save(existingMagazine);

            return mapMagazineToDTO(updatedMagazine);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update the file", e);
        }
    }

    @Override
    public ResponseEntity<MagazineDTO> changeMagazineVisibleState(Long id) {
        Magazine magazine = magazineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        magazine.setActive(!magazine.isActive());
        // Save the updated image back to the repository
        magazine = magazineRepository.save(magazine);
        logger.info("Actualite N° " + id + " change its visibility");
        // Return a ResponseEntity with the updated image and an HTTP status code
        return ResponseEntity.ok(mapMagazineToDTO(magazine));
    }

    @Override
    @Transactional
    public boolean deleteMagazine(Long id) {
        Optional<Magazine> actualiteOptional = magazineRepository.findById(id);
        if (actualiteOptional.isPresent()) {
            magazineRepository.delete(actualiteOptional.get());
            logger.info("Magazine deleted successfully");
            return true; // Deletion was successful
        }
        logger.error("Error in deleting Magazine");
        return false; // Magazine with the given ID was not found
    }
}