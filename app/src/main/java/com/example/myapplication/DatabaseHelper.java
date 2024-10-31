package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmokingRecords.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "smoking_records";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_COUNT = "count";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_COUNT + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 날짜별 흡연 개수 불러오기
    public int getSmokingCountByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_COUNT},
                COLUMN_DATE + "=?", new String[]{date},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COUNT));
            cursor.close();
            return count;
        }
        if (cursor != null) {
            cursor.close();
        }
        return 0; // 기본값 0
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

}
