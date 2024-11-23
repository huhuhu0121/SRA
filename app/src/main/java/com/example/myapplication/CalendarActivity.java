package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CalendarActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView motivationalTextView;
    private Handler handler;
    private Runnable runnable;
    private TextView textViewDaysCount;
    private FrameLayout badgeSlot1, badgeSlot2, badgeSlot3, badgeSlot4;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        motivationalTextView = findViewById(R.id.motivationalTextView);
        TextView textViewGreeting = findViewById(R.id.textViewDaysCount);
        CalendarView calendarView = findViewById(R.id.calendarView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView); // BottomNavigationView 추가

        // 배지 슬롯 초기화
        badgeSlot1 = findViewById(R.id.badgeSlot1);
        badgeSlot2 = findViewById(R.id.badgeSlot2);
        badgeSlot3 = findViewById(R.id.badgeSlot3);
        badgeSlot4 = findViewById(R.id.badgeSlot4);

        // DatabaseHelper 인스턴스 생성
        databaseHelper = new DatabaseHelper(this);
        textViewDaysCount = findViewById(R.id.textViewDaysCount);

        // 금연 일차 계산 및 표시
        updateDaysCount();

        // 사용자 이름을 데이터베이스에서 가져오기
        String userName = databaseHelper.getUserName();
        int daysSinceStart = databaseHelper.calculateDaysSinceStart();

        if (userName != null && !userName.isEmpty()) {
            // 인사 메시지 설정
            textViewGreeting.setText(userName + "님 안녕하세요! \n오늘은 금연" + daysSinceStart + "일차입니다.");
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
        // 동기부여 문구 리스트
        List<String> motivationalQuotes = Arrays.asList(
                "작은 한 걸음이 위대한 변화를 만듭니다.",
                "금연은 새로운 삶의 시작입니다.",
                "오늘도 건강을 위한 멋진 선택을 했습니다.",
                "한번의 결심이 큰 변화를 가져옵니다.",
                "힘들 땐 깊은 숨을 쉬어보세요.",
                "미래의 나를 위해 지금 이겨냅시다.",
                "작은 승리들이 모여 큰 성공을 만듭니다.",
                "지금이 가장 중요한 순간입니다.",
                "오늘도 멋진 선택을 이어가세요.",
                "포기하지 마세요, 당신은 해낼 수 있습니다."
        );

        // Handler로 10초마다 문구 변경
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // 랜덤으로 문구 선택
                String newQuote = motivationalQuotes.get(random.nextInt(motivationalQuotes.size()));
                motivationalTextView.setText(newQuote);

                // 10초 후 다시 실행
                handler.postDelayed(this, 10000); // 10초 = 10000ms
            }
        };

        // 처음 실행
        handler.post(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDaysCount(); // 화면 복귀 시 일차 업데이트
    }

    private void updateDaysCount() {
        int daysSinceStart = databaseHelper.calculateDaysSinceStart();

        updateBadges(daysSinceStart);
    }

    private void updateBadges(int daysSinceStart) {
        // 배지 슬롯들 초기화 (모두 숨기기)
        badgeSlot1.setVisibility(View.GONE);
        badgeSlot2.setVisibility(View.GONE);
        badgeSlot3.setVisibility(View.GONE);
        badgeSlot4.setVisibility(View.GONE);

        // 배지 이미지 추가하기
        if (daysSinceStart >= 1) {
            badgeSlot1.setVisibility(View.VISIBLE);
            ImageView badgeImage1 = new ImageView(this);
            badgeImage1.setImageResource(R.drawable.ic_badge_1day);
            badgeSlot1.addView(badgeImage1);
        }
        if (daysSinceStart >= 7) {
            badgeSlot2.setVisibility(View.VISIBLE);
            ImageView badgeImage2 = new ImageView(this);
            badgeImage2.setImageResource(R.drawable.ic_badge_7days);
            badgeSlot2.addView(badgeImage2);
        }
        if (daysSinceStart >= 30) {
            badgeSlot3.setVisibility(View.VISIBLE);
            ImageView badgeImage3 = new ImageView(this);
            badgeImage3.setImageResource(R.drawable.ic_badge_30days);
            badgeSlot3.addView(badgeImage3);
        }
        if (daysSinceStart >= 365) {
            badgeSlot4.setVisibility(View.VISIBLE);
            ImageView badgeImage4 = new ImageView(this);
            badgeImage4.setImageResource(R.drawable.ic_badge_1year);
            badgeSlot4.addView(badgeImage4);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable); // 메모리 누수 방지
        }
    }
}