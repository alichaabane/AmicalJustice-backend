package com.assocation.justice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Order(3) // Specify the order for table creation
public class Image {
    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    private boolean active;

}
