package com.assocation.justice.service.impl;

import com.assocation.justice.dto.ActualiteDTO;
import com.assocation.justice.entity.Actualite;
import com.assocation.justice.entity.Actualite;
import com.assocation.justice.repository.ActualiteRepository;
import com.assocation.justice.service.ActualiteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActualiteServiceImpl implements ActualiteService {
    private final Logger logger = LoggerFactory.getLogger(ActualiteServiceImpl.class);
    private final ActualiteRepository actualiteRepository;
    private final ResourceLoader resourceLoader;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    public ActualiteServiceImpl(ActualiteRepository actualiteRepository, ResourceLoader resourceLoader) {
        this.actualiteRepository = actualiteRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ActualiteDTO saveImage(MultipartFile file, ActualiteDTO newImageDTO) {
        try {
            // Load the uploads directory as a resource
            System.out.println(file.getOriginalFilename());
            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileName = Objects.requireNonNull(file.getOriginalFilename());

            // Set the filename in the DTO
            newImageDTO.setName(fileName);

            // Save the image information to the database
            Actualite savedImage = saveImageToDatabase(newImageDTO);

            // Map the saved image to DTO for response
            return mapActualiteToDTO(savedImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the file", e);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving classpath resource", e);
        }
    }

    private Actualite saveImageToDatabase(ActualiteDTO actualiteDTO) {
        Actualite actualite = new Actualite();
        actualite.setId(actualiteDTO.getId());
        actualite.setName(actualiteDTO.getName());
        actualite.setTitle(actualiteDTO.getTitle());
        actualite.setText(actualiteDTO.getText());
        actualite.setActive(actualiteDTO.isActive());

        return actualiteRepository.save(actualite);
    }
    @Override
    public ActualiteDTO getActualiteById(Long id) {
        Actualite image = actualiteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actualite not found with ID: " + id));
        logger.info("actualite fetched successfully");
        return mapActualiteToDTO(image);
    }

    @Override
    public List<ActualiteDTO> getAllImages() {
        List<Actualite> images = actualiteRepository.findAll();
        List<ActualiteDTO> imageDTOs = images.stream()
                .map(this::mapActualiteToDTO)
                .collect(Collectors.toList());
        logger.info("Actualite list fetched successfully");
        return imageDTOs;
    }

    @Override
    public ActualiteDTO updateImage(Long imageId, MultipartFile file, ActualiteDTO updatedImageDTO) {
        Actualite existingImage = actualiteRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Actualite not found with id: " + imageId));

        try {
            if (file != null) {

                // A new file is provided, update the file
                Files.createDirectories(Path.of(uploadDir));
                String fileName = Objects.requireNonNull(file.getOriginalFilename());
                Path filePath = Path.of(uploadDir, fileName);

                // Check if the existing file should be deleted
                if (existingImage.getName() != null && !existingImage.getName().isEmpty()) {
                    Path existingFilePath = Path.of(uploadDir, existingImage.getName());
                    Files.deleteIfExists(existingFilePath);
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingImage.setName(fileName);
            }

            // Update other fields
            existingImage.setTitle(updatedImageDTO.getTitle());
            existingImage.setText(updatedImageDTO.getText());
            existingImage.setActive(updatedImageDTO.isActive());

            Actualite updatedImage = actualiteRepository.save(existingImage);

            return mapActualiteToDTO(updatedImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update the file", e);
        }
    }

    String extractImageName(String fileName ) {
       return fileName.substring(0,fileName.indexOf('.')).trim();
    }


    @Override
    @Transactional
    public boolean deleteImage(Long id) {
        Optional<Actualite> actualiteOptional = actualiteRepository.findById(id);
        if (actualiteOptional.isPresent()) {
            actualiteRepository.delete(actualiteOptional.get());
            logger.info("Actualite deleted successfully");
            return true; // Deletion was successful
        }
        logger.error("Error in deleting image");
        return false; // Actualite with the given ID was not found
    }

    public ResponseEntity<ActualiteDTO> changeImageVisibleState(Long id) {
        Actualite actualite = actualiteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        actualite.setActive(!actualite.isActive());
        // Save the updated image back to the repository
        actualite = actualiteRepository.save(actualite);
        logger.info("Actualite N° " + id + " change its visibility");
        // Return a ResponseEntity with the updated image and an HTTP status code
        return ResponseEntity.ok(mapActualiteToDTO(actualite));
    }

    public Actualite mapActualiteDTOToEntity(ActualiteDTO actualiteDTO) {
        Actualite actualite = new Actualite();
        actualite.setId(actualiteDTO.getId());
        actualite.setActive(actualiteDTO.isActive());
        actualite.setTitle(actualiteDTO.getTitle());
        actualite.setText(actualiteDTO.getText());
        actualite.setName(actualiteDTO.getName());
        return actualite;
    }


    public ActualiteDTO mapActualiteToDTO(Actualite actualite) {
        ActualiteDTO dto = new ActualiteDTO();
        dto.setId(actualite.getId());
        dto.setActive(actualite.isActive());
        dto.setText(actualite.getText());
        dto.setTitle(actualite.getTitle());
        dto.setName(actualite.getName());
        return dto;
    }
}
