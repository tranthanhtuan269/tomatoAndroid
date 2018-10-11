package com.tomato.tuantt.tomatoapp.activity;

import android.content.Context;
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
import com.tomato.tuantt.tomatoapp.createorder.ChangePackageListener;
import com.tomato.tuantt.tomatoapp.createorder.ViewOne;
import com.tomato.tuantt.tomatoapp.createorder.ViewOneAdapter;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.view.OneFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceActivity extends AppCompatActivity implements ChangePackageListener {

    public static Intent createIntent(Context context, int serviceID, String name,boolean firstCreate) {
        Intent intent = new Intent(context, ServiceActivity.class);
        intent.putExtra(SERVICE_ID, serviceID);
        intent.putExtra(SERVICE_NAME, name);
        intent.putExtra(FIRST_CREATE, firstCreate);
        return intent;
    }

    public static final String SERVICE_ID = "SERVICE_ID";
    public static final String FIRST_CREATE = "FIRST_CREATE";
    public static final String SERVICE_NAME = "SERVICE_NAME";
    String url_service = "http://api.timtruyen.online/api/services/0/subservice";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private boolean firstCreate;
    private ViewOneAdapter viewOneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        int id = intent.getIntExtra(SERVICE_ID,1);
        firstCreate = intent.getBooleanExtra(FIRST_CREATE,true);
        url_service = "http://api.timtruyen.online/api/services/"+id+"/subservice";


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText("Bảng giá");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstCreate) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    finish();
                }
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

//                    adapter = new ViewPagerAdapter(getSupportFragmentManager());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jo = jsonArray.getJSONObject(i);
//                        int id = jo.getInt("id");
//                        String name = jo.getString("name");
//                        String urlImage = jo.getString("icon");
//                        adapter.addFrag(new OneFragment(), name, jo.getJSONObject("services"));
//                    }
//                    viewPager.setAdapter(adapter);

                    viewOneAdapter = new ViewOneAdapter();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String name = jo.getString("name");
                        ViewOne viewOne = new ViewOne(ServiceActivity.this);
                        viewOne.setData(jo.getJSONObject("services").toString(),name,ServiceActivity.this);
                        viewOneAdapter.addView(viewOne);
                    }
                    viewPager.setAdapter(viewOneAdapter);

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
    }

    @Override
    public void onChange(int id, String name, int number) {

    }
}
