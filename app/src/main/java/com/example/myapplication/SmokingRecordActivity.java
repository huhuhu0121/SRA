package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SmokingRecordActivity extends AppCompatActivity {

    private EditText smokingCountEditText;
    private TextView dateTextView;
    private String currentDate;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoking_record);

        dateTextView = findViewById(R.id.dateTextView);
        smokingCountEditText = findViewById(R.id.smokingCountEditText);
        Button saveButton = findViewById(R.id.saveButton);
        ImageButton increaseButton = findViewById(R.id.increaseButton);
        ImageButton decreaseButton = findViewById(R.id.decreaseButton);
        ImageButton backButton = findViewById(R.id.backButton);

        databaseHelper = new DatabaseHelper(this);

        // 현재 날짜 설정
        currentDate = getIntent().getStringExtra("selectedDate");
        dateTextView.setText("날짜: " + currentDate);

        // SQLite에서 기존 흡연 개수 불러오기
        int smokingCount = databaseHelper.getSmokingCountByDate(currentDate);
        smokingCountEditText.setText(String.valueOf(smokingCount));

        // 증가 버튼
        increaseButton.setOnClickListener(v -> {
            int currentCount = Integer.parseInt(smokingCountEditText.getText().toString());
            currentCount++;
            smokingCountEditText.setText(String.valueOf(currentCount));
        });

        // 감소 버튼
        decreaseButton.setOnClickListener(v -> {
            int currentCount = Integer.parseInt(smokingCountEditText.getText().toString());
            if (currentCount > 0) {
                currentCount--;
            }
            smokingCountEditText.setText(String.valueOf(currentCount));
        });

        // 저장 버튼
        saveButton.setOnClickListener(v -> {
            int countToSave = Integer.parseInt(smokingCountEditText.getText().toString());
            // SQLite에 흡연 개수 저장
            databaseHelper.insertOrUpdateRecord(currentDate, countToSave);
            finish(); // 이전 화면으로 돌아가기
        });

        backButton.setOnClickListener(v -> finish()); // 뒤로가기 버튼 클릭 시
    }
}
