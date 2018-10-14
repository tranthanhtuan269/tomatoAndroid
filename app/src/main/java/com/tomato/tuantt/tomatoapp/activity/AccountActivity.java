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

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener{

    private String url_service = "http://api.timtruyen.online/api/users?phone=+84973619398";
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";

    private int userid;
    private String username;
    private String userimage;
    private String usercode;

    private TextView usernameLbl, userlinkLbl, infoHSPLbl, inviteFriendLbl, couponLbl, historyLbl, configLbl;
    private ImageView userimageImg, infoHSPImg, inviteFriendImg, couponImg, historyImg, configImg;

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
        usernameLbl = (TextView) findViewById(R.id.userNameLbl);
        userlinkLbl = (TextView) findViewById(R.id.userLinkLbl);

        infoHSPLbl = (TextView) findViewById(R.id.faqLbl);
        inviteFriendLbl = (TextView) findViewById(R.id.reportLbl);
        couponLbl = (TextView) findViewById(R.id.contactLbl);
        historyLbl = (TextView) findViewById(R.id.legalLbl);
        configLbl = (TextView) findViewById(R.id.configLbl);

        userimageImg = (ImageView) findViewById(R.id.userAvatarImg);
        infoHSPImg = (ImageView) findViewById(R.id.faqImg);
        inviteFriendImg = (ImageView) findViewById(R.id.reportImg);
        couponImg = (ImageView) findViewById(R.id.contactImg);
        historyImg = (ImageView) findViewById(R.id.legalImg);
        configImg = (ImageView) findViewById(R.id.aboutImg);

        infoHSPLbl.setOnClickListener(this);
        inviteFriendLbl.setOnClickListener(this);
        couponLbl.setOnClickListener(this);
        historyLbl.setOnClickListener(this);
        configLbl.setOnClickListener(this);

        infoHSPImg.setOnClickListener(this);
        inviteFriendImg.setOnClickListener(this);
        couponImg.setOnClickListener(this);
        historyImg.setOnClickListener(this);
        configImg.setOnClickListener(this);



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
                        usercode = jsonObject2.getString("usercode");

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
                        Intent intentMenu = new Intent(AccountActivity.this, MenuActivity.class);
                        startActivity(intentMenu);
                        break;

                    case R.id.navigation_log:
                        Intent intent = new Intent(AccountActivity.this, LogActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_user:
                        Intent accountIntent = new Intent(AccountActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        break;

                    case R.id.navigation_hsp:
                        Intent intentHSP = new Intent(AccountActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.faqLbl:
            case R.id.faqImg:
                Intent intentHSPINFO = new Intent(AccountActivity.this, HSPInfoActivity.class);
                startActivity(intentHSPINFO);
                break;

            case R.id.reportLbl:
            case R.id.reportImg:
                Intent intentINVITE = new Intent(AccountActivity.this, InviteActivity.class);
                startActivity(intentINVITE);
                break;

            case R.id.contactLbl:
            case R.id.contactImg:
                Intent intentCOUPON = new Intent(AccountActivity.this, CouponActivity.class);
                startActivity(intentCOUPON);
                break;

            case R.id.legalLbl:
            case R.id.legalImg:
                Intent intentHISTORY = new Intent(AccountActivity.this, HistoryActivity.class);
                startActivity(intentHISTORY);
                break;

            case R.id.configLbl:
            case R.id.aboutImg:
                Intent intentCONFIG = new Intent(AccountActivity.this, ConfigActivity.class);
                startActivity(intentCONFIG);
                break;

            default:
                break;
        }
    }
}
