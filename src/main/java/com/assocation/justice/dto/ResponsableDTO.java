package com.assocation.justice.dto;

import com.assocation.justice.util.enumeration.CategoryResponsable;
import com.assocation.justice.util.enumeration.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsableDTO {
    private Long id;
    private String nom;
    private String telephone;
    private String responsableEmail;
    private String responsableJob;
    private CategoryResponsable categoryResponsable;
    private Region regionResponsableCity;
    private String regionResponsableName;

    // Getters and setters (or use Lombok annotations like @Data)
}
