package com.example.assignment2.models;

import java.io.Serializable;

/**
 * Course Model Class
 * Represents a course offered by a faculty
 */
public class Course implements Serializable {

    private long courseId;           // Database ID (auto-generated)
    private String courseCode;       // Course code (e.g., "CS101")
    private String courseName;       // Course name (e.g., "Introduction to Programming")
    private int credits;             // Number of credits (default: 3)
    private long facultyId;          // Foreign key to Faculty table
    private String description;      // Course description
    private long createdBy;          // Student who created this course

    // Empty Constructor
    public Course() {
    }

    // Constructor without database ID (for creating new course)
    public Course(String courseCode, String courseName, int credits,
                  long facultyId, String description, long createdBy) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.facultyId = facultyId;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Constructor with database ID (for retrieving from database)
    public Course(long courseId, String courseCode, String courseName,
                  int credits, long facultyId, String description, long createdBy) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.facultyId = facultyId;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Getters
    public long getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public long getFacultyId() {
        return facultyId;
    }

    public String getDescription() {
        return description;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    // Setters
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName; // Used for display
    }
}