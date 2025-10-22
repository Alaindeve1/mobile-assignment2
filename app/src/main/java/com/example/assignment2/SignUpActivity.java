package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Student;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword, etPhone;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Spinner spinnerFaculty;
    private CheckBox cbTerms, cbNewsletter;
    private Button btnSignUp;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;

    private List<Faculty> facultyList;
    private ArrayAdapter<Faculty> facultyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        loadFaculties();
        setupClickListeners();
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone); // Make sure this exists in your layout
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        spinnerFaculty = findViewById(R.id.spinnerFaculty);
        cbTerms = findViewById(R.id.cbTerms);
        cbNewsletter = findViewById(R.id.cbNewsletter);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        // Set default phone if field exists
        if (etPhone != null) {
            etPhone.setText("+2507"); // Default Rwanda phone prefix
        }
    }

    private void loadFaculties() {
        facultyList = dbHelper.getAllFaculties();

        if (facultyList == null || facultyList.isEmpty()) {
            Toast.makeText(this, "No faculties available. Please contact administrator.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        facultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, facultyList);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFaculty.setAdapter(facultyAdapter);

        Toast.makeText(this, facultyList.size() + " faculties loaded",
                Toast.LENGTH_SHORT).show();
    }

    private void setupClickListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void handleSignUp() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone != null ? etPhone.getText().toString().trim() : "+250700000000";

        if (!validateInputs(fullName, email, password, confirmPassword, phone)) {
            return;
        }

        String gender = getSelectedGender();

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate faculty selection
        if (spinnerFaculty.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a faculty", Toast.LENGTH_SHORT).show();
            return;
        }

        Faculty selectedFaculty = (Faculty) spinnerFaculty.getSelectedItem();
        long facultyId = selectedFaculty.getFacultyId();

        // Generate student ID
        int studentCount = dbHelper.getStudentCount();
        String studentId = "S" + String.format("%03d", studentCount + 1);

        // For new students, they are created by the first admin (ID = 1)
        // If no admin exists yet, this will be the first student (self-created)
        long createdBy = 1L; // Default to admin ID

        // Create student with created_by field - using the correct constructor
        Student newStudent = new Student(studentId, fullName, email, phone, gender, password, facultyId, createdBy);

        long result = dbHelper.addStudent(newStudent);

        if (result > 0) {
            Toast.makeText(this,
                    "Account created successfully!\nStudent ID: " + studentId +
                            "\nFaculty: " + selectedFaculty.getFacultyName() +
                            "\nPlease use your Student ID to login",
                    Toast.LENGTH_LONG).show();

            // Auto-fill login fields for convenience
            Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
            loginIntent.putExtra("auto_fill_username", studentId);
            startActivity(loginIntent);
            finish();
        } else {
            Toast.makeText(this, "Failed to create account. Student ID might already exist.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInputs(String fullName, String email, String password, String confirmPassword, String phone) {
        if (fullName.isEmpty()) {
            etFullName.setError("Full name cannot be empty");
            etFullName.requestFocus();
            return false;
        }

        if (fullName.length() < 2) {
            etFullName.setError("Full name must be at least 2 characters");
            etFullName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm password");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getSelectedGender() {
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId == rbMale.getId()) {
            return "Male";
        } else if (selectedId == rbFemale.getId()) {
            return "Female";
        }
        return "Not specified";
    }

    private void navigateToLogin() {
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