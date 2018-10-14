package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;

public class LogActivity extends AppCompatActivity {

    private static final String TAG = LogActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                findViewById(R.id.container).setVisibility(View.GONE);
                switch (item.getItemId()) {
                    case R.id.navigation_location:
                        Toast.makeText(LogActivity.this, "Click Location", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_log:
                        finish();
                        Intent intent = new Intent(LogActivity.this, LogActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_user:
                        Toast.makeText(LogActivity.this, "Click user", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_hsp:
                        Toast.makeText(LogActivity.this, "Click hsp", Toast.LENGTH_SHORT).show();
                        Intent intentHSP = new Intent(LogActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        break;
                }
                return true;
            }
        });
    }
}
