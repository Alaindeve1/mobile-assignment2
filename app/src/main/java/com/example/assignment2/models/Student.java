package com.example.assignment2.models;

import java.io.Serializable;

public class Student implements Serializable {

    private long id;
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String password;
    private long facultyId;
    private long createdBy;

    public Student() {
    }

    // Constructor for new students (without database ID)
    public Student(String studentId, String name, String email,
                   String phone, String gender, String password,
                   long facultyId, long createdBy) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.facultyId = facultyId;
        this.createdBy = createdBy;
    }

    // Constructor with database ID (for retrieving from database)
    public Student(long id, String studentId, String name, String email,
                   String phone, String gender, String password,
                   long facultyId, long createdBy) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.facultyId = facultyId;
        this.createdBy = createdBy;
    }

    // Getters and Setters (make sure you have all of these)
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public long getFacultyId() { return facultyId; }
    public void setFacultyId(long facultyId) { this.facultyId = facultyId; }

    public long getCreatedBy() { return createdBy; }
    public void setCreatedBy(long createdBy) { this.createdBy = createdBy; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", facultyId=" + facultyId +
                ", createdBy=" + createdBy +
                '}';
    }
}