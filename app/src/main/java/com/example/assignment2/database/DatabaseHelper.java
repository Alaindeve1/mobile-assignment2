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
    // CREATE TABLE QUERIES
    private static final String CREATE_TABLE_FACULTIES =
            "CREATE TABLE " + TABLE_FACULTIES + " (" +
                    COL_FACULTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_FACULTY_NAME + " TEXT NOT NULL, " +
                    COL_DEAN_NAME + " TEXT, " +
                    COL_FACULTY_DESC + " TEXT" +
                    ")";

    private static final String CREATE_TABLE_STUDENTS =
            "CREATE TABLE " + TABLE_STUDENTS + " (" +
                    COL_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_STUDENT_NUMBER + " TEXT UNIQUE NOT NULL, " +
                    COL_STUDENT_NAME + " TEXT NOT NULL, " +
                    COL_STUDENT_EMAIL + " TEXT, " +
                    COL_STUDENT_PHONE + " TEXT, " +
                    COL_STUDENT_GENDER + " TEXT, " +
                    COL_STUDENT_PASSWORD + " TEXT, " +
                    COL_STUDENT_FACULTY_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COL_STUDENT_FACULTY_ID + ") REFERENCES " +
                    TABLE_FACULTIES + "(" + COL_FACULTY_ID + ")" +
                    ")";

    private static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COL_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_COURSE_CODE + " TEXT UNIQUE NOT NULL, " +
                    COL_COURSE_NAME + " TEXT NOT NULL, " +
                    COL_COURSE_CREDITS + " INTEGER DEFAULT 3, " +
                    COL_COURSE_FACULTY_ID + " INTEGER NOT NULL, " +
                    COL_COURSE_DESC + " TEXT, " +
                    "FOREIGN KEY(" + COL_COURSE_FACULTY_ID + ") REFERENCES " +
                    TABLE_FACULTIES + "(" + COL_FACULTY_ID + ")" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FACULTIES);
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_COURSES);

        // Add default faculty so students can be created
        ContentValues defaultFaculty = new ContentValues();
        defaultFaculty.put(COL_FACULTY_NAME, "General Studies");
        defaultFaculty.put(COL_DEAN_NAME, "Academic Office");
        defaultFaculty.put(COL_FACULTY_DESC, "Default faculty for new students");
        db.insert(TABLE_FACULTIES, null, defaultFaculty);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACULTIES);
        onCreate(db);
    }
    // ==================== FACULTY CRUD ====================

    public long addFaculty(Faculty faculty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FACULTY_NAME, faculty.getFacultyName());
        values.put(COL_DEAN_NAME, faculty.getDeanName());
        values.put(COL_FACULTY_DESC, faculty.getDescription());

        long id = db.insert(TABLE_FACULTIES, null, values);
        db.close();
        return id;
    }

    public List<Faculty> getAllFaculties() {
        List<Faculty> facultyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FACULTIES + " ORDER BY " + COL_FACULTY_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                facultyList.add(cursorToFaculty(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return facultyList;
    }

    public Faculty getFaculty(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FACULTIES, null, COL_FACULTY_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Faculty faculty = null;
        if (cursor != null && cursor.moveToFirst()) {
            faculty = cursorToFaculty(cursor);
            cursor.close();
        }
        db.close();
        return faculty;
    }

    public int updateFaculty(Faculty faculty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FACULTY_NAME, faculty.getFacultyName());
        values.put(COL_DEAN_NAME, faculty.getDeanName());
        values.put(COL_FACULTY_DESC, faculty.getDescription());

        int rows = db.update(TABLE_FACULTIES, values, COL_FACULTY_ID + " = ?",
                new String[]{String.valueOf(faculty.getFacultyId())});
        db.close();
        return rows;
    }

    public void deleteFaculty(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FACULTIES, COL_FACULTY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    // ==================== COURSE CRUD ====================

    public long addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_COURSE_CODE, course.getCourseCode());
        values.put(COL_COURSE_NAME, course.getCourseName());
        values.put(COL_COURSE_CREDITS, course.getCredits());
        values.put(COL_COURSE_FACULTY_ID, course.getFacultyId());
        values.put(COL_COURSE_DESC, course.getDescription());

        long id = db.insert(TABLE_COURSES, null, values);
        db.close();
        return id;
    }

    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COURSES + " ORDER BY " + COL_COURSE_CODE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                courseList.add(cursorToCourse(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courseList;
    }
    public Course getCourseById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, null, COL_COURSE_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Course course = null;
        if (cursor != null && cursor.moveToFirst()) {
            course = cursorToCourse(cursor);
            cursor.close();
        }
        db.close();
        return course;
    }

    public List<Course> getCoursesByFaculty(long facultyId) {
        List<Course> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, null, COL_COURSE_FACULTY_ID + " = ?",
                new String[]{String.valueOf(facultyId)}, null, null, COL_COURSE_NAME);

        if (cursor.moveToFirst()) {
            do {
                courseList.add(cursorToCourse(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courseList;
    }

    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_COURSE_CODE, course.getCourseCode());
        values.put(COL_COURSE_NAME, course.getCourseName());
        values.put(COL_COURSE_CREDITS, course.getCredits());
        values.put(COL_COURSE_FACULTY_ID, course.getFacultyId());
        values.put(COL_COURSE_DESC, course.getDescription());

        int rows = db.update(TABLE_COURSES, values, COL_COURSE_ID + " = ?",
                new String[]{String.valueOf(course.getCourseId())});
        db.close();
        return rows;
    }

    public void deleteCourse(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURSES, COL_COURSE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    // ==================== STUDENT CRUD ====================

    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NUMBER, student.getStudentId());
        values.put(COL_STUDENT_NAME, student.getName());
        values.put(COL_STUDENT_EMAIL, student.getEmail());
        values.put(COL_STUDENT_PHONE, student.getPhone());
        values.put(COL_STUDENT_GENDER, student.getGender());
        values.put(COL_STUDENT_PASSWORD, student.getPassword());
        values.put(COL_STUDENT_FACULTY_ID, student.getFacultyId());

        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return id;
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COL_STUDENT_NUMBER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                studentList.add(cursorToStudent(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    public List<Student> getStudentsByFaculty(long facultyId) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, COL_STUDENT_FACULTY_ID + " = ?",
                new String[]{String.valueOf(facultyId)}, null, null, COL_STUDENT_NAME);

        if (cursor.moveToFirst()) {
            do {
                studentList.add(cursorToStudent(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    public Student getStudent(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, COL_STUDENT_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = cursorToStudent(cursor);
            cursor.close();
        }
        db.close();
        return student;
    }

    public Student getStudentByStudentId(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, COL_STUDENT_NUMBER + " = ?",
                new String[]{studentId}, null, null, null);

        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = cursorToStudent(cursor);
            cursor.close();
        }
        db.close();
        return student;
    }

    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NUMBER, student.getStudentId());
        values.put(COL_STUDENT_NAME, student.getName());
        values.put(COL_STUDENT_EMAIL, student.getEmail());
        values.put(COL_STUDENT_PHONE, student.getPhone());
        values.put(COL_STUDENT_GENDER, student.getGender());
        values.put(COL_STUDENT_PASSWORD, student.getPassword());
        values.put(COL_STUDENT_FACULTY_ID, student.getFacultyId());

        int rows = db.update(TABLE_STUDENTS, values, COL_STUDENT_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
        db.close();
        return rows;
    }

    public void deleteStudent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, COL_STUDENT_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getStudentCount() {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, new String[]{COL_STUDENT_ID},
                COL_STUDENT_NUMBER + " = ? AND " + COL_STUDENT_PASSWORD + " = ?",
                new String[]{username, password}, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    // ==================== JOIN QUERIES ====================

    public Map<String, String> getStudentWithFaculty(String studentId) {
        Map<String, String> result = new HashMap<>();

        String query = "SELECT s.*, f." + COL_FACULTY_NAME + " " +
                "FROM " + TABLE_STUDENTS + " s " +
                "INNER JOIN " + TABLE_FACULTIES + " f " +
                "ON s." + COL_STUDENT_FACULTY_ID + " = f." + COL_FACULTY_ID + " " +
                "WHERE s." + COL_STUDENT_NUMBER + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{studentId});

        if (cursor.moveToFirst()) {
            result.put("student_id", cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NUMBER)));
            result.put("name", cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)));
            result.put("email", cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_EMAIL)));
            result.put("phone", cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_PHONE)));
            result.put("gender", cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_GENDER)));
            result.put("faculty_name", cursor.getString(cursor.getColumnIndexOrThrow(COL_FACULTY_NAME)));
        }

        cursor.close();
        db.close();
        return result;
    }

    public List<Map<String, String>> getFacultiesWithStudentCount() {
        List<Map<String, String>> resultList = new ArrayList<>();

        String query = "SELECT f." + COL_FACULTY_ID + ", f." + COL_FACULTY_NAME + ", " +
                "f." + COL_DEAN_NAME + ", COUNT(s." + COL_STUDENT_ID + ") as student_count " +
                "FROM " + TABLE_FACULTIES + " f " +
                "LEFT JOIN " + TABLE_STUDENTS + " s " +
                "ON f." + COL_FACULTY_ID + " = s." + COL_STUDENT_FACULTY_ID + " " +
                "GROUP BY f." + COL_FACULTY_ID + " " +
                "ORDER BY f." + COL_FACULTY_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("faculty_id", cursor.getString(0));
                map.put("faculty_name", cursor.getString(1));
                map.put("dean_name", cursor.getString(2));
                map.put("student_count", cursor.getString(3));
                resultList.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;
    }

    public List<Map<String, String>> getCoursesWithFacultyName() {
        List<Map<String, String>> resultList = new ArrayList<>();

        String query = "SELECT c.*, f." + COL_FACULTY_NAME + " " +
                "FROM " + TABLE_COURSES + " c " +
                "INNER JOIN " + TABLE_FACULTIES + " f " +
                "ON c." + COL_COURSE_FACULTY_ID + " = f." + COL_FACULTY_ID + " " +
                "ORDER BY c." + COL_COURSE_CODE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("course_code", cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_CODE)));
                map.put("course_name", cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_NAME)));
                map.put("credits", cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_CREDITS)));
                map.put("faculty_name", cursor.getString(cursor.getColumnIndexOrThrow(COL_FACULTY_NAME)));
                resultList.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;
    }

    // ==================== HELPER METHODS ====================

    private Faculty cursorToFaculty(Cursor cursor) {
        return new Faculty(
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_FACULTY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FACULTY_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DEAN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FACULTY_DESC))
        );
    }

    private Course cursorToCourse(Cursor cursor) {
        return new Course(
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_COURSE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_CODE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_COURSE_CREDITS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_COURSE_FACULTY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_DESC))
        );
    }

    private Student cursorToStudent(Cursor cursor) {
        return new Student(
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NUMBER)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_GENDER)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_PASSWORD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_FACULTY_ID))
        );
    }
}


