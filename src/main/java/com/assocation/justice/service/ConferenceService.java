package com.assocation.justice.service;

import com.assocation.justice.dto.ConferenceDTO;
import com.assocation.justice.dto.PageRequestData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConferenceService {
    ConferenceDTO saveConference(MultipartFile file, ConferenceDTO newConferenceDTO);

    ConferenceDTO getConferenceById(Long id);

    PageRequestData<ConferenceDTO> getAllConferencesPaginated(PageRequest pageRequest);
    List<ConferenceDTO> getAllConferences();

    ConferenceDTO updateConference(Long conferenceId, MultipartFile file, ConferenceDTO updatedConferenceDTO);

    ResponseEntity<ConferenceDTO> changeConferenceVisibleState(Long id);

    boolean deleteConference(Long id);
}
