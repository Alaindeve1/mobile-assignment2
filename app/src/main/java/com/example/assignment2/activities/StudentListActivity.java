package com.example.assignment2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment2.R;
import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private ListView lvStudents;
    private Spinner spinnerFacultyFilter;
    private Button btnClearFilter;
    private ArrayList<Student> studentList;
    private ArrayAdapter<String> adapter;
    private TextView tvBackToLogin, tvStudentCount;
    private DatabaseHelper dbHelper;

    private List<Faculty> facultyList;
    private ArrayAdapter<Faculty> facultyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        initializeViews();

        // Load faculties for filter
        loadFacultiesForFilter();

        // Load student data from DATABASE
        loadStudentData();

        // Set up ListView with adapter
        setupListView();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        lvStudents = findViewById(R.id.lvStudents);
        spinnerFacultyFilter = findViewById(R.id.spinnerFacultyFilter);
        btnClearFilter = findViewById(R.id.btnClearFilter);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvStudentCount = findViewById(R.id.tvStudentCount);
        studentList = new ArrayList<>();

        // Check if views were found
        if (spinnerFacultyFilter == null) {
            Log.e("StudentListActivity", "spinnerFacultyFilter not found!");
        }
        if (btnClearFilter == null) {
            Log.e("StudentListActivity", "btnClearFilter not found!");
        }
        if (tvStudentCount == null) {
            Log.e("StudentListActivity", "tvStudentCount not found!");
        }
    }

    private void loadFacultiesForFilter() {
        facultyList = new ArrayList<>();

        // Add "All Faculties" option - use the constructor without facultyId for the dummy option
        Faculty allOption = new Faculty("All Faculties", "", "", 1L);
        facultyList.add(allOption);

        // Add actual faculties from database
        List<Faculty> dbFaculties = dbHelper.getAllFaculties();
        if (dbFaculties != null) {
            facultyList.addAll(dbFaculties);
        }

        facultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, facultyList);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (spinnerFacultyFilter != null) {
            spinnerFacultyFilter.setAdapter(facultyAdapter);
        }
    }

    private void loadStudentData() {
        // Load from DATABASE instead of dummy data
        studentList.clear();
        List<Student> dbStudents = dbHelper.getAllStudents();
        if (dbStudents != null) {
            studentList.addAll(dbStudents);
        }

        Toast.makeText(this, studentList.size() + " students loaded from database",
                Toast.LENGTH_SHORT).show();

        updateStudentCount();
    }

    private void loadStudentsByFaculty(long facultyId) {
        studentList.clear();
        if (facultyId == 0) {
            // Load all students
            List<Student> dbStudents = dbHelper.getAllStudents();
            if (dbStudents != null) {
                studentList.addAll(dbStudents);
            }
        } else {
            // Load students by faculty
            List<Student> facultyStudents = dbHelper.getStudentsByFaculty(facultyId);
            if (facultyStudents != null) {
                studentList.addAll(facultyStudents);
            }
        }

        updateStudentCount();
        setupListView();
    }

    private void updateStudentCount() {
        if (tvStudentCount != null) {
            tvStudentCount.setText("Total Students: " + studentList.size());
        }
    }

    private void setupListView() {
        // Create a list of strings to display in ListView (ID and Name)
        ArrayList<String> displayList = new ArrayList<>();
        for (Student student : studentList) {
            // Get creator information
            Student creator = dbHelper.getStudent(student.getCreatedBy());
            String creatorName = (creator != null) ? creator.getName() : "Unknown";

            displayList.add(student.getStudentId() + " - " + student.getName() +
                    "\nCreated by: " + creatorName);
        }

        // Create and set adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvStudents.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // ListView item click listener - Navigate to Student Details
        lvStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < studentList.size()) {
                    // Get selected student
                    Student selectedStudent = studentList.get(position);

                    // Show toast
                    Toast.makeText(StudentListActivity.this,
                            "Selected: " + selectedStudent.getName(),
                            Toast.LENGTH_SHORT).show();

                    // Navigate to UserDetailsActivity with Explicit Intent
                    navigateToStudentDetails(selectedStudent);
                }
            }
        });

        // Faculty filter spinner
        if (spinnerFacultyFilter != null) {
            spinnerFacultyFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position < facultyList.size()) {
                        Faculty selectedFaculty = facultyList.get(position);
                        // For "All Faculties" option (facultyId = 0), load all students
                        if (position == 0) {
                            loadStudentData();
                        } else {
                            loadStudentsByFaculty(selectedFaculty.getFacultyId());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });
        }

        // Clear filter button
        if (btnClearFilter != null) {
            btnClearFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (spinnerFacultyFilter != null) {
                        spinnerFacultyFilter.setSelection(0); // Select "All Faculties"
                    }
                    loadStudentData();
                    setupListView();
                }
            });
        }

        // Back to login link
        if (tvBackToLogin != null) {
            tvBackToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToLogin();
                }
            });
        }
    }

    private void navigateToStudentDetails(Student student) {
        // Create explicit intent to UserDetailsActivity
        Intent intent = new Intent(StudentListActivity.this, UserDetailsActivity.class);

        // Pass student data via Intent
        intent.putExtra("studentId", student.getStudentId());
        intent.putExtra("fullName", student.getName());
        intent.putExtra("email", student.getEmail());
        intent.putExtra("phone", student.getPhone());
        intent.putExtra("gender", student.getGender());
        intent.putExtra("password", student.getPassword());
        intent.putExtra("facultyId", student.getFacultyId());
        intent.putExtra("source", "studentlist");

        // Start the activity
        startActivity(intent);
    }

    private void navigateToLogin() {
        // Go back to login
        Intent intent = new Intent(StudentListActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadStudentData();
        setupListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}