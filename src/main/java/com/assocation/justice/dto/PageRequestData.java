package com.assocation.justice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageRequestData<T> {
    private List<T> content; // Represents the list of items on the current page
    private int totalPages; // Total number of pages
    private long totalElements; // Total number of elements
    private int number; // Current page number
    private int size; // Number of elements per page

    // Constructors, getters, setters, and other methods as needed
}
