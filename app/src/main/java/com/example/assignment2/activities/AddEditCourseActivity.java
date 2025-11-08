package com.example.assignment2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.R;
import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Course;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Student;
import java.util.ArrayList;
import java.util.List;

public class AddEditCourseActivity extends AppCompatActivity {

    private EditText etCourseCode, etCourseName, etCredits, etDescription;
    private Spinner spinnerFaculty;
    private Button btnSave, btnCancel;
    private DatabaseHelper dbHelper;

    private List<Faculty> facultyList;
    private boolean isEditMode = false;
    private long courseId = -1;
    private String currentStudentId; // Store logged-in student ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        dbHelper = new DatabaseHelper(this);

        // Get logged-in student ID from intent
        currentStudentId = getIntent().getStringExtra("student_id");

        initializeViews();
        loadFaculties();
        checkEditMode();
        setupClickListeners();
    }

    private void initializeViews() {
        etCourseCode = findViewById(R.id.etCourseCode);
        etCourseName = findViewById(R.id.etCourseName);
        etCredits = findViewById(R.id.etCredits);
        etDescription = findViewById(R.id.etDescription);
        spinnerFaculty = findViewById(R.id.spinnerFaculty);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadFaculties() {
        facultyList = dbHelper.getAllFaculties();

        if (facultyList.isEmpty()) {
            Toast.makeText(this, "Please add faculties first!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Create list of faculty names for spinner
        ArrayList<String> facultyNames = new ArrayList<>();
        for (Faculty faculty : facultyList) {
            facultyNames.add(faculty.getFacultyName());
        }

        // Set up spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                facultyNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFaculty.setAdapter(adapter);
    }

    private void checkEditMode() {
        // Check if we're editing an existing course
        courseId = getIntent().getLongExtra("course_id", -1);

        if (courseId != -1) {
            isEditMode = true;
            setTitle("Edit Course");
            loadCourseData();
        } else {
            isEditMode = false;
            setTitle("Add New Course");

            // Pre-select faculty if passed from FacultyDetailsActivity
            long preselectedFacultyId = getIntent().getLongExtra("faculty_id", -1);
            if (preselectedFacultyId != -1) {
                preselectFaculty(preselectedFacultyId);
            }
        }
    }

    private void loadCourseData() {
        Course course = dbHelper.getCourseById(courseId);

        if (course != null) {
            etCourseCode.setText(course.getCourseCode());
            etCourseName.setText(course.getCourseName());
            etCredits.setText(String.valueOf(course.getCredits()));
            etDescription.setText(course.getDescription());

            // Select the correct faculty in spinner
            preselectFaculty(course.getFacultyId());
        } else {
            Toast.makeText(this, "Failed to load course data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void preselectFaculty(long facultyId) {
        for (int i = 0; i < facultyList.size(); i++) {
            if (facultyList.get(i).getFacultyId() == facultyId) {
                spinnerFaculty.setSelection(i);
                break;
            }
        }
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourse();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveCourse() {
        // Get input values
        String courseCode = etCourseCode.getText().toString().trim().toUpperCase();
        String courseName = etCourseName.getText().toString().trim();
        String creditsStr = etCredits.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(courseCode, courseName, creditsStr)) {
            return;
        }

        int credits = Integer.parseInt(creditsStr);

        // Get selected faculty
        int selectedPosition = spinnerFaculty.getSelectedItemPosition();
        long facultyId = facultyList.get(selectedPosition).getFacultyId();

        // Get current student ID for created_by field
        long createdBy = getCurrentStudentId();

        // Create Course object
        Course course;

        if (isEditMode) {
            // Update existing course - use the constructor with createdBy
            course = new Course(courseId, courseCode, courseName, credits, facultyId, description, createdBy);
            int rowsAffected = dbHelper.updateCourse(course);

            if (rowsAffected > 0) {
                Toast.makeText(this, "Course updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update course", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new course - use the constructor with createdBy
            course = new Course(courseCode, courseName, credits, facultyId, description, createdBy);
            long id = dbHelper.addCourse(course);

            if (id > 0) {
                Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add course. Course code may already exist.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Get the current logged-in student's database ID for the created_by field
     */
    private long getCurrentStudentId() {
        if (currentStudentId != null && !currentStudentId.isEmpty()) {
            Student currentStudent = dbHelper.getStudentByStudentId(currentStudentId);
            if (currentStudent != null) {
                return currentStudent.getId();
            }
        }
        return 1; // Fallback to default ID if no student is logged in
    }

    private boolean validateInputs(String courseCode, String courseName, String creditsStr) {
        // Validate course code
        if (courseCode.isEmpty()) {
            etCourseCode.setError("Course code is required");
            etCourseCode.requestFocus();
            return false;
        }

        if (courseCode.length() < 3) {
            etCourseCode.setError("Course code must be at least 3 characters");
            etCourseCode.requestFocus();
            return false;
        }

        // Validate course name
        if (courseName.isEmpty()) {
            etCourseName.setError("Course name is required");
            etCourseName.requestFocus();
            return false;
        }

        if (courseName.length() < 3) {
            etCourseName.setError("Course name must be at least 3 characters");
            etCourseName.requestFocus();
            return false;
        }

        // Validate credits
        if (creditsStr.isEmpty()) {
            etCredits.setError("Credits are required");
            etCredits.requestFocus();
            return false;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            if (credits < 1 || credits > 10) {
                etCredits.setError("Credits must be between 1 and 10");
                etCredits.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etCredits.setError("Please enter a valid number");
            etCredits.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}