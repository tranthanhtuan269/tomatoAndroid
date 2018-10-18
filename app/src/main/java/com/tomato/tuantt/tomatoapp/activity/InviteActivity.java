package com.tomato.tuantt.tomatoapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.BuildConfig;
import com.tomato.tuantt.tomatoapp.Constant;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;
import com.tomato.tuantt.tomatoapp.adapter.RecyclerViewServiceAdapter;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.helper.BottomNavigationViewHelper;
import com.tomato.tuantt.tomatoapp.helper.GridSpacingItemDecoration;
import com.tomato.tuantt.tomatoapp.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InviteActivity extends AppCompatActivity {

    private String url_service = "http://api.timtruyen.online/api/users?phone=+84973619398";
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";

    private int userid;
    private String userimage;
    private String usercode;

    private TextView userlinkLbl;
    private ImageView userimageImg;
    private Button shareBtn;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private SharedPreferenceConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        // setting top

        if (OrderWorking.serviceHeight == 0 ) {
            calcuServiceHeight();
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText("HSP");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FacebookSdk.sdkInitialize(this.getApplicationContext());


        // main
        userlinkLbl = (TextView) findViewById(R.id.userLinkLbl);
        userimageImg = (ImageView) findViewById(R.id.userAvatarImg);
        shareBtn = (Button) findViewById(R.id.shareBtn);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        config = SharedPreferenceConfig.getInstance(getApplicationContext());
        usercode = config.getUserCode();
        userimage = config.getAvatarLink();

        if (!TextUtils.isEmpty(usercode)) {
            userlinkLbl.setText(usercode);
        }

        if (!TextUtils.isEmpty(userimage)) {
            Picasso.with(InviteActivity.this).load(defaultUrlImage + userimage).error(R.drawable.ic_avatar).fit().centerInside().into(userimageImg);
        }
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usercode == null) {
                    usercode = "";
                }
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote(usercode)
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID))
                        .build();
                if(ShareDialog.canShow(ShareLinkContent.class))
                {
                    shareDialog.show(linkContent);
                }
            }
        });


        if (TextUtils.isEmpty(usercode) || TextUtils.isEmpty(userimage)) {
            final RequestQueue requestQueue = Volley.newRequestQueue(InviteActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url_service, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getInt("status_code") == 200){
                            JSONObject jsonObject2 = jsonObject.getJSONObject("users").getJSONObject("data");
                            userid = jsonObject2.getInt("id");
                            String tmpImage = jsonObject2.getString("avatar");;
                            String tmpCode = jsonObject2.getString("code");;

                            if (TextUtils.isEmpty(usercode) && !TextUtils.isEmpty(tmpCode)) {
                                usercode = tmpCode;
                                config.saveUserCode(usercode);
                                userlinkLbl.setText(usercode);
                            }

                            if (TextUtils.isEmpty(userimage) && !TextUtils.isEmpty(tmpImage)) {
                                userimage  = tmpImage;
                                config.saveAvatarLink(userimage);
                                Picasso.with(InviteActivity.this).load(defaultUrlImage + userimage).error(R.drawable.ic_avatar).fit().centerInside().into(userimageImg);
                            }
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
    }



    private void calcuServiceHeight(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenDp = (int) (metrics.heightPixels / metrics.density);
        OrderWorking.serviceHeight = (int) (screenDp / 640 * 200 * metrics.density);
    }
}
