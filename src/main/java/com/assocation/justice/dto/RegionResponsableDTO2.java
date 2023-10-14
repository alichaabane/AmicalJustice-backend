package com.assocation.justice.dto;

import com.assocation.justice.util.enumeration.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionResponsableDTO2 {
    private Long id;
    private String nom;
    private String longitude;
    private String latitude;
    private Region region;
    private String fax;

    // Getters and setters (or use Lombok annotations like @Data)
}