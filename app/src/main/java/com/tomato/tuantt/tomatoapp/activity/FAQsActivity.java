package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tomato.tuantt.tomatoapp.Constant;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class FAQsActivity extends AppCompatActivity {

    private TextView contentLbl;
    private String url_service = Constant.BASE_URL + "api/faqs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        // setting top
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText("Câu hỏi thường gặp");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });


        contentLbl = (TextView) findViewById(R.id.whyuseLbl);

        final RequestQueue requestQueue = Volley.newRequestQueue(FAQsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String jo = jsonObject.getString("content");
                    contentLbl.setText(Html.fromHtml(jo));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Error", "Error");
                error.printStackTrace();
                requestQueue.stop();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(FAQsActivity.this,R.string.msg_load_fail,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FAQsActivity.this,R.string.msg_load_fail_server,Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackAction();
                    }
                },2000);

            }
        });

        requestQueue.add(stringRequest);


        // setting bottom
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_location:
                        Intent intentMenu = new Intent(FAQsActivity.this, MenuActivity.class);
                        startActivity(intentMenu);
                        break;

                    case R.id.navigation_log:
                        Intent intent = new Intent(FAQsActivity.this, LogActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_user:
                        Intent accountIntent = new Intent(FAQsActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        break;

                    case R.id.navigation_hsp:
                        Intent intentHSP = new Intent(FAQsActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

    private void onBackAction(){
        SharedPreferenceConfig preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        if (preferenceConfig.readLoginStatus()){
            super.onBackPressed();
        } else {
            startActivity(new Intent(FAQsActivity.this, MainActivity.class));
            finish();
        }
    }
}