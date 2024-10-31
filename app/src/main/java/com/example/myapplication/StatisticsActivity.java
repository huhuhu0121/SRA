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
        List<SmokingRecord> records = databaseHelper.getAllSmokingRecords();

        for (SmokingRecord record : records) {
            String date = record.getDate();
            int smokingCount = record.getCount();

            // 날짜 표시
            TextView dateTextView = new TextView(this);
            dateTextView.setText(date);
            dateTextView.setTextSize(16f);
            dateTextView.setTextColor(Color.DKGRAY);
            dateTextView.setPadding(0, 24, 0, 8);
            statisticsContainer.addView(dateTextView);

            // 막대 그래프 생성
            LinearLayout barContainer = new LinearLayout(this);
            barContainer.setOrientation(LinearLayout.HORIZONTAL);
            barContainer.setPadding(0, 8, 0, 8);

            LinearLayout bar = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(smokingCount * 30, 50);
            params.setMargins(0, 0, 16, 0);
            bar.setLayoutParams(params);
            bar.setBackgroundColor(Color.BLUE);
            bar.setPadding(8, 8, 8, 8);

            // 흡연 횟수 표시
            TextView countTextView = new TextView(this);
            countTextView.setText(String.valueOf(smokingCount));
            countTextView.setTextSize(16f);
            countTextView.setTextColor(Color.BLACK);

            // 막대와 카운트 추가
            barContainer.addView(bar);
            barContainer.addView(countTextView);
            statisticsContainer.addView(barContainer);
        }
    }
}
