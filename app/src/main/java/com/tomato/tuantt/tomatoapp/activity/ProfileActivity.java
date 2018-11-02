package com.tomato.tuantt.tomatoapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.Constant;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;
import com.tomato.tuantt.tomatoapp.createorder.CustomEditText;
import com.tomato.tuantt.tomatoapp.model.Event;
import com.tomato.tuantt.tomatoapp.model.User;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int CODE_GALLERY_REQUEST = 999;

    @BindView(R.id.titleBarTxt)
    TextView tvTitle;
    @BindView(R.id.ivGift)
    ImageView ivGift;
    @BindView(R.id.ivBell)
    ImageView ivBell;
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.edt_user_name)
    CustomEditText edtUserName;
    @BindView(R.id.edt_email)
    CustomEditText edtEmail;
    @BindView(R.id.edt_present)
    CustomEditText edtPresentId;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolBar)
    Toolbar toolbar;

    private Unbinder mUnbinder;
    private User mUser;
    private String mUrl;
    private Bitmap bitmap;
    private int change_avatar = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUnbinder = ButterKnife.bind(this);
        tvTitle.setText(R.string.txt_my_profile);
        ivGift.setVisibility(View.INVISIBLE);
        ivBell.setVisibility(View.INVISIBLE);

        setSupportActionBar(toolbar);
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        UserTextWatcher textWatcher = new UserTextWatcher();
        edtEmail.addTextChangedListener(textWatcher);
        edtUserName.addTextChangedListener(textWatcher);
        edtPresentId.addTextChangedListener(textWatcher);

        Button changeAvt = (Button) findViewById(R.id.changeAvatarBtn);

        changeAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_avatar = 1;
                ActivityCompat.requestPermissions(
                        ProfileActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        CODE_GALLERY_REQUEST
                );
            }
        });

        final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constant.USER_INFO)) {
            mUser = intent.getParcelableExtra(Constant.USER_INFO);
            if (mUser != null && mUser.getId() != -1) {
                setLayoutData(mUser);
                mUrl = Constant.BASE_URL + "api/users/" + mUser.getId();
                return;
            }
        }
        edtPresentId.setText(SharedPreferenceConfig.getInstance(this).getPresentId());
        edtEmail.setText(SharedPreferenceConfig.getInstance(this).getEmail());
        edtUserName.setText(SharedPreferenceConfig.getInstance(this).getUserName());
        String avatar = SharedPreferenceConfig.getInstance(this).getAvatarLink();
        if (TextUtils.isEmpty(avatar)) {
            civAvatar.setImageResource(R.drawable.ic_avatar);
        } else {
            Picasso.with(this).load(avatar).error(R.drawable.ic_avatar).fit().centerInside().into(civAvatar);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent1, 1);
            }else{
                Toast.makeText(ProfileActivity.this,"Bạn không có quyền truy cập vào thư viện ảnh",Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                civAvatar.setImageBitmap(bitmap);
                civAvatar.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setLayoutData(User user) {
        if (user == null) {
//            tvSave.setTextColor(ContextCompat.getColor(this, R.color.color_gray));
//            tvSave.setEnabled(false);
            return;
        }

        edtUserName.setText(user.getName());
        edtEmail.setText(user.getEmail());
        edtPresentId.setText(user.getPresenter_id());
        Picasso.with(this).load(mUser.getAvatar()).error(R.drawable.ic_avatar).fit().centerInside().into(civAvatar);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    class UserTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mUser == null) {
                return;
            }
            String fullName = edtUserName.getText().toString();
            String email = edtEmail.getText().toString();
            String presentId = edtPresentId.getText().toString();
            if ((!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(presentId)) &&
                    (!TextUtils.equals(email, mUser.getEmail()) ||
                            !TextUtils.equals(fullName, mUser.getName()) ||
                            !TextUtils.equals(presentId, mUser.getPresenter_id()))) {
                setEnableButtonSave(true);
            } else {
                setEnableButtonSave(false);
            }
        }
    }

    private void setEnableButtonSave(boolean enable) {
//        tvSave.setEnabled(enable);
//        if (enable) {
//            tvSave.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
//        } else {
//            tvSave.setTextColor(ContextCompat.getColor(this, R.color.color_gray));
//        }
    }

    @OnClick(R.id.tv_save)
    void onClickSave() {
        final String fullName = edtUserName.getText().toString().trim();
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, R.string.msg_alert_name, Toast.LENGTH_SHORT).show();
            return;
        }
        final String presentId = edtPresentId.getText().toString().trim();
        if (TextUtils.isEmpty(presentId)) {
            Toast.makeText(this, R.string.msg_alert_code, Toast.LENGTH_SHORT).show();
            return;
        }
        final String email = edtEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.msg_alert_email_fail, Toast.LENGTH_SHORT).show();
            return;
        }
        final String phone = SharedPreferenceConfig.getInstance(this).getPhoneNumber();
        final String access_token = SharedPreferenceConfig.getInstance(this).getToken();

        progressBar.setVisibility(View.VISIBLE);

//        StringBuilder urlBuilder = new StringBuilder();
//        urlBuilder.append(mUrl)
//                .append("?name=").append(fullName)
//                .append("&email=").append(email)
//                .append("&presenter_id=").append(presentId)
//                .append("&phone=").append(phone)
//                .append("&access_token=").append(access_token);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ProfileActivity.this, R.string.msg_update_success, Toast.LENGTH_SHORT).show();
                mUser.setName(fullName);
                mUser.setEmail(email);
                mUser.setPresenter_id(presentId);
                EventBus.getDefault().post(Event.CHANGED_USER_INFO);
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.e("VolleyError", android.util.Log.getStackTraceString(error));
                progressBar.setVisibility(View.GONE);
                requestQueue.stop();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ProfileActivity.this, R.string.msg_error_network_fail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, R.string.msg_delete_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String imageData = "";
                if(change_avatar == 1){
                    imageData = imageToString(bitmap);
                }
                params.put("name", fullName);
                params.put("email", email);
                params.put("presenter_id", presentId);
                params.put("phone", phone);
                params.put("access_token", access_token);
                if(change_avatar == 1){
                    params.put("avatar", imageData);
                }
                return params;
            }
        };

        requestQueue.add(req);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImage;
    }
}
