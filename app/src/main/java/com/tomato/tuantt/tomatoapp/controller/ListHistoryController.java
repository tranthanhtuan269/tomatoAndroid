package com.tomato.tuantt.tomatoapp.controller;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tomato.tuantt.tomatoapp.BuildConfig;
import com.tomato.tuantt.tomatoapp.Constant;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;
import com.tomato.tuantt.tomatoapp.adapter.HistoryPagerAdapter;
import com.tomato.tuantt.tomatoapp.model.OrderData;
import com.tomato.tuantt.tomatoapp.model.Package;
import com.tomato.tuantt.tomatoapp.model.Pivot;
import com.tomato.tuantt.tomatoapp.model.Service;
import com.tomato.tuantt.tomatoapp.model.User;
import com.tomato.tuantt.tomatoapp.view.ListHistoryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListHistoryController {

    private static final String TAG = ListHistoryController.class.getSimpleName();
    private static final String API_NEW_ORDERS = "api/users/neworders?phone=%s&access_token=%s";
    private static final String API_OLD_ORDERS = "api/users/oldorders?phone=%s&access_token=%s";
    private ListHistoryCallback mCallback;
    private int mType;
    private Context mContext;

    public ListHistoryController(Context context, ListHistoryCallback callback) {
        this.mCallback = callback;
        this.mContext = context;
    }

    public void loadData(Bundle arguments) {
        if (mContext == null || mCallback == null) {
            return;
        }

        if (arguments != null && arguments.containsKey(ListHistoryFragment.TYPE)) {
            mType = arguments.getInt(ListHistoryFragment.TYPE);
        }

        String url;
        if (mType == HistoryPagerAdapter.TAB_TODO) {
            url = Constant.BASE_URL + API_NEW_ORDERS;
        } else {
            url = Constant.BASE_URL + API_OLD_ORDERS;
        }
        String phoneNumber = SharedPreferenceConfig.getInstance(mContext).getPhoneNumber();
        String token = SharedPreferenceConfig.getInstance(mContext).getToken();
//        if (BuildConfig.DEBUG) {
//            phoneNumber = "+84973619398";
//            token = "dd4b9a0c9f111a9744ebd7680a801fc8";
//        }
        url = String.format(url, phoneNumber, token);
        if (mCallback != null) {
            mCallback.showProgress();
        }
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String finalUrl = url;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Log.d(ListHistoryController.class.getSimpleName(), finalUrl);
                Log.d(ListHistoryController.class.getSimpleName(), response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject joOrder = jsonObject.getJSONObject("orders");
                    JSONArray jsonArray = joOrder.getJSONArray("data");
                    if (jsonArray != null) {
                        List<OrderData> list = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            OrderData model = new OrderData();
                            model.setId(object.getInt("id"));
                            model.setAddress(object.getString("address"));
                            model.setNumber_address(object.getString("number_address"));
                            model.setNote(object.getString("note"));
                            model.setStart_time(object.getString("start_time"));
                            model.setEnd_time(object.getString("end_time"));
                            model.setState(object.getInt("state"));
                            model.setPrice(object.getString("price"));
                            model.setPay_type(object.getInt("pay_type"));
                            model.setUsername(object.getString("username"));
                            model.setNumber_address(object.getString("number_address"));

                            JSONObject jsonObjectUser = object.getJSONObject("user");
                            User user = new User();
                            user.setId(jsonObjectUser.getInt("id"));
                            user.setName(jsonObjectUser.getString("name"));
                            user.setDisplay_name(jsonObjectUser.getString("display_name"));
                            user.setAvatar(jsonObjectUser.getString("avatar"));
                            user.setEmail(jsonObjectUser.getString("email"));
                            user.setPhone(jsonObjectUser.getString("phone"));
                            user.setAddress(jsonObjectUser.getString("address"));
                            user.setCity_id(jsonObjectUser.getInt("city_id"));
                            user.setRole_id(jsonObjectUser.getInt("role_id"));
                            user.setActive(jsonObjectUser.getInt("active"));
                            user.setPresenter_id(jsonObjectUser.getString("presenter_id"));
                            user.setCode(jsonObjectUser.getString("code"));
                            user.setCreated_at(jsonObjectUser.getString("created_at"));
                            user.setUpdated_at(jsonObjectUser.getString("updated_at"));
                            model.setUser(user);

                            JSONObject jsonObjectService = object.getJSONObject("service");
                            Service service = new Service();
                            service.setId(jsonObjectService.getInt("id"));
                            service.setName(jsonObjectService.getString("name"));
                            service.setIcon(jsonObjectService.getString("icon"));
                            service.setParent_id(jsonObjectService.getInt("parent_id"));
                            model.setService(service);

                            List<Package> packages = new ArrayList<>();
                            JSONArray jsonArrayPackages = object.getJSONArray("package");
                            if (jsonArrayPackages != null) {
                                for (int j = 0; j < jsonArrayPackages.length(); j++) {
                                    JSONObject jsonPackage = jsonArrayPackages.getJSONObject(j);
                                    if (jsonPackage != null) {
                                        JSONObject jsonPivot = jsonPackage.getJSONObject("pivot");
                                        Pivot pivot = new Pivot();
                                        pivot.setOrderId(jsonPivot.getInt("order_id"));
                                        pivot.setPackageId(jsonPivot.getInt("package_id"));

                                        Package packageModel = new Package();
                                        packageModel.setPivot(pivot);
                                        packageModel.setId(jsonPackage.getInt("id"));
                                        packageModel.setServiceId(jsonPackage.getInt("service_id"));
                                        packageModel.setName(jsonPackage.getString("name"));
                                        packageModel.setPrice(jsonPackage.getString("price"));
                                        packageModel.setIcon(jsonPackage.getString("image"));

                                    }
                                }
                            }
                            model.setPackages(packages);

                            list.add(model);
                        }
                        if (mCallback != null) {
                            mCallback.setDataHistory(list);
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                if (mCallback != null) {
                    mCallback.hideProgress();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, Log.getStackTraceString(error));
                requestQueue.stop();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, R.string.msg_load_fail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.msg_load_fail_server, Toast.LENGTH_SHORT).show();
                }
                if (mCallback != null) {
                    mCallback.hideProgress();
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    public void deleteOrder(final int id, String phoneNumber) {
        if (mCallback != null) {
            mCallback.showProgress();
        }
        String url = Constant.BASE_URL + "api/orders/" + id + "?phone=" + phoneNumber + "&access_token=" + SharedPreferenceConfig.getInstance(mContext).getToken();
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (mCallback != null) {
                    mCallback.deleteItem(id);
                    mCallback.hideProgress();
                }
                Toast.makeText(mContext, R.string.msg_delete_success, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, Log.getStackTraceString(error));
                requestQueue.stop();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, R.string.msg_error_network_fail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.msg_delete_fail, Toast.LENGTH_SHORT).show();
                }
                if (mCallback != null) {
                    mCallback.hideProgress();
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    public void updateOrder(final int id, HashMap hashMap) {
        if (mCallback != null) {
            mCallback.showProgress();
        }
        String url = Constant.BASE_URL + "api/orders/" + id;
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (mCallback != null) {
                    mCallback.saveTimeChanged(id);
                    mCallback.hideProgress();
                }
                Toast.makeText(mContext, R.string.msg_update_success, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, Log.getStackTraceString(error));
                requestQueue.stop();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, R.string.msg_error_network_fail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.msg_delete_fail, Toast.LENGTH_SHORT).show();
                }
                if (mCallback != null) {
                    mCallback.reloadHistoryList(id);
                    mCallback.hideProgress();
                }
            }
        });
        requestQueue.add(req);
    }

    public interface ListHistoryCallback {

        void setDataHistory(List<OrderData> list);

        void hideProgress();

        void showProgress();

        void deleteItem(int id);

        void reloadHistoryList(int id);

        void saveTimeChanged(int id);
    }
}
