package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.tomato.tuantt.tomatoapp.adapter.RecyclerViewNewsAdapter;
import com.tomato.tuantt.tomatoapp.adapter.RecyclerViewServiceAdapter;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.helper.GridSpacingItemDecoration;
import com.tomato.tuantt.tomatoapp.model.News;
import com.tomato.tuantt.tomatoapp.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HSPInfoActivity extends AppCompatActivity {

    String url_service = Constant.BASE_URL + "api/news";
    List<News> lstNews;
    RecyclerView mrc;
    RecyclerViewNewsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hspinfo);

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


        lstNews = new ArrayList<>();
        mrc = (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewNewsAdapter(this, lstNews);
        mrc.setLayoutManager(new GridLayoutManager(this, 1));
        final RequestQueue requestQueue = Volley.newRequestQueue(HSPInfoActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject.getJSONObject("news");
                    JSONArray jsonArray = jsonObject2.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        int id = jo.getInt("id");
                        String title = jo.getString("title");
                        String year = jo.getString("created_at");
                        lstNews.add(new News(id, title, year));
                    }

                    mrc.setAdapter(myAdapter);

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
                    Toast.makeText(HSPInfoActivity.this,R.string.msg_load_fail,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HSPInfoActivity.this,R.string.msg_load_fail_server,Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(HSPInfoActivity.this, MainActivity.class));
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
