package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RewardsActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);  // activity_rewards.xml 레이아웃 파일 연결

        databaseHelper = new DatabaseHelper(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView); // BottomNavigationView 추가
        // BottomNavigationView의 아이템 클릭 리스너 설정
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_reset) {
                    databaseHelper.deleteAllRecords(); // 모든 기록 삭제
                    Toast.makeText(RewardsActivity.this, "모든 기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.action_calendar) {
                    Intent statisticsIntent = new Intent(RewardsActivity.this, CalendarActivity.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (itemId == R.id.action_statistics) {
                    Intent statisticsIntent = new Intent(RewardsActivity.this, StatisticsActivity.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (itemId == R.id.action_rewards) {
                    Intent rewardIntent = new Intent(RewardsActivity.this, RewardsActivity.class);
                    startActivity(rewardIntent);
                    return true;
                }
                return false;
            }
        });
    }
}
