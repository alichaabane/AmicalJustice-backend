package com.assocation.justice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceDTO {
        private Long id;
        private String name;
        private String title;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean active;
}

