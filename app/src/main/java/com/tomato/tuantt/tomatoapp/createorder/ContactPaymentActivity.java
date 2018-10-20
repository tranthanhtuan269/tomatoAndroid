package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

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
    //private int phone_type;
    private int payType;
    private String namePayType;
    private String startPhone;
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
        edtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtName.clearFocus();
                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });
        edtName.setOnKeyBack(new CustomEditText.OnKeyBack() {
            @Override
            public void onKeyback() {
                edtName.clearFocus();
            }
        });

        edtPhone = findViewById(R.id.edtPhone);
        edtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtPhone.clearFocus();
                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });
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
        final String[] moneyType = getResources().getStringArray(R.array.money_type);
        List<String> moneyTypeList = new ArrayList<String>(Arrays.asList(moneyType));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item,R.id.textView, moneyTypeList);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spMoneyTYpe.setAdapter(dataAdapter);
        spMoneyTYpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payType = position+1;
                namePayType = moneyType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMoneyTYpe.setSelection(0);

        final String[] phoneType = getResources().getStringArray(R.array.phone_type);
        List<String> phoneTypeList = new ArrayList<String>(Arrays.asList(phoneType));
        ArrayAdapter<String> phoneAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, R.id.textView,phoneTypeList);
        phoneAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spPhoneType.setAdapter(phoneAdapter);
        spPhoneType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startPhone = phoneType[position];
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

        String phoneNumber = SharedPreferenceConfig.getInstance(getApplicationContext()).getPhoneNumber();
        String userName = SharedPreferenceConfig.getInstance(getApplicationContext()).getUserName();
        String userEmail = SharedPreferenceConfig.getInstance(getApplicationContext()).getEmail();
        edtName.setText(userName);
        edtEmail.setText(userEmail);

        if (!TextUtils.isEmpty(phoneNumber)) {
            if (phoneNumber.startsWith("0")) {
                phoneNumber = phoneNumber.substring(1);
            }else {
                int i = -1;
                try {
                    for (String type : phoneType) {
                        i++;
                        int index = type.indexOf("+");
                        String tmpType = type.substring(index);
                        String tmpType2 = tmpType.substring(1);
                        boolean isBreak = false;
                        if (phoneNumber.startsWith(tmpType)) {
                            phoneNumber  = phoneNumber.substring(tmpType.length());
                            isBreak =true;
                        }else if (phoneNumber.startsWith(tmpType2)) {
                            phoneNumber = phoneNumber.substring(tmpType2.length());
                            isBreak = true;
                        }
                        if (isBreak) {
                            break;
                        }
                    }
                    if (i >=0 && i < phoneType.length) {
                        spPhoneType.setSelection(i);
                    }
                }catch (Exception e) {
                }
            }
            edtPhone.setText(phoneNumber);
        }
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
        String email = edtEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this,R.string.msg_alert_email_fail,Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
//            Toast.makeText(this,R.string.msg_alert_email,Toast.LENGTH_SHORT).show();
//            return;
//        }
        OrderWorking.paymentOrderInfor.name = edtName.getText().toString();

        OrderWorking.paymentOrderInfor.startPhone = startPhone;
        String belowPhone = edtPhone.getText().toString();
        OrderWorking.paymentOrderInfor.afterPhone = belowPhone;
        String[] tmpstart = startPhone.split("\\+");
        StringBuilder builder = new StringBuilder("+");
        builder.append(tmpstart[tmpstart.length-1]);
        if (belowPhone.startsWith("0")) {
            builder.append(belowPhone.substring(1));
        }else {
            builder.append(belowPhone);
        }
        OrderWorking.paymentOrderInfor.phone = builder.toString();

        OrderWorking.paymentOrderInfor.email = email;

        OrderWorking.paymentOrderInfor.paymentType = payType;

        OrderWorking.paymentOrderInfor.namePaymentType = namePayType;
        OrderWorking.paymentOrderInfor.promotion = edtPromotion.getText().toString();

        Intent intent = ConfirmPaymentActivity.createIntent(this);
        startActivity(intent);
    }

    private void hideKeyboard(){
        if (this.getCurrentFocus() !=null) {
            try {
                InputMethodManager inputManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }catch (Exception e) {

            }
        }
    }
}
