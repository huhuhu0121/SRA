package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        TextView textViewGreeting = findViewById(R.id.textViewGreeting);
        CalendarView calendarView = findViewById(R.id.calendarView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView); // BottomNavigationView 추가

        // DatabaseHelper 인스턴스 생성
        databaseHelper = new DatabaseHelper(this);

        // 사용자 이름을 데이터베이스에서 가져오기
        String userName = databaseHelper.getUserName();

        if (userName != null && !userName.isEmpty()) {
            // 인사 메시지 설정
            textViewGreeting.setText(userName + "님 안녕하세요! \n오늘은 금연 1일차입니다.");
        } else {
            textViewGreeting.setText("안녕하세요! 오늘은 금연 1일차입니다.");
        }

        // 날짜 선택 시 이벤트 처리
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

            // 다음 액티비티로 넘어갈 Intent 생성
            Intent intent = new Intent(CalendarActivity.this, SmokingRecordActivity.class);
            intent.putExtra("selectedDate", selectedDate); // 선택한 날짜 전달
            intent.putExtra("userName", userName);         // 사용자 이름 전달

            // SmokingRecordActivity 실행
            startActivity(intent);
        });

        // BottomNavigationView의 아이템 클릭 리스너 설정
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_reset) {
                    databaseHelper.deleteAllRecords(); // 모든 기록 삭제
                    Toast.makeText(CalendarActivity.this, "모든 기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.action_statistics) {
                    Intent statisticsIntent = new Intent(CalendarActivity.this, StatisticsActivity.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (itemId == R.id.action_rewards) {
                    Intent rewardIntent = new Intent(CalendarActivity.this, RewardsActivity.class);
                    startActivity(rewardIntent);
                    return true;
                }
                return false;
            }
        });
    }
}
