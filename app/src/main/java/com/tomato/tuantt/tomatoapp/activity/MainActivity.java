package com.tomato.tuantt.tomatoapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final static  int REQUEST_CODE = 999;
    private SharedPreferenceConfig preferenceConfig;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceConfig = SharedPreferenceConfig.getInstance(getApplicationContext());

        printKeyHash();

        ImageButton chondichvuBtn = (ImageButton) findViewById(R.id.chondichvuBtn);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);

        chondichvuBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (requestPermission()) {
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!preferenceConfig.readLoginStatus() || AccountKit.getCurrentAccessToken() == null){
                   if (requestPermission()) {
                       startLoginPage(LoginType.PHONE);
                   }
                }
            }
        });
        requestPermission();
    }

    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(result.getError() != null){
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }else if(result.wasCancelled()){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if(result.getAccessToken() != null){
                    preferenceConfig.saveToken(result.getAccessToken().getToken());
//                    Toast.makeText(this, "getAccessToken! " + result.getAccessToken().getAccountId(), Toast.LENGTH_SHORT).show();
                    Log.d("getAccessToken", result.getAccessToken().getAccountId());
                }else{
//                    Toast.makeText(this, "getAuthorizationCode! " + result.getAuthorizationCode(), Toast.LENGTH_SHORT).show();
                    //Log.d("getAuthorizationCode", result.getAccessToken().getAccountId());
                }
                preferenceConfig.writeLoginStatus(true);
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.tomato.tuantt.tomatoapp", PackageManager.GET_SIGNATURES);

            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @AfterPermissionGranted(PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
    public boolean  requestPermission(){
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION ,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECEIVE_SMS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (preferenceConfig.readLoginStatus()){
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "Ứng dụng cần thêm quyền để hỗ trợ bạn tốt hơn!",
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, perms);
            return false;
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
