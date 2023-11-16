package com.assocation.justice.service.impl;

import com.assocation.justice.dto.RechercheDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.entity.Recherche;
import com.assocation.justice.repository.RechercheRepository;
import com.assocation.justice.service.RechercheService;
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
public class RechercheServiceImpl implements RechercheService {
    private final Logger logger = LoggerFactory.getLogger(RechercheServiceImpl.class);
    private final RechercheRepository rechercheRepository;

    @Value("${app.recherche.dir}")
    private String uploadRechercheDir;

    @Autowired
    public RechercheServiceImpl(RechercheRepository rechercheRepository) {
        this.rechercheRepository = rechercheRepository;
    }

    @Override
    @Transactional
    public RechercheDTO saveRecherche(MultipartFile file, RechercheDTO newRechercheDTO) {
        try {
            String fileName = newRechercheDTO.getName() != null ? newRechercheDTO.getName() : file.getOriginalFilename();
            assert fileName != null;
            fileName = fileName.replaceAll("\\s+", "_").trim();

            Path copyLocation = Paths.get(uploadRechercheDir + File.separator + StringUtils.cleanPath(Objects.requireNonNull(fileName)));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            // Set the filename in the DTO
            newRechercheDTO.setName(fileName);
            newRechercheDTO.setCreatedAt(LocalDateTime.now());
            newRechercheDTO.setUpdatedAt(LocalDateTime.now());

            // Save the image information to the database
            Recherche saveRechercheToDatabase = saveRechercheToDatabase(newRechercheDTO);

            // Map the saved image to DTO for response
            return mapRechercheToDTO(saveRechercheToDatabase);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the file", e);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving classpath resource", e);
        }
    }

    private Recherche saveRechercheToDatabase(RechercheDTO rechercheDTO) {
        Recherche recherche = new Recherche();
        recherche.setId(rechercheDTO.getId());
        recherche.setName(rechercheDTO.getName());
        recherche.setTitle(rechercheDTO.getTitle());
        recherche.setActive(rechercheDTO.isActive());
        recherche.setCreatedAt(LocalDateTime.now());
        recherche.setUpdatedAt(LocalDateTime.now());
        return rechercheRepository.save(recherche);
    }

    public RechercheDTO mapRechercheToDTO(Recherche recherche) {
        RechercheDTO dto = new RechercheDTO();
        dto.setId(recherche.getId());
        dto.setActive(recherche.isActive());
        dto.setTitle(recherche.getTitle());
        dto.setName(recherche.getName());
        dto.setCreatedAt(recherche.getCreatedAt());
        dto.setUpdatedAt(recherche.getUpdatedAt());
        return dto;
    }

    @Override
    public RechercheDTO getRechercheById(Long id) {
        Recherche recherche = rechercheRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recherche not found with ID: " + id));
        logger.info("Recherche fetched successfully");
        return mapRechercheToDTO(recherche);
    }

    @Override
    public PageRequestData<RechercheDTO> getAllRecherches(PageRequest pageRequest) {
        logger.info("Recherche list fetched successfully");
        Page<Recherche> recherchePage = rechercheRepository.findAll(pageRequest);
        PageRequestData<RechercheDTO> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(recherchePage.map(this::mapRechercheToDTO).getContent());
        customPageResponse.setTotalPages(recherchePage.getTotalPages());
        customPageResponse.setTotalElements(recherchePage.getTotalElements());
        customPageResponse.setNumber(recherchePage.getNumber());
        customPageResponse.setSize(recherchePage.getSize());

        return customPageResponse;
    }

    @Override
    public RechercheDTO updateRecherche(Long rechercheId, MultipartFile file, RechercheDTO updatedRechercheDTO) {
        Recherche existingRecherche = rechercheRepository.findById(rechercheId)
                .orElseThrow(() -> new RuntimeException("Recherche not found with id: " + rechercheId));
        try {
            if (file != null) {
                // A new file is provided, update the file
                Files.createDirectories(Path.of(uploadRechercheDir));

                String fileName = updatedRechercheDTO.getName() != null ? updatedRechercheDTO.getName() : file.getOriginalFilename();
                assert fileName != null;
                fileName = fileName.replaceAll("\\s+", "_").trim() ;

                Path filePath = Path.of(uploadRechercheDir, fileName);

                // Check if the existing file should be deleted
                if (existingRecherche.getName() != null && !existingRecherche.getName().isEmpty()) {
                    Path existingFilePath = Path.of(uploadRechercheDir, existingRecherche.getName());
                    Files.deleteIfExists(existingFilePath);
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingRecherche.setName(fileName);
            }

            // Update other fields
            existingRecherche.setTitle(updatedRechercheDTO.getTitle());
            existingRecherche.setActive(updatedRechercheDTO.isActive());
            existingRecherche.setUpdatedAt(LocalDateTime.now());

            Recherche updatedRecherche = rechercheRepository.save(existingRecherche);

            return mapRechercheToDTO(updatedRecherche);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update the file", e);
        }
    }

    @Override
    public ResponseEntity<RechercheDTO> changeRechercheVisibleState(Long id) {
        Recherche recherche = rechercheRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        recherche.setActive(!recherche.isActive());
        // Save the updated image back to the repository
        recherche = rechercheRepository.save(recherche);
        logger.info("Actualite N° " + id + " change its visibility");
        // Return a ResponseEntity with the updated image and an HTTP status code
        return ResponseEntity.ok(mapRechercheToDTO(recherche));
    }

    @Override
    @Transactional
    public boolean deleteRecherche(Long id) {
        Optional<Recherche> actualiteOptional = rechercheRepository.findById(id);
        if (actualiteOptional.isPresent()) {
            rechercheRepository.delete(actualiteOptional.get());
            logger.info("Recherche deleted successfully");
            return true; // Deletion was successful
        }
        logger.error("Error in deleting Recherche");
        return false; // Recherche with the given ID was not found
    }
}