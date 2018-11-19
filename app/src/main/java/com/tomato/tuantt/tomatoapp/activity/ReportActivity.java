package com.tomato.tuantt.tomatoapp.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.model.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private Button sendBtn;
    private EditText reportTxt;
    private ProgressBar progressBar;
    private String url_service = "http://api.timtruyen.online/api/feedbacks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // setting top
        if (OrderWorking.serviceHeight == 0 ) {
            calcuServiceHeight();
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText("Góp ý, báo lỗi");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendBtn = (Button) findViewById(R.id.sendBtn);
        reportTxt = (EditText) findViewById(R.id.reportTxt);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String reportContent = reportTxt.getText().toString().trim();
                if (TextUtils.isEmpty(reportContent)) {
                    Toast.makeText(ReportActivity.this, R.string.msg_alert_report_content, Toast.LENGTH_SHORT).show();
                    return;
                }

                final String phone = SharedPreferenceConfig.getInstance(ReportActivity.this).getPhoneNumber();
                final String access_token = SharedPreferenceConfig.getInstance(ReportActivity.this).getToken();
                Log.d("REPORT_CONTENT", reportContent);

                final RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
                StringRequest req = new StringRequest(Request.Method.POST, url_service, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("access_token", response.toString());
                        Toast.makeText(ReportActivity.this, R.string.msg_send_feedback_success, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.e("VolleyError", android.util.Log.getStackTraceString(error));
                        requestQueue.stop();
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(ReportActivity.this, R.string.msg_error_network_fail, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReportActivity.this, R.string.msg_delete_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("content_feedback", reportContent);
                        params.put("phone", phone);
                        params.put("access_token", access_token);
                        return params;
                    }
                };

                requestQueue.add(req);
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

    private void onBackAction(){
        SharedPreferenceConfig preferenceConfig = SharedPreferenceConfig.getInstance(getApplicationContext());
        if (preferenceConfig.readLoginStatus()){
            super.onBackPressed();
        } else {
            startActivity(new Intent(ReportActivity.this, MainActivity.class));
            finish();
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