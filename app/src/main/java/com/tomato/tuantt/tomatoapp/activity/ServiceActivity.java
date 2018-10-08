package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tomato.tuantt.tomatoapp.adapter.ViewPagerAdapter;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.helper.Utils;
import com.tomato.tuantt.tomatoapp.model.Services.DataNoteOne;
import com.tomato.tuantt.tomatoapp.model.Services.ResponseServices;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    String url_service = "http://api.timtruyen.online/api/services/0/subservice";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<DataNoteOne> listCategory = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        setupViewPager();
        Intent intent = getIntent();
        int id = intent.getExtras().getInt("ServiceId");
        url_service = "http://api.timtruyen.online/api/services/"+id+"/subservice";
//        nameTv = (TextView) findViewById(R.id.serviceName);
//        thumbnailIv = (ImageView) findViewById(R.id.serviceThumbnail);
//
//        Intent intent = getIntent();
//        int id = intent.getExtras().getInt("ServiceId");
//        String name = intent.getExtras().getString("ServiceName");
//        String thumbnail = intent.getExtras().getString("ServiceThumbnail");
//
//        nameTv.setText(id + "");
//        Picasso.with(this).load(defaultUrlImage + thumbnail).fit().centerInside().into(thumbnailIv);




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

        final RequestQueue requestQueue = Volley.newRequestQueue(ServiceActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    ResponseServices responseServices = Utils.parserObject(response, ResponseServices.class);
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject jsonObject2 = jsonObject.getJSONObject("serviceData");
//                    JSONArray jsonArray = jsonObject2.getJSONArray("data");
//
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jo = jsonArray.getJSONObject(i);
//                        int id = jo.getInt("id");
//                        String name = jo.getString("name");
//                        String urlImage = jo.getString("icon");
//                        adapter.addFrag(new OneFragment(), name, jo.getJSONObject("services"));
//                    }
                    adapter.addListCategory(responseServices.serviceNoteOne.listDataNoteOne);
                    //viewPager.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Error");
                error.printStackTrace();
                requestQueue.stop();
            }
        });

        requestQueue.add(stringRequest);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_location:
                        Toast.makeText(ServiceActivity.this, "Click Location", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_log:
                        Intent intent = new Intent(ServiceActivity.this, LogActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_user:
                        Toast.makeText(ServiceActivity.this, "Click user", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_hsp:
                        Toast.makeText(ServiceActivity.this, "Click hsp", Toast.LENGTH_SHORT).show();
                        Intent intentHSP = new Intent(ServiceActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        break;
                }
                return true;
            }
        });
    }

    private void setupViewPager(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), listCategory);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
