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
    private long facultyId; // NEW - Foreign Key

    public Student() {
    }

    public Student(String studentId, String name, String email,
                   String phone, String gender, String password, long facultyId) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.facultyId = facultyId;
    }

    public Student(long id, String studentId, String name, String email,
                   String phone, String gender, String password, long facultyId) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.facultyId = facultyId;
    }

    public long getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getPassword() { return password; }
    public long getFacultyId() { return facultyId; }

    public void setId(long id) { this.id = id; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPassword(String password) { this.password = password; }
    public void setFacultyId(long facultyId) { this.facultyId = facultyId; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", facultyId=" + facultyId +
                '}';
    }
}