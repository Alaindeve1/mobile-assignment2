package com.example.assignment2.models;

import java.io.Serializable;

/**
 * Faculty Model Class
 * Represents a faculty/department in the university
 */
public class Faculty implements Serializable {

    private long facultyId;           // Database ID (auto-generated)
    private String facultyName;       // Name of faculty (e.g., "Computer Science")
    private String deanName;          // Dean's name
    private String description;       // Faculty description

    // Empty Constructor
    public Faculty() {
    }

    // Constructor without database ID (for creating new faculty)
    public Faculty(String facultyName, String deanName, String description) {
        this.facultyName = facultyName;
        this.deanName = deanName;
        this.description = description;
    }