package com.assocation.justice.service.impl;

import com.assocation.justice.dto.ConferenceDTO;
import com.assocation.justice.dto.PageRequestData;
import com.assocation.justice.entity.Conference;
import com.assocation.justice.repository.ConferenceRepository;
import com.assocation.justice.service.ConferenceService;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConferenceServiceImpl implements ConferenceService {
    private final Logger logger = LoggerFactory.getLogger(ConferenceServiceImpl.class);
    private final ConferenceRepository conferenceRepository;

    @Value("${app.conference.dir}")
    private String uploadConferenceDir;

    @Autowired
    public ConferenceServiceImpl(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    @Override
    @Transactional
    public ConferenceDTO saveConference(MultipartFile file, ConferenceDTO newConferenceDTO) {
        try {
            String fileName = newConferenceDTO.getName() != null ? newConferenceDTO.getName() : file.getOriginalFilename();
            assert fileName != null;
            fileName = fileName.replaceAll("\\s+", "_").trim();

            Path copyLocation = Paths.get(uploadConferenceDir + File.separator + StringUtils.cleanPath(Objects.requireNonNull(fileName)));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            // Set the filename in the DTO
            newConferenceDTO.setName(fileName);
            newConferenceDTO.setCreatedAt(LocalDateTime.now());
            newConferenceDTO.setUpdatedAt(LocalDateTime.now());

            // Save the image information to the database
            Conference saveConferenceToDatabase = saveConferenceToDatabase(newConferenceDTO);

            // Map the saved image to DTO for response
            return mapConferenceToDTO(saveConferenceToDatabase);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the file", e);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving classpath resource", e);
        }
    }

    private Conference saveConferenceToDatabase(ConferenceDTO conferenceDTO) {
        Conference conference = new Conference();
        conference.setId(conferenceDTO.getId());
        conference.setName(conferenceDTO.getName());
        conference.setTitle(conferenceDTO.getTitle());
        conference.setActive(conferenceDTO.isActive());
        conference.setCreatedAt(LocalDateTime.now());
        conference.setUpdatedAt(LocalDateTime.now());
        return conferenceRepository.save(conference);
    }

    public ConferenceDTO mapConferenceToDTO(Conference conference) {
        ConferenceDTO dto = new ConferenceDTO();
        dto.setId(conference.getId());
        dto.setActive(conference.isActive());
        dto.setTitle(conference.getTitle());
        dto.setName(conference.getName());
        dto.setCreatedAt(conference.getCreatedAt());
        dto.setUpdatedAt(conference.getUpdatedAt());
        return dto;
    }

    @Override
    public ConferenceDTO getConferenceById(Long id) {
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conference not found with ID: " + id));
        logger.info("Conference fetched successfully");
        return mapConferenceToDTO(conference);
    }

    @Override
    public List<ConferenceDTO> getAllConferences() {
        List<Conference> conferences = conferenceRepository.findAll();
        List<ConferenceDTO> conferenceDTOS = conferences.stream()
                .map(this::mapConferenceToDTO)
                .collect(Collectors.toList());
        return conferenceDTOS;
    }

    @Override
    public PageRequestData<ConferenceDTO> getAllConferencesPaginated(PageRequest pageRequest) {
        logger.info("Conference list fetched successfully");
        Page<Conference> conferencePage = conferenceRepository.findAll(pageRequest);
        PageRequestData<ConferenceDTO> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(conferencePage.map(this::mapConferenceToDTO).getContent());
        customPageResponse.setTotalPages(conferencePage.getTotalPages());
        customPageResponse.setTotalElements(conferencePage.getTotalElements());
        customPageResponse.setNumber(conferencePage.getNumber());
        customPageResponse.setSize(conferencePage.getSize());

        return customPageResponse;
    }

    @Override
    public ConferenceDTO updateConference(Long conferenceId, MultipartFile file, ConferenceDTO updatedConferenceDTO) {
        Conference existingConference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));
        try {
            if (file != null) {
                // A new file is provided, update the file
                Files.createDirectories(Path.of(uploadConferenceDir));

                String fileName = updatedConferenceDTO.getName() != null ? updatedConferenceDTO.getName() : file.getOriginalFilename();
                assert fileName != null;
                fileName = fileName.replaceAll("\\s+", "_").trim() ;

                Path filePath = Path.of(uploadConferenceDir, fileName);

                // Check if the existing file should be deleted
                if (existingConference.getName() != null && !existingConference.getName().isEmpty()) {
                    Path existingFilePath = Path.of(uploadConferenceDir, existingConference.getName());
                    Files.deleteIfExists(existingFilePath);
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingConference.setName(fileName);
            }

            // Update other fields
            existingConference.setTitle(updatedConferenceDTO.getTitle());
            existingConference.setActive(updatedConferenceDTO.isActive());
            existingConference.setUpdatedAt(LocalDateTime.now());

            Conference updatedConference = conferenceRepository.save(existingConference);

            return mapConferenceToDTO(updatedConference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update the file", e);
        }
    }

    @Override
    public ResponseEntity<ConferenceDTO> changeConferenceVisibleState(Long id) {
        Conference conference = conferenceRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        conference.setActive(!conference.isActive());
        // Save the updated image back to the repository
        conference = conferenceRepository.save(conference);
        logger.info("Actualite N° " + id + " change its visibility");
        // Return a ResponseEntity with the updated image and an HTTP status code
        return ResponseEntity.ok(mapConferenceToDTO(conference));
    }

    @Override
    @Transactional
    public boolean deleteConference(Long id) {
        Optional<Conference> actualiteOptional = conferenceRepository.findById(id);
        if (actualiteOptional.isPresent()) {
            conferenceRepository.delete(actualiteOptional.get());
            logger.info("Conference deleted successfully");
            return true; // Deletion was successful
        }
        logger.error("Error in deleting Conference");
        return false; // Conference with the given ID was not found
    }
}
