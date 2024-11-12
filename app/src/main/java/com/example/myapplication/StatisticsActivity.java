package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private LinearLayout statisticsContainer;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // 뒤로가기 버튼 설정
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        statisticsContainer = findViewById(R.id.statisticsContainer);
        databaseHelper = new DatabaseHelper(this);

        loadStatistics();
    }

    private void loadStatistics() {
        // 월별 흡연 기록을 가져옴
        List<SmokingRecord> monthlyRecords = databaseHelper.getMonthlySmokingStatistics();

        for (SmokingRecord record : monthlyRecords) {
            String month = record.getDate(); // "YYYY-MM" 형식의 월
            int smokingCount = record.getCount();

            // 월 표시
            TextView monthTextView = new TextView(this);
            monthTextView.setText(month); // 예: "2024-01"
            monthTextView.setTextSize(16f);
            monthTextView.setTextColor(Color.DKGRAY);
            monthTextView.setPadding(0, 24, 0, 8);
            statisticsContainer.addView(monthTextView);

            // 막대 그래프 생성
            LinearLayout barContainer = new LinearLayout(this);
            barContainer.setOrientation(LinearLayout.VERTICAL);
            barContainer.setPadding(0, 8, 0, 8);

            LinearLayout bar = new LinearLayout(this);
            // 흡연 횟수에 따라 막대의 너비가 결정됩니다
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(smokingCount * 30, 50);
            params.setMargins(0, 0, 16, 0);
            bar.setLayoutParams(params);
            bar.setBackgroundColor(Color.BLUE);
            bar.setPadding(8, 8, 8, 8);

            // 흡연 횟수 텍스트 표시
            TextView countTextView = new TextView(this);
            countTextView.setText(String.valueOf(smokingCount)); // 예: "15"
            countTextView.setTextSize(16f);
            countTextView.setTextColor(Color.BLACK);

            // 막대와 흡연 횟수 텍스트를 막대 컨테이너에 추가
            barContainer.addView(bar);
            barContainer.addView(countTextView);

            // 막대 컨테이너를 메인 컨테이너에 추가
            statisticsContainer.addView(barContainer);
        }
    }
}
