package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmokingRecords.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "smoking_records";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_COUNT = "count";

    // users 테이블에 대한 컬럼 정의
    private static final String USERS_TABLE = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_AGE = "age";
    private static final String COLUMN_USER_GENDER = "gender";
    private static final String COLUMN_USER_SMOKING_DURATION = "smoking_duration";
    private static final String COLUMN_USER_REGISTRATION_DATE = "registration_date"; // 추가된 컬럼

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // smoking_records 테이블 생성
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_COUNT + " INTEGER)";
        db.execSQL(CREATE_TABLE);

        // users 테이블 생성
        String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_AGE + " INTEGER, " +
                COLUMN_USER_GENDER + " TEXT, " +
                COLUMN_USER_SMOKING_DURATION + " INTEGER," +
                COLUMN_USER_REGISTRATION_DATE + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        // 새 테이블 생성
        onCreate(db);
    }

    // 모든 흡연 기록 불러오기
    public List<SmokingRecord> getAllSmokingRecords() {
        List<SmokingRecord> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_DATE, COLUMN_COUNT},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                int count = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COUNT));
                records.add(new SmokingRecord(date, count));
            }
            cursor.close();
        }
        return records;
    }

    // 날짜별 흡연 개수 불러오기
    public int getSmokingCountByDate(String date) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_COUNT},
                     COLUMN_DATE + "=?", new String[]{date},
                     null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COUNT));
            }
        }
        return 0; // 기본값 0
    }

    // 기록 삽입 또는 업데이트
    public void insertOrUpdateRecord(String date, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_COUNT, count);

        // 중복된 날짜가 있으면 업데이트, 없으면 삽입
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // 모든 기록 삭제 메소드 추가
    public void deleteAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    // 월별 흡연 통계
    public List<SmokingRecord> getMonthlySmokingStatistics() {
        List<SmokingRecord> monthlyStatistics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT strftime('%m월', " + COLUMN_DATE + ") AS month, SUM(" + COLUMN_COUNT + ") AS total_count " +
                "FROM " + TABLE_NAME + " " +
                "GROUP BY month " +
                "ORDER BY month ASC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String month = cursor.getString(cursor.getColumnIndexOrThrow("month"));
                int totalSmokingCount = cursor.getInt(cursor.getColumnIndexOrThrow("total_count"));
                monthlyStatistics.add(new SmokingRecord(month, totalSmokingCount));
            }
            cursor.close();
        }
        return monthlyStatistics;
    }

    // 회원가입 시 사용자의 정보 저장
    public void saveUserInfo(String name, int age, String gender, int smokingDuration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_AGE, age);
        values.put(COLUMN_USER_GENDER, gender);
        values.put(COLUMN_USER_SMOKING_DURATION, smokingDuration);

        long result = db.insert(USERS_TABLE, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Error saving user information");
        } else {
            Log.d("DatabaseHelper", "User information saved successfully");
        }
        db.close();
    }

    // 사용자가 등록되었는지 확인
    public boolean isUserRegistered() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USERS_TABLE + " LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;  // 사용자가 이미 등록됨
        } else {
            cursor.close();
            return false;  // 사용자가 등록되지 않음
        }
    }

    public String getUserName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_NAME + " FROM " + USERS_TABLE + " LIMIT 1"; // 첫 번째 사용자 정보 가져오기
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
            cursor.close();
            return userName;
        }
        cursor.close();
        return null; // 사용자가 없을 경우 null 반환
    }
}

