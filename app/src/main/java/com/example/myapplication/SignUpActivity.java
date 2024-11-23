package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName, editTextAge, editTextSmokingDuration;
    private Spinner spinnerGender;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextSmokingDuration = findViewById(R.id.editTextSmokingDuration);
        spinnerGender = findViewById(R.id.spinnerGender);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        // 데이터베이스 헬퍼 초기화
        databaseHelper = new DatabaseHelper(this);

        // 성별 스피너에 데이터 추가
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        buttonSignUp.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            int age = Integer.parseInt(editTextAge.getText().toString());
            String gender = spinnerGender.getSelectedItem().toString();
            int smokingDuration = Integer.parseInt(editTextSmokingDuration.getText().toString());

            // 사용자 정보를 데이터베이스에 저장
            databaseHelper.saveUserInfo(name, age, gender, smokingDuration);

            // 금연 시작 날짜 저장
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            databaseHelper.saveSmokingStartDate(today);

            // CalendarActivity로 이동
            Intent intent = new Intent(SignUpActivity.this, CalendarActivity.class);
            intent.putExtra("userName", name); // 이름 전달
            startActivity(intent);
            finish(); // 현재 Activity 종료
        });
    }
}
