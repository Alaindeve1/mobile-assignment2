package com.example.assignment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Student;

public class UserDetailsActivity extends AppCompatActivity {

    private TextView tvWelcome, tvUserInfo, tvSource, tvFacultyName;
    private TextView tvStudentId, tvFullName, tvEmail, tvPhone, tvGender;
    private Button btnLogout, btnEditProfile, btnDelete;
    private TextView tvAucaLink;

    private DatabaseHelper dbHelper;
    private Student currentStudent;
    private boolean isMyProfile = false;
    private long studentDbId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        initializeViews();
        displayUserInfo();
        setupClickListeners();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        tvSource = findViewById(R.id.tvSource);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvGender = findViewById(R.id.tvGender);
        tvFacultyName = findViewById(R.id.tvFacultyName);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnDelete = findViewById(R.id.btnDelete);
        tvAucaLink = findViewById(R.id.tvAucaLink);
    }

    private void displayUserInfo() {
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");

        if ("myprofile".equals(source)) {
            // My Profile - data from Dashboard
            isMyProfile = true;
            studentDbId = intent.getLongExtra("student_db_id", -1);

            loadStudentFromDatabase(studentDbId);

            tvWelcome.setText("My Profile");
            tvUserInfo.setText("Your account information");
            tvSource.setText("Source: My Profile");

            btnEditProfile.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);

        } else if ("studentlist".equals(source)) {
            // Student Details from StudentListActivity
            isMyProfile = false;

            String studentId = intent.getStringExtra("studentId");
            currentStudent = dbHelper.getStudentByStudentId(studentId);

            if (currentStudent != null) {
                studentDbId = currentStudent.getId();
                displayStudentData();

                tvWelcome.setText("Student Details");
                tvUserInfo.setText("View and manage student information");
                tvSource.setText("Source: Student List");

                btnEditProfile.setVisibility(View.VISIBLE);
                btnEditProfile.setText("Edit Student");
                btnDelete.setVisibility(View.VISIBLE);

                makeEmailClickable();
                makePhoneClickable();
            }

        } else if ("signup".equals(source)) {
            // From SignUp - Display the data passed
            String studentId = intent.getStringExtra("studentId");
            String fullName = intent.getStringExtra("fullName");
            String email = intent.getStringExtra("email");
            String gender = intent.getStringExtra("gender");
            boolean newsletter = intent.getBooleanExtra("newsletter", false);

            tvWelcome.setText("Welcome!");
            tvUserInfo.setText("Account created successfully");
            tvSource.setText("Source: Sign Up");

            // Display the data
            tvStudentId.setText("Student ID: " + studentId);
            tvStudentId.setVisibility(View.VISIBLE);

            tvFullName.setText("Full Name: " + fullName);
            tvFullName.setVisibility(View.VISIBLE);

            tvEmail.setText("Email: " + email);
            tvEmail.setVisibility(View.VISIBLE);

            tvGender.setText("Gender: " + gender);
            tvGender.setVisibility(View.VISIBLE);

            // Hide fields not available from signup
            tvPhone.setVisibility(View.GONE);
            tvFacultyName.setVisibility(View.GONE);

            // Hide action buttons for signup display
            btnEditProfile.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void loadStudentFromDatabase(long dbId) {
        currentStudent = dbHelper.getStudent(dbId);

        if (currentStudent != null) {
            displayStudentData();
        } else {
            Toast.makeText(this, "Error loading student data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayStudentData() {
        tvStudentId.setText("Student ID: " + currentStudent.getStudentId());
        tvFullName.setText("Full Name: " + currentStudent.getName());
        tvEmail.setText("📧 Email: " + currentStudent.getEmail());
        tvPhone.setText("📞 Phone: " + currentStudent.getPhone());
        tvGender.setText("Gender: " + currentStudent.getGender());

        // Get and display faculty name
        Faculty faculty = dbHelper.getFaculty(currentStudent.getFacultyId());
        if (faculty != null) {
            tvFacultyName.setText("Faculty: " + faculty.getFacultyName());
            tvFacultyName.setVisibility(View.VISIBLE);
        } else {
            tvFacultyName.setVisibility(View.GONE);
        }

        // Make all fields visible
        tvStudentId.setVisibility(View.VISIBLE);
        tvFullName.setVisibility(View.VISIBLE);
        tvEmail.setVisibility(View.VISIBLE);
        tvPhone.setVisibility(View.VISIBLE);
        tvGender.setVisibility(View.VISIBLE);
    }

    private void makeEmailClickable() {
        tvEmail.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvEmail.setClickable(true);
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(currentStudent.getEmail());
            }
        });
    }

    private void makePhoneClickable() {
        tvPhone.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvPhone.setClickable(true);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(currentStudent.getPhone());
            }
        });
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmation();
            }
        });

        tvAucaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAucaWebsite();
            }
        });
    }

    private void showEditDialog() {
        if (currentStudent == null) {
            Toast.makeText(this, "No student data to edit", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Student");

        // Create custom dialog layout
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_student, null);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.etEditName);
        final EditText etEmail = dialogView.findViewById(R.id.etEditEmail);
        final EditText etPhone = dialogView.findViewById(R.id.etEditPhone);

        // Pre-fill with current data
        etName.setText(currentStudent.getName());
        etEmail.setText(currentStudent.getEmail());
        etPhone.setText(currentStudent.getPhone());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = etName.getText().toString().trim();
                String newEmail = etEmail.getText().toString().trim();
                String newPhone = etPhone.getText().toString().trim();

                if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
                    Toast.makeText(UserDetailsActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update student data
                currentStudent.setName(newName);
                currentStudent.setEmail(newEmail);
                currentStudent.setPhone(newPhone);

                int rowsAffected = dbHelper.updateStudent(currentStudent);

                if (rowsAffected > 0) {
                    Toast.makeText(UserDetailsActivity.this, "Student updated successfully!", Toast.LENGTH_SHORT).show();
                    displayStudentData(); // Refresh display
                } else {
                    Toast.makeText(UserDetailsActivity.this, "Failed to update student", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmation() {
        if (currentStudent == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Student");
        builder.setMessage("Are you sure you want to delete " + currentStudent.getName() + "?\n\nThis action cannot be undone.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteStudent(currentStudent.getId());
                Toast.makeText(UserDetailsActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello from Student Manager");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void makePhoneCall(String phone) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phone));

        try {
            startActivity(dialIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No phone app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAucaWebsite() {
        String url = "https://www.auca.ac.rw";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        try {
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No browser found", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogout() {
        Intent intent = new Intent(UserDetailsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
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