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
    }  // Constructor with database ID (for retrieving from database)
    public Faculty(long facultyId, String facultyName, String deanName, String description) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.deanName = deanName;
        this.description = description;
    } // Getters
    public long getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getDeanName() {
        return deanName;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setDeanName(String deanName) {
        this.deanName = deanName;
    }

    public void setDescription(String description) {
        this.description = description;
    } @Override
    public String toString() {
        return facultyName; // Used for Spinner display
    }
}