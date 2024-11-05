package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

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

        // 성별 스피너에 데이터 추가
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString(); // 입력한 이름 가져오기
                int age = Integer.parseInt(editTextAge.getText().toString()); // 나이 가져오기
                String gender = spinnerGender.getSelectedItem().toString(); // 성별 가져오기
                int smokingDuration = Integer.parseInt(editTextSmokingDuration.getText().toString()); // 흡연 기간 가져오기

                // 사용자 정보 저장
                databaseHelper.insertUser(name, age, gender, smokingDuration);

                // 이름을 Calendar Activity로 전달
                Intent intent = new Intent(SignUpActivity.this, CalendarActivity.class);
                intent.putExtra("userName", name); // 이름 전달
                startActivity(intent);
                finish(); // 현재 Activity 종료
            }
        });
    }
}
