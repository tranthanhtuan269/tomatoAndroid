package com.tomato.tuantt.tomatoapp.model;

import com.google.gson.annotations.SerializedName;

public class Pivot {
    @SerializedName("order_id")
    private int orderId;
    @SerializedName("package_id")
    private int packageId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }
}
