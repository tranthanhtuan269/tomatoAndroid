package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.tuantt.tomatoapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class ContactPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MONEY = "MONEY";
    public static Intent createIntent(Context context, int totalMoney) {
        Intent intent = new Intent(context,ContactPaymentActivity.class);
        intent.putExtra(MONEY,totalMoney);
        return intent;
    }

    private CustomEditText edtName,edtPhone,edtEmail,edtPromotion;
    private Spinner spMoneyTYpe,spPhoneType;
    private int sum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_payment);
        sum = getIntent().getIntExtra(MONEY,0);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText(R.string.contact_payment_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackAction();
            }
        });


        edtName = findViewById(R.id.edtName);
        edtName.clearFocus();
        edtName.setOnKeyBack(new CustomEditText.OnKeyBack() {
            @Override
            public void onKeyback() {
                edtName.clearFocus();
            }
        });

        edtPhone = findViewById(R.id.edtPhone);
        edtPhone.setOnKeyBack(new CustomEditText.OnKeyBack() {
            @Override
            public void onKeyback() {
                edtPhone.clearFocus();
            }
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edtEmail.clearFocus();
                    hideKeyboard();
                    return true;
                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });
        edtEmail.setOnKeyBack(new CustomEditText.OnKeyBack() {
            @Override
            public void onKeyback() {
                edtEmail.clearFocus();
            }
        });


        edtPromotion = findViewById(R.id.edtPromotion);
        edtPromotion.setOnKeyBack(new CustomEditText.OnKeyBack() {
            @Override
            public void onKeyback() {
                edtPromotion.clearFocus();
            }
        });

        spMoneyTYpe = findViewById(R.id.spMoneyTYpe);
        spPhoneType = findViewById(R.id.spPhoneType);
        String[] moneyType = getResources().getStringArray(R.array.money_type);
        List<String> moneyTypeList = new ArrayList<String>(Arrays.asList(moneyType));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item,R.id.textView, moneyTypeList);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spMoneyTYpe.setAdapter(dataAdapter);
        spMoneyTYpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMoneyTYpe.setSelection(0);

        String[] phoneType = getResources().getStringArray(R.array.phone_type);
        List<String> phoneTypeList = new ArrayList<String>(Arrays.asList(phoneType));
        ArrayAdapter<String> phoneAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, R.id.textView,phoneTypeList);
        phoneAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spPhoneType.setAdapter(phoneAdapter);
        spPhoneType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMoneyTYpe.setSelection(0);

        TextView tvMoney = findViewById(R.id.tvMoney);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price = formatter.format(sum);
        price = price.replace(",",".");
        tvMoney.setText(price +" VNĐ");
        findViewById(R.id.llNext).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

    protected void onBackAction(){
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.llNext:
                nextAction();
                break;
        }
    }

    private void nextAction(){
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            Toast.makeText(this,R.string.msg_alert_name,Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtPhone.getText().toString())) {
            Toast.makeText(this,R.string.msg_alert_phone_number,Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            Toast.makeText(this,R.string.msg_alert_email,Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void hideKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
