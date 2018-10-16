package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.model.Event;
import com.tomato.tuantt.tomatoapp.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private String url_service = "http://api.timtruyen.online/api/users?phone=";
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";

    private int userid;
    private String username;
    private String userimage;
    private String usercode;
    private User mUser = new User();

    private TextView usernameLbl, userlinkLbl, infoHSPLbl, inviteFriendLbl, couponLbl, historyLbl, configLbl;
    private ImageView userimageImg, infoHSPImg, inviteFriendImg, couponImg, historyImg, configImg;

    BottomNavigationView bottomNavigationView;
    private boolean isReloadData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (OrderWorking.serviceHeight == 0) {
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
        usernameLbl = (TextView) findViewById(R.id.userNameLbl);
        userlinkLbl = (TextView) findViewById(R.id.userLinkLbl);

        infoHSPLbl = (TextView) findViewById(R.id.faqLbl);
        inviteFriendLbl = (TextView) findViewById(R.id.reportLbl);
        couponLbl = (TextView) findViewById(R.id.contactLbl);
        configLbl = (TextView) findViewById(R.id.configLbl);

        userimageImg = (ImageView) findViewById(R.id.userAvatarImg);
        infoHSPImg = (ImageView) findViewById(R.id.faqImg);
        inviteFriendImg = (ImageView) findViewById(R.id.reportImg);
        couponImg = (ImageView) findViewById(R.id.contactImg);
        configImg = (ImageView) findViewById(R.id.aboutImg);

        infoHSPLbl.setOnClickListener(this);
        inviteFriendLbl.setOnClickListener(this);
        couponLbl.setOnClickListener(this);
        configLbl.setOnClickListener(this);

        infoHSPImg.setOnClickListener(this);
        inviteFriendImg.setOnClickListener(this);
        couponImg.setOnClickListener(this);
        configImg.setOnClickListener(this);
        userlinkLbl.setOnClickListener(this);

        loadData();

        // setting bottom
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_user);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_location:
                        Intent intentMenu = new Intent(AccountActivity.this, MenuActivity.class);
                        startActivity(intentMenu);
                        finish();
                        break;

                    case R.id.navigation_log:
                        Intent intent = new Intent(AccountActivity.this, LogActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.navigation_user:
//                        Intent accountIntent = new Intent(AccountActivity.this, AccountActivity.class);
//                        startActivity(accountIntent);
//                        finish();
                        break;

                    case R.id.navigation_hsp:
                        Intent intentHSP = new Intent(AccountActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        finish();
                        break;
                }
                return true;
            }
        });

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReloadData) {
            loadData();
            isReloadData = false;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadData() {
        String url = url_service + SharedPreferenceConfig.getInstance(this).getPhoneNumber();
        final RequestQueue requestQueue = Volley.newRequestQueue(AccountActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status_code") == 200) {
                        JSONObject jsonObject2 = jsonObject.getJSONObject("users").getJSONObject("data");
                        userid = jsonObject2.getInt("id");
                        username = jsonObject2.getString("name");
                        userimage = jsonObject2.getString("avatar");
                        usercode = jsonObject2.getString("code");
                        SharedPreferenceConfig config = SharedPreferenceConfig.getInstance(getApplicationContext());
                        if (TextUtils.isEmpty(config.getUserCode()) && !TextUtils.isEmpty(usercode)) {
                            config.saveUserCode(usercode);
                        }
                        if (TextUtils.isEmpty(config.getAvatarLink()) && !TextUtils.isEmpty(userimage)) {
                            config.saveAvatarLink(userimage);
                        }
                        String email = jsonObject2.getString("email");
                        config.setUserName(username);
                        config.setEmail(email);
                        mUser.setId(userid);
                        mUser.setName(username);
                        mUser.setCode(usercode);
                        mUser.setAvatar(defaultUrlImage + userimage);
                        mUser.setEmail(email);

                        usernameLbl.setText(username);
                        Picasso.with(AccountActivity.this).load(defaultUrlImage + userimage).error(R.drawable.ic_avatar_default).fit().centerInside().into(userimageImg);
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
            case R.id.userLinkLbl:
//                Intent intent = new Intent(this, ProfileActivity.class);
//                intent.putExtra(Constant.USER_INFO, mUser);
//                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void calcuServiceHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenDp = (int) (metrics.heightPixels / metrics.density);
        OrderWorking.serviceHeight = (int) (screenDp / 640 * 200 * metrics.density);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        if (event != null && event == Event.CHANGED_USER_INFO) {
            isReloadData = true;
        }
    }
}
