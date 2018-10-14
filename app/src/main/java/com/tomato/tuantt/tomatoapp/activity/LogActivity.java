package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.adapter.ViewLogPagerAdapter;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.view.HistoryFragment;
import com.tomato.tuantt.tomatoapp.view.LogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogActivity extends AppCompatActivity {

    private static final String TAG = LogActivity.class.getCanonicalName();
    private TextView textView;
    private String url_service = "http://api.timtruyen.online/api/users/oldorders?phone=+84973619398&access_token=dd4b9a0c9f111a9744ebd7680a801fc8";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewLogPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        textView = (TextView) findViewById(R.id.textView);

        String phone = "+84973619398";
        String accessToken = "dd4b9a0c9f111a9744ebd7680a801fc8";

        url_service = "http://api.timtruyen.online/api/users/oldorders?phone=" + phone + "&access_token=" + accessToken;

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        adapter = new ViewLogPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LogFragment(), "Chờ làm");
        adapter.addFrag(new LogFragment(), "Đã làm");

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

        final RequestQueue requestQueue = Volley.newRequestQueue(LogActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // process JSON
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONObject("orders").getJSONArray("data");
                    if (jsonArray.length() == 0) {
//                        textView.setText("Bạn chưa tạo một công việc nào!");
                    } else {

                    }
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
                switch (item.getItemId()) {
                    case R.id.navigation_location:
                        Intent intentMenu = new Intent(LogActivity.this, MenuActivity.class);
                        startActivity(intentMenu);
                        break;

                    case R.id.navigation_log:
                        Fragment fragment = HistoryFragment.newInstance();
                        FragmentManager fm = getSupportFragmentManager();
                        fm.beginTransaction()
                                .add(R.id.container, fragment, fragment.getClass().getCanonicalName())
                                .commit();
                        break;

                    case R.id.navigation_user:
                        Toast.makeText(LogActivity.this, "Click user", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_hsp:
                        Intent intentHSP = new Intent(LogActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        break;
                }
                return true;
            }
        });
    }
}
