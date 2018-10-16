package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
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
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.tomato.tuantt.tomatoapp.Constant;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;
import com.tomato.tuantt.tomatoapp.adapter.RecyclerViewServiceAdapter;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.helper.GridSpacingItemDecoration;
import com.tomato.tuantt.tomatoapp.model.PaymentOrderInfor;
import com.tomato.tuantt.tomatoapp.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    String url_service = Constant.BASE_URL + "api/services/0/subservice";
    List<Service> lstService;
    RecyclerView mrc;
    RecyclerViewServiceAdapter myAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
        lstService = new ArrayList<>();
        mrc = (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewServiceAdapter(this, lstService);
        mrc.setLayoutManager(new GridLayoutManager(this, 2));
        int spacing = 40; // 50px
        boolean includeEdge = false;
        mrc.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));
        final RequestQueue requestQueue = Volley.newRequestQueue(MenuActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject.getJSONObject("service");
                    JSONArray jsonArray = jsonObject2.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        int id = jo.getInt("id");
                        String name = jo.getString("name");
                        String urlImage = jo.getString("icon");
                        lstService.add(new Service(id, name, urlImage));
                    }

                    mrc.setAdapter(myAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MenuActivity.this, R.string.msg_load_fail_server, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackAction();
                        }
                    }, 2000);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Error", "Error");
                error.printStackTrace();
                requestQueue.stop();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(MenuActivity.this, R.string.msg_load_fail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuActivity.this, R.string.msg_load_fail_server, Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackAction();
                    }
                }, 2000);

            }
        });

        requestQueue.add(stringRequest);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_location);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_location:
                        break;

                    case R.id.navigation_log:
                        Intent intentLog = new Intent(MenuActivity.this, LogActivity.class);
                        startActivity(intentLog);
                        finish();
                        break;

                    case R.id.navigation_user:
                        Intent intentAccount = new Intent(MenuActivity.this, AccountActivity.class);
                        startActivity(intentAccount);
                        finish();
                        break;

                    case R.id.navigation_hsp:
                        Intent intentHSP = new Intent(MenuActivity.this, HSPActivity.class);
                        startActivity(intentHSP);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OrderWorking.currentOrder == null) {
            OrderWorking.currentOrder = new ArrayMap<>();
        } else {
            OrderWorking.currentOrder.clear();
        }
        try {
            if (OrderWorking.activity != null) {
                OrderWorking.activity.finish();
            }
        } catch (Exception e) {
        }
        OrderWorking.activity = null;
        OrderWorking.paymentOrderInfor = new PaymentOrderInfor();
        if (TextUtils.isEmpty(SharedPreferenceConfig.getInstance(this).getPhoneNumber())) {
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    String phoneNumberString = phoneNumber.toString();
                    SharedPreferenceConfig.getInstance(MenuActivity.this).setPhoneNumber(phoneNumberString);
                }

                @Override
                public void onError(final AccountKitError error) {
                    // Handle Error
                    Log.e("getCurrentAccount", error.toString());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

    private void onBackAction() {
        SharedPreferenceConfig preferenceConfig = SharedPreferenceConfig.getInstance(getApplicationContext());
        if (preferenceConfig.readLoginStatus()) {
            finish();
        } else {
            finish();
        }
    }

    private void calcuServiceHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenDp = (int) (metrics.heightPixels / metrics.density);
        OrderWorking.serviceHeight = (int) (screenDp / 640 * 200 * metrics.density);
    }
}