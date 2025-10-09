package com.example.assignment2.models;

import java.io.Serializable;

/**
 * Student Model Class
 * Represents a student entity with all properties
 */
public class Student implements Serializable {

    private long id;              // Database ID (auto-generated)
    private String studentId;     // Student ID (S001, S002, etc.)
    private String name;          // Full name
    private String email;         // Email address
    private String phone;         // Phone number
    private String gender;        // Male/Female
    private String password;      // Password

    // Empty Constructor
    public Student() {
    }
