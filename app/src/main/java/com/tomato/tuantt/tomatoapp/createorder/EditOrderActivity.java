package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tomato.tuantt.tomatoapp.model.LocationInfo;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class EditOrderActivity extends AppCompatActivity {

    public static final String LOCATION = "LOCATION";
    public static final int CHANGE_LOCATION = 100;
    public static Intent createIntent(Context context, LocationInfo locationInfo) {
        Intent intent = new Intent(context,EditOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOCATION,locationInfo);
        intent.putExtras(bundle);
        return intent;
    }
    private LocationInfo info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = (LocationInfo) getIntent().getExtras().getSerializable(LOCATION);
        int a= 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_LOCATION) {
            if (resultCode == RESULT_OK) {
                info = (LocationInfo) data.getExtras().getSerializable(LOCATION);
                int a = 0;
            }
        }
    }
}
