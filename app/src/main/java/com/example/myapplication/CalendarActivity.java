package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        TextView textViewGreeting = findViewById(R.id.textViewGreeting);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Button statisticsButton = findViewById(R.id.statisticsButton);
        Button deleteRecordsButton = findViewById(R.id.deleteRecordsButton); // 추가된 버튼

        // DatabaseHelper 인스턴스 생성
        databaseHelper = new DatabaseHelper(this);

        // Intent로부터 이름 가져오기
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");

        if (userName != null && !userName.isEmpty()) {
            // 인사 메시지 설정
            textViewGreeting.setText(userName + "님 안녕하세요! \n 오늘은 금연 O일차입니다.");
        } else {
            textViewGreeting.setText("안녕하세요! 오늘은 금연 O일차입니다.");
        }

        // 날짜 선택 시 이벤트 처리
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                // 다음 액티비티로 넘어갈 Intent 생성
                Intent intent = new Intent(CalendarActivity.this, SmokingRecordActivity.class);
                intent.putExtra("selectedDate", selectedDate); // 선택한 날짜 전달
                intent.putExtra("userName", userName);         // 사용자 이름 전달

                // SmokingRecordActivity 실행
                startActivity(intent);
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        // 기록 삭제 버튼 클릭 시
        deleteRecordsButton.setOnClickListener(v -> {
            databaseHelper.deleteAllRecords(); // 모든 기록 삭제
            Toast.makeText(CalendarActivity.this, "모든 기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        });
    }
}
