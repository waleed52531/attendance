package com.example.attendance.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.attendance.Loader.Loading;
import com.example.attendance.R;
import com.example.attendance.SharedPreference.DataProccessor;
import com.example.attendance.Utilis.GpsTracker;
import com.example.attendance.WebServices.ConstantsAPIS;
import com.example.attendance.WebServices.MySingleton;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DataProccessor dataProccessor = new DataProccessor(this);
    /* access modifiers changed from: private */
    private DrawerLayout drawer;
    GpsTracker gpsTracker;
    private androidx.appcompat.widget.Toolbar home_toolbar;
    private Loading loading;
    private NavigationView nav_view;
    private ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout checkIn, checkOut, locationReload, card_layout_leave, card_layout_attendance, card_layout_salary, card_layout_loan, card_layout_news, card_layout_tracking, Clock;
    TextView timeIn,timeOut,Time,textview;
    private double distance;
    private Location startPoint, endPoint;
    private String current;

    public void LoadingAlert() {
        final Loading loading2 = new Loading(this);
        loading2.startLoading();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                loading2.dismissDialog();
            }
        }, 3000);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_dashboard);
        LoadingAlert();
        init();
        setUpNavs();
        btn_click();
//        loadData();
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//               getCurrentLocation();
//            }
//        }, 3000);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btn_click() {

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCheckIn();
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCheckOut();
            }
        });

        locationReload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        card_layout_leave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });

        card_layout_attendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });

        card_layout_salary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });
        card_layout_loan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });
        card_layout_news.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });
        card_layout_tracking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                overridePendingTransition(0, 0);
//                getCurrentLocation();
                overridePendingTransition(0, 0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void init() {
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.home_toolbar);
        home_toolbar = toolbar;
        toolbar.setTitle("Movex");
        home_toolbar.setTitleTextColor(Color.WHITE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        checkIn = (LinearLayout) findViewById(R.id.check_in);
        checkOut = (LinearLayout) findViewById(R.id.check_out);
        Time = (TextView) findViewById(R.id.clock);
        locationReload = (LinearLayout) findViewById(R.id.location_reload);
        Clock = (LinearLayout) findViewById(R.id.reload_time);
        textview = (TextView) findViewById(R.id.myOriginDistance);
        card_layout_leave = (LinearLayout) findViewById(R.id.card_layout_leave);
        card_layout_attendance = (LinearLayout) findViewById(R.id.card_layout_attendance);
        card_layout_salary = (LinearLayout) findViewById(R.id.card_layout_salary);
        card_layout_loan = (LinearLayout) findViewById(R.id.card_layout_loan);
        card_layout_news = (LinearLayout) findViewById(R.id.card_layout_news);
        card_layout_tracking = (LinearLayout) findViewById(R.id.card_layout_tracking);
    }

    private void setUpNavs() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, home_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList((ColorStateList) null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_about:
                showDialog();
                break;
            case R.id.nav_home:
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.nav_tracking:
                showDialog();
                break;
        }
        drawer.closeDrawer((int) GravityCompat.START);
        return false;
    }

    /* access modifiers changed from: private */
    public void showDialog() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Notification").setMessage((CharSequence) "Service under process!").setPositiveButton((CharSequence) "ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void radiusAlert() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Notification").setMessage((CharSequence) "not in radius").setPositiveButton((CharSequence) "ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        for (NetworkInfo ni : ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                haveConnectedWifi = true;
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                haveConnectedMobile = true;
            }
        }
        if (haveConnectedWifi || haveConnectedMobile) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void wifiAlert() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Network Error").setMessage((CharSequence) "Please make sure you are connected to internet!").setPositiveButton("ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void ApiCheckIn(){
//        final TextView textView = (TextView) findViewById(R.id.text);
        timeIn = (TextView) findViewById(R.id.time_in);

// ...

// Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantsAPIS.CHECK_IN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        timeIn.setVisibility(View.VISIBLE);
                        timeIn.setText("Response is: " + response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timeIn.setText("error");
            }
        });

// Add the request to the RequestQueue.
//        queue.add(stringRequest);

        MySingleton.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);
    }

    private void ApiCheckOut(){
//        final TextView textView = (TextView) findViewById(R.id.text);
        timeOut = (TextView) findViewById(R.id.time_out);

// ...

// Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantsAPIS.CHECK_IN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        timeOut.setVisibility(View.VISIBLE);
                        timeOut.setText("Response is: " + response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timeOut.setText("error");
            }
        });

// Add the request to the RequestQueue.
//        queue.add(stringRequest);

        MySingleton.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);
    }

    public void getCurrentLocation() {
        GpsTracker gpsTracker2 = new GpsTracker(this);
        this.gpsTracker = gpsTracker2;
        if (gpsTracker2.canGetLocation()) {
            double longitude = this.gpsTracker.getLongitude();
            double latitude = this.gpsTracker.getLatitude();
            Location location = new Location("locationA");
            startPoint = location;
            location.setLatitude(latitude);
            startPoint.setLongitude(longitude);
            Location location2 = new Location("locationB");
            endPoint = location2;
            location2.setLatitude(latitude);
            endPoint.setLongitude(longitude);
            distance = (double) this.startPoint.distanceTo(this.endPoint);
            textview.setText(String.format("%.0f", new Object[]{Double.valueOf(this.distance)}) + " meters");
            current = String.format("%.0f", new Object[]{Double.valueOf(this.distance)});
            return;
        }
        this.gpsTracker.showSettingsAlert();
    }

//    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
//        int Radius = 6371;// radius of earth in Km
//        double lat1 = StartP.latitude;
//        double lat2 = EndP.latitude;
//        double lon1 = StartP.longitude;
//        double lon2 = EndP.longitude;
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1))
//                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
//                * Math.sin(dLon / 2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        double valueResult = Radius * c;
//        double km = valueResult / 1;
//        DecimalFormat newFormat = new DecimalFormat("####");
//        int kmInDec = Integer.valueOf(newFormat.format(km));
//        double meter = valueResult % 1000;
//        int meterInDec = Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec);
//
//        return Radius * c;
//    }
}