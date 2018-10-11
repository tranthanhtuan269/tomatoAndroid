package com.tomato.tuantt.tomatoapp.createorder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.model.LocationInfo;
import com.tomato.tuantt.tomatoapp.model.Package;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class EditOrderActivity extends AppCompatActivity implements View.OnClickListener {

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
    private TextView tvAddress,tvAddressNumber,tvDate,tvHour,tvMinute,tvMoney;
    private Button btnChoose;
    private Calendar time;
    private boolean needCal = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createorder);
        info = (LocationInfo) getIntent().getExtras().getSerializable(LOCATION);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText(OrderWorking.currentService);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackAction();
            }
        });
        findViewById(R.id.llAddress).setOnClickListener(this);
        findViewById(R.id.llAddressNumber).setOnClickListener(this);
        tvDate = findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);
        findViewById(R.id.llTime).setOnClickListener(this);
        findViewById(R.id.llAddMore).setOnClickListener(this);
        findViewById(R.id.llNext).setOnClickListener(this);

        tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setText(info.name);

        tvAddressNumber = findViewById(R.id.tvAddressNumber);
        tvHour = findViewById(R.id.tvHour);
        tvMinute = findViewById(R.id.tvMinute);
        tvMoney = findViewById(R.id.tvMoney);

        createTmpTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needCal) {
            needCal = false;
            int sum = 0;
            for (Package p : OrderWorking.currentOrder.values()) {
                String price = p.getPrice();
                if (price.contains(",")) {
                    price = price.replace(",","");
                }
                if (price.contains(".")) {
                    price = price.replace(".","");
                }
                Integer i = Integer.valueOf(price);
                int tmp = i * p.number;
                sum +=tmp;
            }
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String price = formatter.format(sum);
            price = price.replace(",",".");
            tvMoney.setText(price +" VNĐ");
        }
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

    protected void onBackAction(){
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.llAddress:
                break;
            case R.id.llAddressNumber:
                break;
            case R.id.tvDate:
                showDatePicker();
                break;
            case R.id.llTime:
                showTimePicker();
                break;
            case R.id.llAddMore:
                break;
            case R.id.llNext:
                break;
        }
    }

    private void createTmpTime(){
        time = Calendar.getInstance();
        time.add(Calendar.DATE,1);
        time.set(Calendar.HOUR_OF_DAY, 14);
        time.set(Calendar.MINUTE,0);
        time.set(Calendar.MILLISECOND,0);
        updateTimeData();
    }

    private void updateTimeData(){
        Calendar calendar = Calendar.getInstance();
        StringBuilder date = new StringBuilder();
        if (time.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            date.append(getString(R.string.today));
            date.append("\n");
        }else if (time.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1) {
            date.append(getString(R.string.tomorrow));
            date.append("\n");
        }
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            date.append("0");
        }
        date.append(day);
        date.append("/");
        int month = time.get(Calendar.MONTH)+1;
        if (month < 10) {
            date.append("0");
        }
        date.append(month);
        date.append("/");
        date.append( time.get(Calendar.YEAR));
        tvDate.setText(date.toString());
        int hour = time.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            tvHour.setText(0+""+hour);
        }else {
            tvHour.setText(""+hour);
        }

        int minute = time.get(Calendar.MINUTE);
        if (minute < 10) {
            tvMinute.setText(0+""+minute);
        }else {
            tvMinute.setText(""+minute);
        }
    }

    private void showDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar tmp = Calendar.getInstance();
                tmp.set(year,month,dayOfMonth,time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),0);
                if (calendar.getTimeInMillis() > tmp.getTimeInMillis()) {
                    Toast.makeText(EditOrderActivity.this,R.string.msg_alert_time_bigger,Toast.LENGTH_SHORT).show();
                    return;
                }
//                time.set(Calendar.YEAR,year);
//                time.set(Calendar.MONTH,month);
//                time.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                time.set(year,month,dayOfMonth);
                updateTimeData();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_YEAR,7);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_YEAR,-7);
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    private void showTimePicker(){
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar tmp = Calendar.getInstance();
                tmp.set(time.get(Calendar.YEAR),time.get(Calendar.MONTH),time.get(Calendar.DAY_OF_MONTH),hourOfDay,minute,0);
                if (calendar.getTimeInMillis() > tmp.getTimeInMillis()) {
                    Toast.makeText(EditOrderActivity.this,R.string.msg_alert_time_bigger,Toast.LENGTH_SHORT).show();
                    return;
                }
                time.set(Calendar.HOUR_OF_DAY,hourOfDay);
                time.set(Calendar.MINUTE,minute);
                updateTimeData();
            }
        },time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),true);
        dialog.show();

    }
}
