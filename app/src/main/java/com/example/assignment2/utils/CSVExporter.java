package com.example.assignment2;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Course;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CSVExporter {

    private Context context;
    private DatabaseHelper dbHelper;

    public CSVExporter(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Export all data (Students, Faculties, Courses) to CSV files
     * Returns the directory path where files were saved
     */
    public String exportAllData() {
        try {
            // Create export directory
            File exportDir = getExportDirectory();
            if (exportDir == null) {
                return null;
            }

            // Generate timestamp for filenames
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            // Export each table
            boolean studentsExported = exportStudents(exportDir, timestamp);
            boolean facultiesExported = exportFaculties(exportDir, timestamp);
            boolean coursesExported = exportCourses(exportDir, timestamp);

            if (studentsExported && facultiesExported && coursesExported) {
                return exportDir.getAbsolutePath();
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /**
     * Export only students to CSV
     */
    public String exportStudentsOnly() {
        try {
            File exportDir = getExportDirectory();
            if (exportDir == null) return null;

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            if (exportStudents(exportDir, timestamp)) {
                return exportDir.getAbsolutePath();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Export Students table to CSV with creator information
     */
    private boolean exportStudents(File exportDir, String timestamp) {
        try {
            File file = new File(exportDir, "students_" + timestamp + ".csv");
            FileWriter writer = new FileWriter(file);

            // UPDATED HEADER - added Created By
            writer.append("Student ID,Name,Email,Phone,Gender,Faculty ID,Created By Student ID,Created By Student Name\n");

            // Write data
            List<Student> students = dbHelper.getAllStudents();
            for (Student student : students) {
                writer.append(escapeCsv(student.getStudentId())).append(",");
                writer.append(escapeCsv(student.getName())).append(",");
                writer.append(escapeCsv(student.getEmail())).append(",");
                writer.append(escapeCsv(student.getPhone())).append(",");
                writer.append(escapeCsv(student.getGender())).append(",");
                writer.append(String.valueOf(student.getFacultyId())).append(",");

                // Get creator information
                Student creator = dbHelper.getStudent(student.getCreatedBy());
                if (creator != null) {
                    writer.append(escapeCsv(creator.getStudentId())).append(",");
                    writer.append(escapeCsv(creator.getName())).append("\n");
                } else {
                    writer.append("Unknown,").append("Unknown\n");
                }
            }

            writer.flush();
            writer.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Export Faculties table to CSV with creator information
     */
    private boolean exportFaculties(File exportDir, String timestamp) {
        try {
            File file = new File(exportDir, "faculties_" + timestamp + ".csv");
            FileWriter writer = new FileWriter(file);

            // UPDATED HEADER - added Created By
            writer.append("Faculty ID,Faculty Name,Dean Name,Description,Created By Student ID,Created By Student Name\n");

            // Write data
            List<Faculty> faculties = dbHelper.getAllFaculties();
            for (Faculty faculty : faculties) {
                writer.append(String.valueOf(faculty.getFacultyId())).append(",");
                writer.append(escapeCsv(faculty.getFacultyName())).append(",");
                writer.append(escapeCsv(faculty.getDeanName())).append(",");
                writer.append(escapeCsv(faculty.getDescription())).append(",");

                // Get creator information
                Student creator = dbHelper.getStudent(faculty.getCreatedBy());
                if (creator != null) {
                    writer.append(escapeCsv(creator.getStudentId())).append(",");
                    writer.append(escapeCsv(creator.getName())).append("\n");
                } else {
                    writer.append("Unknown,").append("Unknown\n");
                }
            }

            writer.flush();
            writer.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Export Courses table to CSV with creator information
     */
    private boolean exportCourses(File exportDir, String timestamp) {
        try {
            File file = new File(exportDir, "courses_" + timestamp + ".csv");
            FileWriter writer = new FileWriter(file);

            // UPDATED HEADER - added Created By
            writer.append("Course ID,Course Code,Course Name,Credits,Faculty ID,Description,Created By Student ID,Created By Student Name\n");

            // Write data
            List<Course> courses = dbHelper.getAllCourses();
            for (Course course : courses) {
                writer.append(String.valueOf(course.getCourseId())).append(",");
                writer.append(escapeCsv(course.getCourseCode())).append(",");
                writer.append(escapeCsv(course.getCourseName())).append(",");
                writer.append(String.valueOf(course.getCredits())).append(",");
                writer.append(String.valueOf(course.getFacultyId())).append(",");
                writer.append(escapeCsv(course.getDescription())).append(",");

                // Get creator information
                Student creator = dbHelper.getStudent(course.getCreatedBy());
                if (creator != null) {
                    writer.append(escapeCsv(creator.getStudentId())).append(",");
                    writer.append(escapeCsv(creator.getName())).append("\n");
                } else {
                    writer.append("Unknown,").append("Unknown\n");
                }
            }

            writer.flush();
            writer.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get or create export directory
     */
    private File getExportDirectory() {
        File exportDir;

        // For Android 10+ (API 29+), use app-specific directory
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            exportDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "StudentManager_Exports");
        } else {
            // For older versions
            exportDir = new File(Environment.getExternalStorageDirectory(), "StudentManager_Exports");
        }

        if (!exportDir.exists()) {
            if (!exportDir.mkdirs()) {
                Toast.makeText(context, "Failed to create export directory", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        return exportDir;
    }

    /**
     * Escape special characters in CSV
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        // If value contains comma, quote, or newline, wrap in quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    /**
     * Close database helper
     */
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}