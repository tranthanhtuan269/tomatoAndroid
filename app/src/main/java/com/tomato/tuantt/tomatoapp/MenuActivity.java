package com.tomato.tuantt.tomatoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                TextView userIdLbl = (TextView) findViewById(R.id.userIdLbl);
                TextView userNameLbl = (TextView) findViewById(R.id.userNameLbl);
                TextView userPhoneLbl = (TextView) findViewById(R.id.userPhoneLbl);

                userIdLbl.setText(String.format("User Id: %s", account.getId()));
                userNameLbl.setText(String.format("User name: %s", account.getEmail()));
                userPhoneLbl.setText(String.format("User phone: %s", account.getPhoneNumber()));
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }
}
