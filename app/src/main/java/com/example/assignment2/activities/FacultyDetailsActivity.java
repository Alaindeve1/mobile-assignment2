package com.example.assignment2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.R;
import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Course;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Student;
import java.util.ArrayList;
import java.util.List;

public class FacultyDetailsActivity extends AppCompatActivity {

    private TextView tvFacultyName, tvDeanName, tvDescription, tvStudentCount, tvCourseCount;
    private ListView lvStudents, lvCourses;
    private Button btnEditFaculty, btnDeleteFaculty, btnAddStudent, btnAddCourse, btnBack;

    private DatabaseHelper dbHelper;
    private Faculty faculty;
    private long facultyId;
    private List<Student> studentList;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_details);

        dbHelper = new DatabaseHelper(this);

        // Get faculty ID from intent
        Intent intent = getIntent();
        facultyId = intent.getLongExtra("faculty_id", -1);

        if (facultyId == -1) {
            Toast.makeText(this, "Error: Faculty not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        loadFacultyDetails();
        loadStudents();
        loadCourses();
        setupClickListeners();
    }

    private void initializeViews() {
        tvFacultyName = findViewById(R.id.tvFacultyName);
        tvDeanName = findViewById(R.id.tvDeanName);
        tvDescription = findViewById(R.id.tvDescription);
        tvStudentCount = findViewById(R.id.tvStudentCount);
        tvCourseCount = findViewById(R.id.tvCourseCount);
        lvStudents = findViewById(R.id.lvStudents);
        lvCourses = findViewById(R.id.lvCourses);
        btnEditFaculty = findViewById(R.id.btnEditFaculty);
        btnDeleteFaculty = findViewById(R.id.btnDeleteFaculty);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadFacultyDetails() {
        faculty = dbHelper.getFaculty(facultyId);

        if (faculty != null) {
            tvFacultyName.setText(faculty.getFacultyName());
            tvDeanName.setText("Dean: " + faculty.getDeanName());
            tvDescription.setText(faculty.getDescription());
        }
    }

    private void loadStudents() {
        studentList = dbHelper.getStudentsByFaculty(facultyId);
        tvStudentCount.setText("Students: " + studentList.size());

        ArrayList<String> displayList = new ArrayList<>();
        for (Student student : studentList) {
            displayList.add(student.getStudentId() + " - " + student.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        lvStudents.setAdapter(adapter);

        // Click listener for student items
        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student selectedStudent = studentList.get(position);
            navigateToStudentDetails(selectedStudent);
        });
    }

    private void loadCourses() {
        courseList = dbHelper.getCoursesByFaculty(facultyId);
        tvCourseCount.setText("Courses: " + courseList.size());

        ArrayList<String> displayList = new ArrayList<>();
        for (Course course : courseList) {
            displayList.add(course.getCourseCode() + " - " + course.getCourseName() +
                    " (" + course.getCredits() + " credits)");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        lvCourses.setAdapter(adapter);

        // Click listener for course items
        lvCourses.setOnItemClickListener((parent, view, position, id) -> {
            Course selectedCourse = courseList.get(position);
            showCourseDetails(selectedCourse);
        });
    }

    private void setupClickListeners() {
        btnEditFaculty.setOnClickListener(v -> {
            Intent intent = new Intent(FacultyDetailsActivity.this, AddEditFacultyActivity.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("faculty_id", facultyId);
            intent.putExtra("faculty_name", faculty.getFacultyName());
            intent.putExtra("dean_name", faculty.getDeanName());
            intent.putExtra("description", faculty.getDescription());
            startActivity(intent);
        });

        btnDeleteFaculty.setOnClickListener(v -> confirmDeleteFaculty());

        btnAddStudent.setOnClickListener(v -> {
            Toast.makeText(this, "Add Student feature - Navigate to SignUp with faculty preselected",
                    Toast.LENGTH_SHORT).show();
            // You can implement navigation to SignUpActivity with faculty pre-selected
        });

        btnAddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(FacultyDetailsActivity.this, AddEditCourseActivity.class);
            intent.putExtra("mode", "add");
            intent.putExtra("faculty_id", facultyId);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void navigateToStudentDetails(Student student) {
        Intent intent = new Intent(FacultyDetailsActivity.this, UserDetailsActivity.class);
        intent.putExtra("studentId", student.getStudentId());
        intent.putExtra("fullName", student.getName());
        intent.putExtra("email", student.getEmail());
        intent.putExtra("phone", student.getPhone());
        intent.putExtra("gender", student.getGender());
        intent.putExtra("password", student.getPassword());
        intent.putExtra("source", "studentlist");
        startActivity(intent);
    }

    private void showCourseDetails(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(course.getCourseCode() + " - " + course.getCourseName());
        builder.setMessage(
                "Credits: " + course.getCredits() + "\n\n" +
                        "Description: " + course.getDescription()
        );
        builder.setPositiveButton("Edit", (dialog, which) -> {
            Intent intent = new Intent(FacultyDetailsActivity.this, AddEditCourseActivity.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("course_id", course.getCourseId());
            intent.putExtra("course_code", course.getCourseCode());
            intent.putExtra("course_name", course.getCourseName());
            intent.putExtra("credits", course.getCredits());
            intent.putExtra("faculty_id", course.getFacultyId());
            intent.putExtra("description", course.getDescription());
            startActivity(intent);
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void confirmDeleteFaculty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Faculty");
        builder.setMessage("Are you sure you want to delete this faculty?\n\n" +
                "Warning: This will also affect all students and courses linked to this faculty.");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            dbHelper.deleteFaculty(facultyId);
            Toast.makeText(this, "Faculty deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFacultyDetails();
        loadStudents();
        loadCourses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}