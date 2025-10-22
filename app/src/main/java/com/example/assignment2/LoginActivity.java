package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Student;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        // Create a default student if database is empty
        createDefaultStudentIfNeeded();

        initializeViews();
        setupClickListeners();
    }

    private void createDefaultStudentIfNeeded() {
        int studentCount = dbHelper.getStudentCount();
        if (studentCount == 0) {
            // Create a default admin student using the correct constructor
            Student defaultStudent = new Student(
                    "ADMIN001",
                    "Admin User",
                    "admin@auca.ac.rw",
                    "+250788888888",
                    "Male",
                    "admin123",
                    1L,  // facultyId (General Studies)
                    1L   // createdBy (self)
            );

            long id = dbHelper.addStudent(defaultStudent);
            if (id != -1) {
                Toast.makeText(this, "Default admin created: ADMIN001/admin123", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to create default admin", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUp();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Please contact admin to reset password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return;
        }

        // DEBUG: Check total students
        int totalStudents = dbHelper.getStudentCount();
        Toast.makeText(this, "Total students in DB: " + totalStudents, Toast.LENGTH_SHORT).show();

        // Try to get student from database
        Student student = dbHelper.getStudentByStudentId(username);

        if (student == null) {
            // Show available student IDs for debugging
            StringBuilder availableIds = new StringBuilder("Available IDs: ");
            java.util.List<Student> allStudents = dbHelper.getAllStudents();
            for (Student s : allStudents) {
                availableIds.append(s.getStudentId()).append(", ");
            }
            Toast.makeText(this, "User '" + username + "' not found. " + availableIds.toString(), Toast.LENGTH_LONG).show();
            etUsername.requestFocus();
            return;
        }

        // Check password
        if (student.getPassword().equals(password)) {
            Toast.makeText(this, "Welcome back, " + student.getName() + "!", Toast.LENGTH_SHORT).show();
            navigateToDashboard(student.getStudentId(), student.getName());
        } else {
            Toast.makeText(this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            etPassword.requestFocus();
        }
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboard(String studentId, String studentName) {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("student_id", studentId);
        intent.putExtra("username", studentName);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}