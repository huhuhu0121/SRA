package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        // 앱 실행 시 데이터베이스에서 사용자 정보 확인
        try {
            if (databaseHelper.isUserRegistered()) {
                // 이미 가입한 사용자라면 CalendarActivity로 이동
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish(); // MainActivity 종료
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonSignUp.setOnClickListener(v -> {
            // 회원가입 화면으로 이동
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
