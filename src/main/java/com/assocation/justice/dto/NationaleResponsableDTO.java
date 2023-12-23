package com.assocation.justice.dto;

import com.assocation.justice.util.enumeration.CategoryResponsable;
import com.assocation.justice.util.enumeration.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NationaleResponsableDTO {
    private Long id;
    private String nom;
    private String telephone;
    private String nationaleResponsableEmail;
    private String nationaleResponsableJob;
    private String categoryNationaleResponsable;
    // Getters and setters (or use Lombok annotations like @Data)
}
