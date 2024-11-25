package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatisticsActivity extends AppCompatActivity {

    private LinearLayout statisticsContainer;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        statisticsContainer = findViewById(R.id.statisticsContainer);
        databaseHelper = new DatabaseHelper(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView); // BottomNavigationView 추가

        // BottomNavigationView의 아이템 클릭 리스너 설정
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_reset) {
                    databaseHelper.deleteAllRecords(); // 모든 기록 삭제
                    Toast.makeText(StatisticsActivity.this, "모든 기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.action_calendar) {
                    Intent statisticsIntent = new Intent(StatisticsActivity.this, CalendarActivity.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (itemId == R.id.action_statistics) {
                    Intent statisticsIntent = new Intent(StatisticsActivity.this, StatisticsActivity.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (itemId == R.id.action_rewards) {
                    Intent rewardIntent = new Intent(StatisticsActivity.this, RewardsActivity.class);
                    startActivity(rewardIntent);
                    return true;
                }
                return false;
            }
        });

        loadStatistics(); // 날짜별 통계 로드
    }

    private void loadStatistics() {
        // 날짜별 흡연 기록을 가져옴
        List<SmokingRecord> dailyRecords = databaseHelper.getAllSmokingRecords();

        // 각 날짜에 대해 그래프와 텍스트를 표시
        for (SmokingRecord record : dailyRecords) {
            String date = record.getDate(); // "YYYY-MM-DD" 형식의 날짜
            int smokingCount = record.getCount();

            String formattedDate = formatDate(date);

            // 날짜 표시 (TextView)
            TextView dateTextView = new TextView(this);
            dateTextView.setText(formattedDate); // 예: "2024-01-01"
            dateTextView.setTextSize(16f);
            dateTextView.setTextColor(Color.DKGRAY);
            dateTextView.setPadding(0, 24, 0, 8);

            // 막대 그래프 디자인 (LinearLayout)
            LinearLayout barContainer = new LinearLayout(this);
            barContainer.setOrientation(LinearLayout.VERTICAL);
            barContainer.setPadding(0, 8, 0, 8); // 막대 간격을 설정

            // 막대 그래프 (LinearLayout)
            LinearLayout bar = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    smokingCount * 30, 50); // 흡연 횟수에 비례하는 너비 설정
            params.setMargins(0, 0, 16, 0);
            bar.setLayoutParams(params);
            bar.setBackgroundColor(getBarColor(smokingCount));  // 색상 변경
            bar.setPadding(8, 8, 8, 8); // 막대에 패딩 추가
            bar.setElevation(4f);  // 그림자 효과 추가

            // 애니메이션 효과 추가 (막대가 확장됨)
            ValueAnimator animator = ValueAnimator.ofInt(0, smokingCount * 30);
            animator.setDuration(1000); // 애니메이션 시간 1초
            animator.addUpdateListener(animation -> {
                int width = (int) animation.getAnimatedValue();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bar.getLayoutParams();
                layoutParams.width = width;
                bar.setLayoutParams(layoutParams);
            });
            animator.start();

            // 흡연 횟수 텍스트 표시
            TextView countTextView = new TextView(this);
            countTextView.setText(String.valueOf(smokingCount)); // 예: "15"
            countTextView.setTextSize(16f);
            countTextView.setTextColor(Color.BLACK);

            // 막대와 흡연 횟수 텍스트를 막대 컨테이너에 추가
            barContainer.addView(bar);
            barContainer.addView(countTextView);

            // 날짜 텍스트와 막대 컨테이너를 메인 컨테이너에 추가
            statisticsContainer.addView(dateTextView);
            statisticsContainer.addView(barContainer);
        }
    }

    // 흡연 횟수에 따른 색상 변경
    private int getBarColor(int smokingCount) {
        if (smokingCount <= 10) {
            return Color.GREEN;  // 낮은 흡연 횟수일 때 녹색
        } else if (smokingCount < 20) {
            return Color.YELLOW;  // 중간 흡연 횟수일 때 노란색
        } else {
            return Color.RED;  // 높은 흡연 횟수일 때 빨간색
        }
    }
    // 날짜 형식 변환 메서드
    private String formatDate(String date) {
        try {
            // 원본 날짜 형식
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            // 변환할 날짜 형식
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());

            Date parsedDate = originalFormat.parse(date);
            if (parsedDate != null) {
                return targetFormat.format(parsedDate); // "2024년 01월 01일"
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date; // 예외 처리 시 원본 날짜 반환
    }
}
