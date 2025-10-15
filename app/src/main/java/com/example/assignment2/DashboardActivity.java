package com.example.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Student;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcomeUser, tvDateTime, tvQuickStats;
    private CardView cardStudentList, cardMyProfile, cardAboutApp, cardSettings;
    private CardView cardEmergencyContacts, cardAcademicCalendar;
    private CardView cardFacultyManagement, cardCourseManagement;
    private Button btnLogout;
    private CardView cardExportData; // Add this line with other CardView declarations

    private String studentId; // Store logged-in student ID
    private String username;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        // Get logged-in user data from intent
        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        username = intent.getStringExtra("username");

        if (username == null || username.isEmpty()) {
            username = "Guest";
        }

        initializeViews();
        setupDateTime();
        updateQuickStats();
        setupClickListeners();
    }

    private void initializeViews() {
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvQuickStats = findViewById(R.id.tvQuickStats);
        cardStudentList = findViewById(R.id.cardStudentList);
        cardMyProfile = findViewById(R.id.cardMyProfile);
        cardAboutApp = findViewById(R.id.cardAboutApp);
        cardSettings = findViewById(R.id.cardSettings);
        cardEmergencyContacts = findViewById(R.id.cardEmergencyContacts);
        cardAcademicCalendar = findViewById(R.id.cardAcademicCalendar);
        cardFacultyManagement = findViewById(R.id.cardFacultyManagement);
        cardCourseManagement = findViewById(R.id.cardCourseManagement);
        cardExportData = findViewById(R.id.cardExportData);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcomeUser.setText("Welcome, " + username + "!");
    }

    private void setupDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy - hh:mm a", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        tvDateTime.setText(currentDateTime);
    }

    private void updateQuickStats() {
        int studentCount = dbHelper.getStudentCount();
        int facultyCount = dbHelper.getAllFaculties().size();
        int courseCount = dbHelper.getAllCourses().size();

        tvQuickStats.setText("👥 Students: " + studentCount + " | 🏛️ Faculties: " +
                facultyCount + " | 📚 Courses: " + courseCount);
    }

    private void setupClickListeners() {
        // Student List Card
        cardStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Opening Student List...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, StudentListActivity.class);
                startActivity(intent);
            }
        });

        // My Profile Card - UPDATED to use database
        cardMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentId == null || studentId.isEmpty()) {
                    Toast.makeText(DashboardActivity.this, "Please login to view profile", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get logged-in student data from database
                Student loggedInStudent = dbHelper.getStudentByStudentId(studentId);

                if (loggedInStudent != null) {
                    Toast.makeText(DashboardActivity.this, "Opening Your Profile...", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DashboardActivity.this, UserDetailsActivity.class);
                    intent.putExtra("source", "myprofile");
                    intent.putExtra("student_db_id", loggedInStudent.getId());
                    intent.putExtra("studentId", loggedInStudent.getStudentId());
                    intent.putExtra("fullName", loggedInStudent.getName());
                    intent.putExtra("email", loggedInStudent.getEmail());
                    intent.putExtra("phone", loggedInStudent.getPhone());
                    intent.putExtra("gender", loggedInStudent.getGender());
                    intent.putExtra("password", loggedInStudent.getPassword());
                    intent.putExtra("facultyId", loggedInStudent.getFacultyId());

                    startActivity(intent);
                } else {
                    Toast.makeText(DashboardActivity.this, "Error loading profile data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Faculty Management Card
        cardFacultyManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Opening Faculty Management...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, FacultyListActivity.class);
                startActivity(intent);
            }
        });

        // Course Management Card
        cardCourseManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Opening Course Management...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        // About App Card
        cardAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        // Settings Card
        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Settings feature coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        // Emergency Contacts Card
        cardEmergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmergencyContacts();
            }
        });

        // Academic Calendar Card
        cardAcademicCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAcademicCalendar();
            }
        });
        cardExportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExportDialog();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

    }

    private void showAboutDialog() {
        Toast.makeText(this,
                "Student Manager v2.0\n" +
                        "Assignment #4 - Relational Data\n" +
                        "Developed for AUCA",
                Toast.LENGTH_LONG).show();
    }

    private void showEmergencyContacts() {
        Toast.makeText(this, "Emergency Contacts Available", Toast.LENGTH_SHORT).show();
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:112"));
        try {
            startActivity(dialIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open dialer", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAcademicCalendar() {
        Toast.makeText(this, "Opening AUCA Academic Calendar...", Toast.LENGTH_SHORT).show();
        String url = "https://www.auca.ac.rw/academics/academic-calendar";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open browser", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogout() {
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateQuickStats();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use logout button to exit", Toast.LENGTH_SHORT).show();
    }
    private void showExportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Export Data to CSV");
        builder.setMessage("Choose what to export:");

        builder.setPositiveButton("All Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exportAllData();
            }
        });

        builder.setNegativeButton("Students Only", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exportStudentsOnly();
            }
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void exportAllData() {
        Toast.makeText(this, "Exporting all data...", Toast.LENGTH_SHORT).show();

        CSVExporter exporter = new CSVExporter(this);
        String exportPath = exporter.exportAllData();
        exporter.close();

        if (exportPath != null) {
            showExportSuccessDialog(exportPath, "All data (Students, Faculties, Courses)");
        } else {
            Toast.makeText(this, "Export failed!", Toast.LENGTH_LONG).show();
        }
    }

    private void exportStudentsOnly() {
        Toast.makeText(this, "Exporting students...", Toast.LENGTH_SHORT).show();

        CSVExporter exporter = new CSVExporter(this);
        String exportPath = exporter.exportStudentsOnly();
        exporter.close();

        if (exportPath != null) {
            showExportSuccessDialog(exportPath, "Students");
        } else {
            Toast.makeText(this, "Export failed!", Toast.LENGTH_LONG).show();
        }
    }

    private void showExportSuccessDialog(String path, String dataType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✅ Export Successful!");
        builder.setMessage(dataType + " exported successfully!\n\nLocation:\n" + path +
                "\n\nYou can find the CSV files in your device's Documents folder.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}