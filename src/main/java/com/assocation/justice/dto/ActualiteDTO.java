package com.assocation.justice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualiteDTO {
    private Long id;
    private String name;
    private String title;
    private String text;
    private boolean active;
    // Constructors if needed
}

