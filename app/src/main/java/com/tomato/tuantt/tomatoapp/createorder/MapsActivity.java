package com.tomato.tuantt.tomatoapp.createorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.tomato.tuantt.tomatoapp.Constant;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.model.LocationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private static final int REQUEST_CHECK_SETTINGS = 102;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 101;
    public static final String CALL_FROM_SERVICE = "CALL_FROM_SERVICE";

    public static Intent createIntent(Context context,boolean callFromService) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(CALL_FROM_SERVICE, callFromService);
        return intent;
    }

    private GoogleMap mMap;
    private TextView tvAddress;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationClient;
    Location mLastKnownLocation;
    private static final int DEFAULT_ZOOM = 15;
    private LocationCallback mLocationCallback;
    Geocoder geocoder;
    private ProgressDialog dialog;
    private View mapView;
    private View imgLocation;
    private boolean callFromService;
    private LatLng currentLatlng;
    private Button btnChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        callFromService = getIntent().getBooleanExtra(CALL_FROM_SERVICE,false);
        mapFragment.getMapAsync(this);
        findViewById(R.id.imgBack).setOnClickListener(this);
        findViewById(R.id.imgClear).setOnClickListener(this);
        btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(this);

        findViewById(R.id.llAddress).setOnClickListener(this);
        imgLocation = findViewById(R.id.imgLocation);
        imgLocation.setOnClickListener(this);
        tvAddress = findViewById(R.id.tvAddress);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        if (dialog == null) {
            dialog = new ProgressDialog(MapsActivity.this);
            dialog.setMessage(getString(R.string.msg_wait_load_location));
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                List<Location> locations = locationResult.getLocations();
                if (isGetLocation) {
                    return;
                }
                if (!locations.isEmpty()) {
                    if (mMap != null) {
                        isGetLocation = true;
                        mLastKnownLocation = locations.get(0);
                        if (!mMap.isMyLocationEnabled()) {
                            mMap.setMyLocationEnabled(true);
                        }
                        if (OrderWorking.paymentOrderInfor == null || OrderWorking.paymentOrderInfor.location == null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            addMarker(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),true);
                        }
                    }
                }
            }

            ;
        };
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private boolean mRequestingLocationUpdates;

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private boolean isGetLocation;

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (!mLocationPermissionGranted) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, null /* Looper */);
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
    public void  requestPermission(){
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mLocationPermissionGranted = true;
            createLocationRequest();
        } else {
            EasyPermissions.requestPermissions(this, "Ứng dụng cần quyền location " + "\n",
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, perms);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS ) {
            if (resultCode == RESULT_OK) {
                startLocationUpdates();
            }else {
                createLocationRequest();
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String name = place.getName().toString();
                String address = place.getAddress().toString();
                if (!address.contains(name)) {
                    address = name +", "+ address;
                }
                tvAddress.setText(address);
                addMarker(place.getLatLng(),false);
                Log.i("KI", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("KI", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        if (OrderWorking.paymentOrderInfor.location !=null) {
            LocationInfo info = OrderWorking.paymentOrderInfor.location;
            tvAddress.setText(info.address);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(info.lat, info.lgn), DEFAULT_ZOOM));
            addMarker(new LatLng(info.lat, info.lgn),false);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng,true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgClear:
                tvAddress.setText("");
                if (currentMarket !=null) {
                    currentMarket.remove();
                }
                break;
            case R.id.btnChoose:
                if (TextUtils.isEmpty(tvAddress.getText().toString().trim()) || currentLatlng == null || currentLatlng.latitude == 0 || currentLatlng.longitude == 0) {
                    Toast.makeText(this,R.string.msg_alert_select_location,Toast.LENGTH_SHORT).show();
                    return;
                }
                LocationInfo info = new LocationInfo();
                info.lat = currentLatlng.latitude;
                info.lgn = currentLatlng.longitude;
                info.address = tvAddress.getText().toString();
                OrderWorking.paymentOrderInfor.location = info;

                if (callFromService) {
                    Intent intent = EditOrderActivity.createIntent(this);
                    startActivity(intent);
                    if (OrderWorking.activity !=null) {
                        OrderWorking.activity.finish();
                        OrderWorking.activity = null;
                    }
                } else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                }

                finish();
                break;
            case R.id.imgLocation:
                moveToCurrent();
                break;
            case R.id.llAddress:
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)
                            .setCountry("VN")
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                // mLastKnownLocation = null;
                // getLocationPermission();
                // requestPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: ", e.getMessage());
        }
    }

    private LocationRequest mLocationRequest;
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...\
                mRequestingLocationUpdates = true;
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private Marker currentMarket;
    private void addMarker(LatLng latLng, boolean needGetLocation){
        // Creating a marker
        if (currentMarket !=null) {
            currentMarket.remove();
        }

        currentLatlng = latLng;

        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(MapsActivity.this,R.drawable.ic_location)));

        // Clears the previously touched position
        mMap.clear();
        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        // Placing a marker on the touched position
        currentMarket = mMap.addMarker(markerOptions);

//        List<Address> addresses = new ArrayList<>();
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            android.location.Address address = addresses.get(0);
//
//            if (address != null) {
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
//                    sb.append(address.getAddressLine(i) + ",");
//                }
//                // String line = address.getAddressLine(0);
//                tvAddress.setText(sb.substring(0,sb.length()-1));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (needGetLocation) {
            getAddNormal(latLng);
        }

        // getAddressPro(latLng);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void getAddressPro(LatLng latLng){
        dialog.show();
        final Location location = new Location("Current");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        String latlong = latLng.latitude +","+latLng.longitude;
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latlong+"&key="+Constant.GEO_KEY;

        final RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("results");
                    int length = result.length();
                    String address;
                    if (length > 0) {
                        Location tmpLo = new Location("location0");
                        address = result.getJSONObject(0).getString("formatted_address");
                        double lat = result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        double lng = result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        tmpLo.setLatitude(lat);
                        tmpLo.setLongitude(lng);
                        float distance = location.distanceTo(tmpLo);
                        if (length > 1) {
                            String tmp = result.getJSONObject(1).getString("formatted_address");
                            double lat1 = result.getJSONObject(1).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            double lng1 = result.getJSONObject(1).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            tmpLo.setLatitude(lat1);
                            tmpLo.setLongitude(lng1);
                            float distance1 = location.distanceTo(tmpLo);
                            if (TextUtils.isEmpty(address)) {
                                address = tmp;
                            }else {
                                if (!TextUtils.isEmpty(tmp)) {
//                                   if (address.length() < tmp.length()) {
//                                       address = tmp;
//                                   }
                                    if (distance1 < distance) {
                                        address = tmp;
                                        distance = distance1;
                                    }
                                }
                            }
                        }

                        if (length > 2) {
                            String tmp2 = result.getJSONObject(2).getString("formatted_address");
                            double lat1 = result.getJSONObject(2).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            double lng1 = result.getJSONObject(2).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            tmpLo.setLatitude(lat1);
                            tmpLo.setLongitude(lng1);
                            float distance1 = location.distanceTo(tmpLo);
                            if (TextUtils.isEmpty(address)) {
                                address = tmp2;
                            }else {
                                if (!TextUtils.isEmpty(tmp2)) {
//                                    if (address.length() < tmp2.length()) {
//                                        address = tmp2;
//                                    }
                                    if (distance1 < distance) {
                                        address = tmp2;
                                    }
                                }
                            }
                        }
                        tvAddress.setText(address);
                    }else {
                        tvAddress.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Error");
                error.printStackTrace();
                dialog.dismiss();
            }
        });

        requestQueue.add(stringRequest);

    }

    private void getAddNormal(LatLng latLng){
        new GetAddessAsyn().execute(latLng);
    }

    private void moveToCurrent(){
        if (mLastKnownLocation == null) {
            return;
        }
        if (mMap ==null) {
            return;
        }
        if (!mMap.isMyLocationEnabled()) {
            mMap.setMyLocationEnabled(true);
        }

        LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(mLastKnownLocation.getLatitude(),
//                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
    }

    private class GetAddessAsyn extends AsyncTask<LatLng,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog !=null && !dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(latLngs[0].latitude, latLngs[0].longitude, 1);
                Address address = addresses.get(0);

                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                        sb.append(address.getAddressLine(i) + ",");
                    }
                    return sb.substring(0,sb.length()-1);
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvAddress.setText(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (TextUtils.isEmpty(s)) {
                tvAddress.setText(" ");
                Toast.makeText(MapsActivity.this,R.string.msg_get_address_error,Toast.LENGTH_SHORT).show();
                btnChoose.setBackgroundResource(R.drawable.button_corner_location_disable);
            }else{
                btnChoose.setBackgroundResource(R.drawable.button_corner_location);
            }
        }
    }
}
