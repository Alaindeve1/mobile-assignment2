package com.example.assignment2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STUDENTS = "students";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_NEWSLETTER = "newsletter";
    // Create table query
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FULL_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_GENDER + " TEXT,"
            + KEY_NEWSLETTER + " INTEGER DEFAULT 0"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // Add a new student
    public long addStudent(String fullName, String email, String password, String gender, boolean newsletter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FULL_NAME, fullName);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_GENDER, gender);
        values.put(KEY_NEWSLETTER, newsletter ? 1 : 0);

        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return id;
    }
    // Check if student exists
    public boolean checkStudent(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID};
        String selection = KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(
                TABLE_STUDENTS,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // Check if email exists
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID};
        String selection = KEY_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                TABLE_STUDENTS,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // Get student by email
    public Student getStudentByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID, KEY_FULL_NAME, KEY_EMAIL, KEY_GENDER, KEY_NEWSLETTER};
        String selection = KEY_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                TABLE_STUDENTS,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = new Student(
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                    "", // Don't return password for security
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_GENDER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NEWSLETTER)) == 1
            );
            student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            cursor.close();
        }
        db.close();
        return student;
    }
}

