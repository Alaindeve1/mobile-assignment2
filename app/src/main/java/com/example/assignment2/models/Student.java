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

    // Constructor without database ID (for creating new students)
    public Student(String studentId, String name, String email,
                   String phone, String gender, String password) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
    }

    // Constructor with database ID (for retrieving from database)
    public Student(long id, String studentId, String name, String email,
                   String phone, String gender, String password) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
    } // Getters
    public long getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getPassword() { return password; }
    // Setters
    public void setId(long id) { this.id = id; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}