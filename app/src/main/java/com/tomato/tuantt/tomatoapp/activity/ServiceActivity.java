package com.tomato.tuantt.tomatoapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.tomato.tuantt.tomatoapp.adapter.ViewPagerAdapter;
import com.tomato.tuantt.tomatoapp.createorder.ChangePackageListener;
import com.tomato.tuantt.tomatoapp.createorder.MapsActivity;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.createorder.PackageBuyAdapter;
import com.tomato.tuantt.tomatoapp.createorder.ViewOne;
import com.tomato.tuantt.tomatoapp.createorder.ViewOneAdapter;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.model.Package;
import com.tomato.tuantt.tomatoapp.view.OneFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ServiceActivity extends AppCompatActivity implements ChangePackageListener, PackageBuyAdapter.OnPackageRemove , EasyPermissions.PermissionCallbacks{

    public static Intent createIntent(Context context, int serviceID, String name,boolean firstCreate) {
        Intent intent = new Intent(context, ServiceActivity.class);
        intent.putExtra(SERVICE_ID, serviceID);
        intent.putExtra(SERVICE_NAME, name);
        intent.putExtra(FIRST_CREATE, firstCreate);
        return intent;
    }

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;

    public static final String SERVICE_ID = "SERVICE_ID";
    public static final String FIRST_CREATE = "CALL_FROM_SERVICE";
    public static final String SERVICE_NAME = "SERVICE_NAME";
    String url_service = Constant.BASE_URL + "api/services/0/subservice";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private boolean firstCreate;
    private ViewOneAdapter viewOneAdapter;
    private RecyclerView rvSelect;
    private PackageBuyAdapter packageBuyAdapter;
    private LinearLayoutManager manager;
    boolean isOnResume = false;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        int id = intent.getIntExtra(SERVICE_ID,1);
        firstCreate = intent.getBooleanExtra(FIRST_CREATE,true);
        if (firstCreate) {
            OrderWorking.activity = this;
        }
        url_service = Constant.BASE_URL + "api/services/"+id+"/subservice";

        if (dialog == null) {
            dialog = new ProgressDialog(ServiceActivity.this);
            dialog.setMessage(getString(R.string.msg_wait_load_data));
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rvSelect = findViewById(R.id.rvSelect);
        manager = new LinearLayoutManager(this);
        rvSelect.setLayoutManager(manager);
        rvSelect.setItemAnimator(new DefaultItemAnimator());

        packageBuyAdapter = new PackageBuyAdapter(this,this);
        rvSelect.setAdapter(packageBuyAdapter);

        findViewById(R.id.btnAgree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionAgree();
            }
        });
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText("Bảng giá");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackAction();
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

                } finally {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Error", "Error");
                error.printStackTrace();
                requestQueue.stop();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ServiceActivity.this,R.string.msg_load_fail,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ServiceActivity.this,R.string.msg_load_fail_server,Toast.LENGTH_SHORT).show();
                }
                onBackAction();
            }
        });

        dialog.show();
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstCreate && !isOnResume) {
            isOnResume = true;
            if (OrderWorking.currentOrder !=null && !OrderWorking.currentOrder.isEmpty()) {
                for (Package p : OrderWorking.currentOrder.values()) {
                    packageBuyAdapter.addPackage(p);
                }
                packageBuyAdapter.notifyDataSetChanged();
                autoSize();
            }
        }

    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

    protected void onBackAction(){
//        if (firstCreate) {
//           super.onBackPressed();
//        } else {
//        }
        super.onBackPressed();
    }

    private void onActionAgree(){
        boolean allowNext = false;
        if (OrderWorking.currentOrder !=null && OrderWorking.currentOrder.size() > 0) {
            for (Package p : OrderWorking.currentOrder.values()){
                if (p.number > 0) {
                    allowNext = true;
                    break;
                }
            }
        }

        if (firstCreate) {
            if (allowNext) {
                requestPermission();
            } else {
                Toast.makeText(this,R.string.msg_alert_select_package,Toast.LENGTH_SHORT).show();
            }

        } else {
            if (allowNext) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this,R.string.msg_alert_select_package,Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onChange(Package p) {
        if (OrderWorking.currentOrder == null) {
            OrderWorking.currentOrder = new ArrayMap<>();
        }
        if (OrderWorking.currentOrder.get(p.getId()) != null) {
            Package tmp = OrderWorking.currentOrder.get(p.getId());
            tmp.number = p.number;
            packageBuyAdapter.updatePackage(tmp);
        } else {
            Package tmp = new Package();
            tmp.setName(p.getName());
            tmp.setId(p.getId());
            tmp.number = p.number;
            tmp.setPrice(p.getPrice());
            OrderWorking.currentOrder.put(p.getId(), tmp);
            packageBuyAdapter.addPackage(tmp);
            autoSize();
        }
    }

    @Override
    public void onPackageRemove(int id) {
        if (OrderWorking.currentOrder !=null) {
            if (OrderWorking.currentOrder.get(id) != null) {
                OrderWorking.currentOrder.remove(id);
            }
        }
        for (ViewOne o : viewOneAdapter.getViewOnes()) {
            o.resetData(id);
        }
        autoSize();
    }

    int height = 0;
    private void autoSize(){
        if (packageBuyAdapter.hasMoreView()) {
            manager.setAutoMeasureEnabled(false);
            ViewGroup.LayoutParams params = rvSelect.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            params.height = height;
            rvSelect.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = rvSelect.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            height = (int) (30 * metrics.density * packageBuyAdapter.getItemCount());
            params.height = height;
            rvSelect.setLayoutParams(params);
        }
    }

    private void openMap(){
        Intent intent = MapsActivity.createIntent(ServiceActivity.this,true);
        startActivity(intent);
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
    public void  requestPermission(){
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            openMap();
        } else {
            EasyPermissions.requestPermissions(this, "Ứng dụng cần quyền location " + "\n",
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //just do nothing , everything is done by requestPermission
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
