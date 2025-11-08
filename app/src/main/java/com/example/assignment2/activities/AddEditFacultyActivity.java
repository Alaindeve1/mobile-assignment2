package com.example.assignment2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.R;
import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Student;

public class AddEditFacultyActivity extends AppCompatActivity {

    private EditText etFacultyName, etDeanName, etDescription;
    private Button btnSave, btnCancel;
    private DatabaseHelper dbHelper;

    private boolean isEditMode = false;
    private long facultyId = -1;
    private String currentStudentId; // Store logged-in student ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_faculty);

        dbHelper = new DatabaseHelper(this);

        // Get logged-in student ID from intent
        currentStudentId = getIntent().getStringExtra("student_id");

        initializeViews();
        checkEditMode();
        setupClickListeners();
    }

    private void initializeViews() {
        etFacultyName = findViewById(R.id.etFacultyName);
        etDeanName = findViewById(R.id.etDeanName);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void checkEditMode() {
        // Check if we're editing an existing faculty
        facultyId = getIntent().getLongExtra("faculty_id", -1);

        if (facultyId != -1) {
            isEditMode = true;
            setTitle("Edit Faculty");
            loadFacultyData();
        } else {
            isEditMode = false;
            setTitle("Add New Faculty");
        }
    }

    private void loadFacultyData() {
        Faculty faculty = dbHelper.getFaculty(facultyId);

        if (faculty != null) {
            etFacultyName.setText(faculty.getFacultyName());
            etDeanName.setText(faculty.getDeanName());
            etDescription.setText(faculty.getDescription());
        } else {
            Toast.makeText(this, "Failed to load faculty data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFaculty();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveFaculty() {
        // Get input values
        String facultyName = etFacultyName.getText().toString().trim();
        String deanName = etDeanName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(facultyName, deanName)) {
            return;
        }

        // Get current student ID for created_by field
        long createdBy = getCurrentStudentId();

        // Create Faculty object
        Faculty faculty;

        if (isEditMode) {
            // Update existing faculty - use the constructor with createdBy
            faculty = new Faculty(facultyId, facultyName, deanName, description, createdBy);
            int rowsAffected = dbHelper.updateFaculty(faculty);

            if (rowsAffected > 0) {
                Toast.makeText(this, "Faculty updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update faculty", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new faculty - use the constructor with createdBy
            faculty = new Faculty(facultyName, deanName, description, createdBy);
            long id = dbHelper.addFaculty(faculty);

            if (id > 0) {
                Toast.makeText(this, "Faculty added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add faculty", Toast.LENGTH_SHORT).show();
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

    private boolean validateInputs(String facultyName, String deanName) {
        // Validate faculty name
        if (facultyName.isEmpty()) {
            etFacultyName.setError("Faculty name is required");
            etFacultyName.requestFocus();
            return false;
        }

        if (facultyName.length() < 3) {
            etFacultyName.setError("Faculty name must be at least 3 characters");
            etFacultyName.requestFocus();
            return false;
        }

        // Validate dean name
        if (deanName.isEmpty()) {
            etDeanName.setError("Dean name is required");
            etDeanName.requestFocus();
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