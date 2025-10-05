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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcomeUser, tvDateTime, tvQuickStats;
    private CardView cardStudentList, cardMyProfile, cardAboutApp, cardSettings;
    private CardView cardEmergencyContacts, cardAcademicCalendar;
    private Button btnLogout;

    private String username;

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

        // Get username from intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = "Guest";
        }

        initializeViews();
        setupDateTime();
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
        btnLogout = findViewById(R.id.btnLogout);

        // Set welcome message
        tvWelcomeUser.setText("Welcome, " + username + "!");

        // Set quick stats
        tvQuickStats.setText("📊 Total Students: 8 | 👥 Active Users: 5");
    }

    private void setupDateTime() {
        // Display current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy - hh:mm a", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        tvDateTime.setText(currentDateTime);
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

        // My Profile Card
        cardMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Opening Your Profile...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, UserDetailsActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("source", "login");
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


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });
    }

    private void showAboutDialog() {
        Toast.makeText(this,
                "Instagram Clone v1.0\n" +
                        "Mobile Programming Assignment\n" +
                        "Developed for AUCA",
                Toast.LENGTH_LONG).show();
    }

    private void showEmergencyContacts() {
        // Show emergency contact options
        Toast.makeText(this, "Emergency Contacts Available", Toast.LENGTH_SHORT).show();

        // Open dialer with emergency number
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:112")); // Emergency number

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
    public void onBackPressed() {
        Toast.makeText(this, "Use logout button to exit", Toast.LENGTH_SHORT).show();
    }
}