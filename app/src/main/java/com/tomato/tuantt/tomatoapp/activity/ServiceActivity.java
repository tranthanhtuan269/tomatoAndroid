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
import com.tomato.tuantt.tomatoapp.view.OneFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceActivity extends AppCompatActivity {

    String url_service = "http://api.timtruyen.online/api/services/0/subservice";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

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


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject.getJSONObject("service");
                    JSONArray jsonArray = jsonObject2.getJSONArray("data");

                    adapter = new ViewPagerAdapter(getSupportFragmentManager());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        int id = jo.getInt("id");
                        String name = jo.getString("name");
                        String urlImage = jo.getString("icon");
                        adapter.addFrag(new OneFragment(), name, jo.getJSONObject("services"));
                    }

                    viewPager.setAdapter(adapter);

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
}
