package com.tomato.tuantt.tomatoapp.createorder;

import android.util.ArrayMap;
import android.util.SparseArray;

import com.tomato.tuantt.tomatoapp.activity.ServiceActivity;
import com.tomato.tuantt.tomatoapp.model.LocationInfo;
import com.tomato.tuantt.tomatoapp.model.Package;
import com.tomato.tuantt.tomatoapp.model.PaymentOrderInfor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class OrderWorking {
    public static Map<Integer, Package> currentOrder = new ArrayMap<>();
    public static ServiceActivity activity;
    public static PaymentOrderInfor paymentOrderInfor = new PaymentOrderInfor();
}
