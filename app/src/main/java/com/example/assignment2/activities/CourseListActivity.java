package com.example.assignment2;

import android.content.DialogInterface;
import com.example.assignment2.models.Student;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Course;
import com.example.assignment2.models.Faculty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseListActivity extends AppCompatActivity {

    private ListView lvCourses;
    private Button btnAddCourse;
    private TextView tvCourseCount;
    private List<Course> courseList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    private long facultyId = -1; // -1 means show all courses
    private String facultyName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        dbHelper = new DatabaseHelper(this);

        // Check if filtering by faculty
        facultyId = getIntent().getLongExtra("faculty_id", -1);
        facultyName = getIntent().getStringExtra("faculty_name");

        if (facultyId != -1 && facultyName != null) {
            setTitle("Courses - " + facultyName);
        } else {
            setTitle("All Courses");
        }

        initializeViews();
        loadCourses();
        setupListView();
        setupClickListeners();
    }

    private void initializeViews() {
        lvCourses = findViewById(R.id.lvCourses);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        tvCourseCount = findViewById(R.id.tvCourseCount);
        courseList = new ArrayList<>();
    }

    private void loadCourses() {
        if (facultyId != -1) {
            // Load courses for specific faculty
            courseList = dbHelper.getCoursesByFaculty(facultyId);
        } else {
            // Load all courses
            courseList = dbHelper.getAllCourses();
        }

        updateCourseCount();
    }

    private void updateCourseCount() {
        String countText = courseList.size() + " course" + (courseList.size() != 1 ? "s" : "") + " available";
        tvCourseCount.setText(countText);
    }

    private void setupListView() {
        ArrayList<String> displayList = new ArrayList<>();

        if (courseList.isEmpty()) {
            displayList.add("No courses available. Tap + to add one.");
        } else {
            // If showing all courses, include faculty name
            if (facultyId == -1) {
                // Use JOIN query to get courses with faculty names and creator info
                List<Map<String, String>> coursesWithFaculty = dbHelper.getCoursesWithFacultyNameAndCreator();

                for (Map<String, String> courseMap : coursesWithFaculty) {
                    String display = courseMap.get("course_code") + " - " +
                            courseMap.get("course_name") + "\n" +
                            "Faculty: " + courseMap.get("faculty_name") + " | " +
                            courseMap.get("credits") + " credits" + "\n" +
                            "Created by: " + courseMap.get("creator_name");
                    displayList.add(display);
                }
            } else {
                // Just show course info (faculty is already known)
                for (Course course : courseList) {
                    // Get creator information
                    Student creator = dbHelper.getStudent(course.getCreatedBy());
                    String creatorName = (creator != null) ? creator.getName() : "Unknown";

                    String display = course.getCourseCode() + " - " + course.getCourseName() + "\n" +
                            course.getCredits() + " credits" + "\n" +
                            "Created by: " + creatorName;
                    displayList.add(display);
                }
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvCourses.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // Add course button
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseListActivity.this, AddEditCourseActivity.class);

                // If viewing courses for specific faculty, pass faculty_id
                if (facultyId != -1) {
                    intent.putExtra("faculty_id", facultyId);
                }

                // Pass student_id for created_by functionality
                String studentId = getIntent().getStringExtra("student_id");
                if (studentId != null) {
                    intent.putExtra("student_id", studentId);
                }

                startActivity(intent);
            }
        });

        // Course item click - show options (Edit/Delete)
        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (courseList.isEmpty()) {
                    return; // Don't do anything if list is empty
                }

                Course selectedCourse = courseList.get(position);
                showCourseOptionsDialog(selectedCourse);
            }
        });

        // Long click for delete
        lvCourses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (courseList.isEmpty()) {
                    return true;
                }

                Course selectedCourse = courseList.get(position);
                showDeleteConfirmationDialog(selectedCourse);
                return true;
            }
        });
    }

    private void showCourseOptionsDialog(final Course course) {
        // Get faculty name for this course
        Faculty faculty = dbHelper.getFaculty(course.getFacultyId());
        String facultyName = (faculty != null) ? faculty.getFacultyName() : "Unknown";

        // Get creator information
        Student creator = dbHelper.getStudent(course.getCreatedBy());
        String creatorName = (creator != null) ? creator.getName() : "Unknown";
        String creatorId = (creator != null) ? creator.getStudentId() : "Unknown";

        String message = "Course Code: " + course.getCourseCode() + "\n" +
                "Course Name: " + course.getCourseName() + "\n" +
                "Credits: " + course.getCredits() + "\n" +
                "Faculty: " + facultyName + "\n" +
                "Created by: " + creatorName + " (" + creatorId + ")" + "\n" +
                "Description: " + (course.getDescription() != null ? course.getDescription() : "N/A");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Course Details");
        builder.setMessage(message);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editCourse(course);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDeleteConfirmationDialog(course);
            }
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void editCourse(Course course) {
        Intent intent = new Intent(CourseListActivity.this, AddEditCourseActivity.class);
        intent.putExtra("course_id", course.getCourseId());

        // Pass student_id for created_by functionality
        String studentId = getIntent().getStringExtra("student_id");
        if (studentId != null) {
            intent.putExtra("student_id", studentId);
        }

        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(final Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Course");
        builder.setMessage("Are you sure you want to delete '" + course.getCourseName() + "'?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCourse(course);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteCourse(Course course) {
        dbHelper.deleteCourse(course.getCourseId());
        Toast.makeText(this, "Course deleted: " + course.getCourseName(), Toast.LENGTH_SHORT).show();

        // Reload the list
        loadCourses();
        setupListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload courses when returning from AddEditCourseActivity
        loadCourses();
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