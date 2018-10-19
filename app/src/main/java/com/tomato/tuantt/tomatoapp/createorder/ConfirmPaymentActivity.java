package com.tomato.tuantt.tomatoapp.createorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.tomato.tuantt.tomatoapp.activity.MainActivity;
import com.tomato.tuantt.tomatoapp.activity.ServiceActivity;
import com.tomato.tuantt.tomatoapp.model.CreateOrder;
import com.tomato.tuantt.tomatoapp.model.Package;
import com.tomato.tuantt.tomatoapp.model.PackageOrder;
import com.tomato.tuantt.tomatoapp.model.PaymentOrderInfor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class ConfirmPaymentActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context,ConfirmPaymentActivity.class);
        return intent;
    }

    private ProgressDialog dialog;
    private boolean isAllowClick = true;
    private View btnCreateOrder;
    private static final String TAG = "CREATE_ORDER";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        TextView title = (TextView) toolbar.findViewById(R.id.titleBarTxt);
        title.setText(R.string.confirm_payment_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView ivService = findViewById(R.id.ivService);
        Picasso.with(this).load(OrderWorking.paymentOrderInfor.currentServiceUrl).fit().centerInside().into(ivService);
        TextView tvService = findViewById(R.id.tvService);
        tvService.setText(OrderWorking.paymentOrderInfor.currentService);

        TextView tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setText(OrderWorking.paymentOrderInfor.location.address);

        TextView tvAddressNumber = findViewById(R.id.tvAddressNumber);
        tvAddressNumber.setText(OrderWorking.paymentOrderInfor.numberAddress);

        TextView tvTime = findViewById(R.id.tvTime);
        String[] week = getResources().getStringArray(R.array.week);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(OrderWorking.paymentOrderInfor.time);
        StringBuilder builder = new StringBuilder();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            builder.append(0+""+hour);
        }else {
            builder.append(hour);
        }
        builder.append(":");
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 10) {
            builder.append(0+""+minute);
        }else {
            builder.append(minute);
        }

        builder.append(" ");
        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
        builder.append(week[dayofWeek-1]);
        builder.append(", ");

        int day  = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            builder.append(0+""+day);
        }else {
            builder.append(day);
        }
        builder.append("/");
        int month = calendar.get(Calendar.MONTH)+1;
        if (month < 10) {
            builder.append(0+""+month);
        }else {
            builder.append(month);
        }
        builder.append("/");
        int year = calendar.get(Calendar.YEAR);
        builder.append(year);
        tvTime.setText(builder.toString());

        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(OrderWorking.paymentOrderInfor.name);

        TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.setText(OrderWorking.paymentOrderInfor.phone);

        TextView tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(OrderWorking.paymentOrderInfor.email);

        TextView tvMoney = findViewById(R.id.tvMoney);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price = formatter.format(OrderWorking.paymentOrderInfor.totalMoney);
        price = price.replace(",",".");
        tvMoney.setText(price +" VNĐ");

        TextView tvMoneyPay = findViewById(R.id.tvMoneyPay);
        tvMoneyPay.setText(OrderWorking.paymentOrderInfor.namePaymentType);

        btnCreateOrder = findViewById(R.id.tvConfirm);
        findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

        if (dialog == null) {
            dialog = new ProgressDialog(ConfirmPaymentActivity.this);
            dialog.setMessage(getString(R.string.msg_wait_create_order));
            dialog.setCancelable(false);
        }

        isAllowClick = true;
        btnCreateOrder.setEnabled(true);
    }
    CreateOrder order;
    private void createOrder() {
        if (!isAllowClick) {
            return;
        }
        btnCreateOrder.setEnabled(false);
        isAllowClick = false;
        List<PackageOrder> packageOrders = new ArrayList<>();
        for (Package p : OrderWorking.currentOrder.values()) {
            PackageOrder tmp = new PackageOrder();
            tmp.package_id = p.getId();
            tmp.number = p.number;
            packageOrders.add(tmp);
        }
        order = new CreateOrder();
        PaymentOrderInfor infor = OrderWorking.paymentOrderInfor;
        order.address = infor.location.address;
        order.number_address = infor.numberAddress;
        order.note = "";
        order.start_time = String.valueOf(infor.time);
        order.end_time = order.start_time;
        order.price = String.valueOf(infor.totalMoney);
        order.pay_type = infor.paymentType;
        order.list_packages = packageOrders;
        order.phone = infor.phone;
        order.access_token = SharedPreferenceConfig.getInstance(this).getToken();
        order.promotion_code = infor.promotion;
        order.username = infor.name;
        order.email = infor.email;

        String url = Constant.BASE_URL +"api/orders";

        final RequestQueue requestQueue = Volley.newRequestQueue(ConfirmPaymentActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    JSONObject jo = new JSONObject(response);
                    int status = jo.getInt("status_code");
                    if (status == 200 || status == 201) {
                        Toast.makeText(ConfirmPaymentActivity.this,R.string.msg_create_order_success,Toast.LENGTH_SHORT).show();
                        finishOrder();
                    } else {
                        isAllowClick = true;
                        btnCreateOrder.setEnabled(true);
                        Toast.makeText(ConfirmPaymentActivity.this,R.string.msg_create_order_fail_server,Toast.LENGTH_SHORT).show();
                        requestQueue.stop();
                        requestQueue.cancelAll(TAG);
                    }
                } catch (JSONException e) {
                    isAllowClick = true;
                    btnCreateOrder.setEnabled(true);
                    Toast.makeText(ConfirmPaymentActivity.this,R.string.msg_create_order_fail_server,Toast.LENGTH_SHORT).show();
                    requestQueue.stop();
                    requestQueue.cancelAll(TAG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                error.printStackTrace();
                requestQueue.stop();
                requestQueue.cancelAll(TAG);
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ConfirmPaymentActivity.this,R.string.msg_create_order_fail,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConfirmPaymentActivity.this,R.string.msg_create_order_fail_server,Toast.LENGTH_SHORT).show();
                }
                isAllowClick = true;
                btnCreateOrder.setEnabled(true);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("address",order.address);
                params.put("number_address",order.number_address);
                params.put("note",order.note);
                params.put("start_time",order.start_time);
                params.put("end_time",order.end_time);
                params.put("price",order.price);
                params.put("pay_type",""+order.pay_type);
                params.put("phone",order.phone);
                params.put("access_token",order.access_token);
                params.put("promotion_code",order.promotion_code);
                params.put("username",order.username);
                params.put("email",order.email);
                JSONArray ja = new JSONArray();
                for (PackageOrder p : order.list_packages) {
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("package_id", p.package_id);
                        jo.put("number", p.number);
                        ja.put(jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                params.put("list_packages",ja.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        dialog.show();
        requestQueue.add(stringRequest);
    }

    private void finishOrder(){
        Intent intent = new Intent(ConfirmPaymentActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
