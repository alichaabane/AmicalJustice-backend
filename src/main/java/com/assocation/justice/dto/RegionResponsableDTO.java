package com.assocation.justice.dto;

import com.assocation.justice.util.enumeration.CategoryResponsable;
import com.assocation.justice.util.enumeration.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionResponsableDTO {
    private Long id;
    private String nom;
    private String longitude;
    private String latitude;
    private Region region;
    private String fax;
    private List<ResponsableDTO> responsables;

    // Getters and setters (or use Lombok annotations like @Data)
}
