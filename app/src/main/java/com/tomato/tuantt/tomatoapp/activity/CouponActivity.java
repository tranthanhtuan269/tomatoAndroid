package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;

public class CouponActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        // setting top
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText("HSP");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });



        // setting bottom
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_location:
                        Toast.makeText(CouponActivity.this, "Click Location", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_log:
                        Intent intent = new Intent(CouponActivity.this, LogActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_user:
                        Intent accountIntent = new Intent(CouponActivity.this, LogActivity.class);
                        startActivity(accountIntent);
                        break;

                    case R.id.navigation_hsp:
                        Toast.makeText(CouponActivity.this, "Click hsp", Toast.LENGTH_SHORT).show();
                        Intent intentHSP = new Intent(CouponActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        break;
                }
                return true;
            }
        });
    }
}
