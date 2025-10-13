package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.DatabaseHelper;
import com.example.assignment2.models.Faculty;
import java.util.ArrayList;
import java.util.List;

public class FacultyListActivity extends AppCompatActivity {

    private ListView lvFaculties;
    private Button btnAddFaculty;
    private List<Faculty> facultyList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);

        dbHelper = new DatabaseHelper(this);

        lvFaculties = findViewById(R.id.lvFaculties);
        btnAddFaculty = findViewById(R.id.btnAddFaculty);

        loadFaculties();
        setupListView();

        btnAddFaculty.setOnClickListener(v -> {
            Intent intent = new Intent(FacultyListActivity.this, AddEditFacultyActivity.class);
            startActivity(intent);
        });

        lvFaculties.setOnItemClickListener((parent, view, position, id) -> {
            Faculty selectedFaculty = facultyList.get(position);
            Intent intent = new Intent(FacultyListActivity.this, FacultyDetailsActivity.class);
            intent.putExtra("faculty_id", selectedFaculty.getFacultyId());
            intent.putExtra("faculty_name", selectedFaculty.getFacultyName());
            startActivity(intent);
        });
    }

    private void loadFaculties() {
        facultyList = dbHelper.getAllFaculties();
        Toast.makeText(this, facultyList.size() + " faculties loaded", Toast.LENGTH_SHORT).show();
    }

    private void setupListView() {
        ArrayList<String> displayList = new ArrayList<>();
        for (Faculty faculty : facultyList) {
            displayList.add(faculty.getFacultyName() + "\nDean: " + faculty.getDeanName());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvFaculties.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFaculties();
        setupListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) dbHelper.close();
    }
}