package com.example.assignment2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.assignment2.models.Student;
import com.example.assignment2.models.Faculty;
import com.example.assignment2.models.Course;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB.db";
    private static final int DATABASE_VERSION = 2;
    // FACULTIES TABLE
    private static final String TABLE_FACULTIES = "faculties";
    private static final String COL_FACULTY_ID = "faculty_id";
    private static final String COL_FACULTY_NAME = "faculty_name";
    private static final String COL_DEAN_NAME = "dean_name";
    private static final String COL_FACULTY_DESC = "description";

    // STUDENTS TABLE
    private static final String TABLE_STUDENTS = "students";
    private static final String COL_STUDENT_ID = "_id";
    private static final String COL_STUDENT_NUMBER = "student_id";
    private static final String COL_STUDENT_NAME = "name";
    private static final String COL_STUDENT_EMAIL = "email";
    private static final String COL_STUDENT_PHONE = "phone";
    private static final String COL_STUDENT_GENDER = "gender";
    private static final String COL_STUDENT_PASSWORD = "password";
    private static final String COL_STUDENT_FACULTY_ID = "faculty_id";

    // COURSES TABLE
    private static final String TABLE_COURSES = "courses";
    private static final String COL_COURSE_ID = "course_id";
    private static final String COL_COURSE_CODE = "course_code";
    private static final String COL_COURSE_NAME = "course_name";
    private static final String COL_COURSE_CREDITS = "credits";
    private static final String COL_COURSE_FACULTY_ID = "faculty_id";
    private static final String COL_COURSE_DESC = "description";
