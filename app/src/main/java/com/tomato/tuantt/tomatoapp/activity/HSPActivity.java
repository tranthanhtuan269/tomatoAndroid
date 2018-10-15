package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;

public class HSPActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView whyuseLbl, practiceLbl, faqsLbl, reportLbl, contactLbl, legalLbl, aboutLbl;
    private ImageView whyuseImg, practiceImg, faqsImg, reportImg, contactImg, legalImg, aboutImg;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsp);

        // setting top

        if (OrderWorking.serviceHeight == 0 ) {
            calcuServiceHeight();
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);

//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText("HSP");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });


        // main

        whyuseLbl = (TextView) findViewById(R.id.whyuseLbl);
        practiceLbl = (TextView) findViewById(R.id.bestpraticeLbl);
        faqsLbl = (TextView) findViewById(R.id.faqLbl);
        reportLbl = (TextView) findViewById(R.id.reportLbl);
        contactLbl = (TextView) findViewById(R.id.contactLbl);
        legalLbl = (TextView) findViewById(R.id.legalLbl);
        aboutLbl = (TextView) findViewById(R.id.aboutLbl);

        whyuseImg = (ImageView) findViewById(R.id.whyuseImg);
        practiceImg = (ImageView) findViewById(R.id.bestpraticeImg);
        faqsImg = (ImageView) findViewById(R.id.faqImg);
        reportImg = (ImageView) findViewById(R.id.reportImg);
        contactImg = (ImageView) findViewById(R.id.contactImg);
        legalImg = (ImageView) findViewById(R.id.legalImg);
        aboutImg = (ImageView) findViewById(R.id.aboutImg);


        whyuseLbl.setOnClickListener(this);
        whyuseImg.setOnClickListener(this);
        practiceLbl.setOnClickListener(this);
        practiceImg.setOnClickListener(this);
        faqsLbl.setOnClickListener(this);
        faqsImg.setOnClickListener(this);
        reportLbl.setOnClickListener(this);
        reportImg.setOnClickListener(this);
        contactLbl.setOnClickListener(this);
        contactImg.setOnClickListener(this);
        legalLbl.setOnClickListener(this);
        legalImg.setOnClickListener(this);
        aboutLbl.setOnClickListener(this);
        aboutImg.setOnClickListener(this);




        // setting bottom
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_hsp);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_location:
                        Intent intentMenu = new Intent(HSPActivity.this, MenuActivity.class);
                        startActivity(intentMenu);
                        finish();
                        break;

                    case R.id.navigation_log:
                        Intent intent = new Intent(HSPActivity.this, LogActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.navigation_user:
                        Intent accountIntent = new Intent(HSPActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        finish();
                        break;

                    case R.id.navigation_hsp:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.whyuseLbl:
            case R.id.whyuseImg:
                Intent intentWhyUse = new Intent(HSPActivity.this, WhyUseActivity.class);
                startActivity(intentWhyUse);
                break;

            case R.id.bestpraticeLbl:
            case R.id.bestpraticeImg:
                Intent intentBestPratice = new Intent(HSPActivity.this, BestPractiveActivity.class);
                startActivity(intentBestPratice);
                break;

            case R.id.faqLbl:
            case R.id.faqImg:
                Intent intentHSPINFO = new Intent(HSPActivity.this, FAQsActivity.class);
                startActivity(intentHSPINFO);
                break;

            case R.id.reportLbl:
            case R.id.reportImg:
                Intent intentINVITE = new Intent(HSPActivity.this, ReportActivity.class);
                startActivity(intentINVITE);
                break;

            case R.id.contactLbl:
            case R.id.contactImg:
                Intent intentCOUPON = new Intent(HSPActivity.this, ContactActivity.class);
                startActivity(intentCOUPON);
                break;

            case R.id.legalLbl:
            case R.id.legalImg:
                Intent intentHISTORY = new Intent(HSPActivity.this, LegalActivity.class);
                startActivity(intentHISTORY);
                break;

            case R.id.aboutLbl:
            case R.id.aboutImg:
                Intent intentCONFIG = new Intent(HSPActivity.this, AboutActivity.class);
                startActivity(intentCONFIG);
                break;

            default:
                break;
        }
    }



    private void calcuServiceHeight(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenDp = (int) (metrics.heightPixels / metrics.density);
        OrderWorking.serviceHeight = (int) (screenDp / 640 * 200 * metrics.density);
    }
}
