package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {

    private ListView lvStudents;
    private ArrayList<Student> studentList;
    private ArrayAdapter<String> adapter;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_list);

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        initializeViews();

        // Load dummy student data
        loadStudentData();

        // Set up ListView with adapter
        setupListView();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        lvStudents = findViewById(R.id.lvStudents);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        studentList = new ArrayList<>();
    }

    private void loadStudentData() {
        // Creating dummy student data for demonstration
        studentList.add(new Student("S001", "John Doe", "john.doe@auca.ac.rw", "+250788123456", "Male", "password123"));
        studentList.add(new Student("S002", "Jane Smith", "jane.smith@auca.ac.rw", "+250788234567", "Female", "password123"));
        studentList.add(new Student("S003", "Michael Brown", "michael.brown@auca.ac.rw", "+250788345678", "Male", "password123"));
        studentList.add(new Student("S004", "Emily Davis", "emily.davis@auca.ac.rw", "+250788456789", "Female", "password123"));
        studentList.add(new Student("S005", "David Wilson", "david.wilson@auca.ac.rw", "+250788567890", "Male", "password123"));
        studentList.add(new Student("S006", "Sarah Johnson", "sarah.johnson@auca.ac.rw", "+250788678901", "Female", "password123"));
        studentList.add(new Student("S007", "James Martinez", "james.martinez@auca.ac.rw", "+250788789012", "Male", "password123"));
        studentList.add(new Student("S008", "Lisa Anderson", "lisa.anderson@auca.ac.rw", "+250788890123", "Female", "password123"));

        Toast.makeText(this, studentList.size() + " students loaded", Toast.LENGTH_SHORT).show();
    }

    private void setupListView() {
        // Create a list of strings to display in ListView (ID and Name)
        ArrayList<String> displayList = new ArrayList<>();
        for (Student student : studentList) {
            displayList.add(student.getStudentId() + " - " + student.getName());
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
                // Get selected student
                Student selectedStudent = studentList.get(position);

                // Show toast
                Toast.makeText(StudentListActivity.this,
                        "Selected: " + selectedStudent.getName(),
                        Toast.LENGTH_SHORT).show();

                // Navigate to UserDetailsActivity with Explicit Intent
                navigateToStudentDetails(selectedStudent);
            }
        });

        // Back to login link
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
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
}