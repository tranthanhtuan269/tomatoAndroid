package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {

    private String url_service = "http://api.timtruyen.online/api/users?phone=+84973619398";
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";

    private int userid;
    private String username;
    private String userimage;

    private TextView usernameLbl, userlinkLbl;
    private ImageView userimageImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

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


        // main

        userimageImg = (ImageView) findViewById(R.id.userAvatarImg);
        usernameLbl = (TextView) findViewById(R.id.userNameLbl);
        userlinkLbl = (TextView) findViewById(R.id.userLinkLbl);


        final RequestQueue requestQueue = Volley.newRequestQueue(AccountActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("status_code") == 200){
                        JSONObject jsonObject2 = jsonObject.getJSONObject("users").getJSONObject("data");
                        userid = jsonObject2.getInt("id");
                        username = jsonObject2.getString("name");
                        userimage = jsonObject2.getString("avatar");

                        usernameLbl.setText(username);
                        Picasso.with(AccountActivity.this).load(defaultUrlImage + userimage).fit().centerInside().into(userimageImg);
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


        // setting bottom
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_location:
                        Toast.makeText(AccountActivity.this, "Click Location", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_log:
                        Intent intent = new Intent(AccountActivity.this, LogActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_user:
                        Intent accountIntent = new Intent(AccountActivity.this, LogActivity.class);
                        startActivity(accountIntent);
                        break;

                    case R.id.navigation_hsp:
                        Toast.makeText(AccountActivity.this, "Click hsp", Toast.LENGTH_SHORT).show();
                        Intent intentHSP = new Intent(AccountActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        break;
                }
                return true;
            }
        });
    }
}
