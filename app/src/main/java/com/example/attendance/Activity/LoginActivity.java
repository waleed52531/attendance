package com.example.attendance.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.attendance.R;
import com.example.attendance.SharedPreference.DataProccessor;
import com.example.attendance.WebServices.ConstantsAPIS;
import com.example.attendance.WebServices.MySingleton;
import com.google.android.material.textfield.TextInputLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "wow123";
    //    private KProgressHUD progressHUD;

    TextInputLayout ilCode, ilPassword, ilImei;
    EditText etCode, etPassword, etimei, etdeviceId;
    Button login_btn;
    private int a = 0;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DataProccessor dataProccessor = new DataProccessor(this);
        init();
        idCall();
//        getImei();
        clicks();


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }

    private void idCall(){
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        etdeviceId.setText(deviceId);
        Log.d("tag", "etdeviceId: " + deviceId);
    }

    private boolean checkInternet() {
        ConnectivityManager mgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert mgr != null;
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {
                // Internet Available
//                Toast.makeText(this, "internet is connected", Toast.LENGTH_SHORT).show();
            } else {
                //No internet
                Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            //No internet
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    private void clicks() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_validation()) {
                    DataProccessor.setStr(ConstantsAPIS.LOGIN_KEY, Objects.requireNonNull(etCode.getText()).toString());
                    if (checkInternet()) {
                        ApiCall();
                        Log.d("tag", "etdeviceId: " + deviceId);

                    }
                }
            }
        });
    }

    private void ApiCall() {
//        progressHUD.show();
        final String accountNo = Objects.requireNonNull(etCode.getText()).toString().toUpperCase().trim();
        final String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
        final String device_id = etdeviceId.getText().toString().trim();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("account_no", accountNo.toUpperCase());
            jsonObject.put("password", password);
            jsonObject.put("imei_no",device_id);
            Log.d(TAG, "ApiCall: " + accountNo);
            Log.d(TAG, "ApiCall: " + password);
            Log.d(TAG, "ApiCall: " + device_id);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ConstantsAPIS.LOG_IN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: Sign In" + response);
//                progressHUD.dismiss();

                String api_key;
                try {
                    if (response.getInt("http-response") == 200) {
                        api_key = response.getJSONObject("response").getString("api_key");
                        DataProccessor.setStr(ConstantsAPIS.LOGIN_API_KEY, api_key);
                        DataProccessor.setInt(ConstantsAPIS.LOGIN_KEY, 100);
//                        Toast.makeText(LoginActivity.this, "API KEY:" + api_key, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User Login API key onResponse: " + api_key);
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                        Log.d(TAG, "response");

                    } else {
                        Log.d(TAG, "Error: " + response.getString("message"));
                        Toast.makeText(LoginActivity.this, "Error: Not 200 " + response.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(LoginActivity.this, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressHUD.dismiss();

//                Toast.makeText(LoginActivity.this, "Error: onErrorResponse"+error.getMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(LoginActivity.this, "Error: onErrorResponse"+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse: SignIn" + error.getMessage());
                try {
//                    if (error.networkResponse.data.length > 0) {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Toast.makeText(LoginActivity.this, "Error:" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "JSONException Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(LoginActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Error: " + error
//                        + ">>" + error.networkResponse.statusCode
//                        + ">>" + error.networkResponse.data
//                        + ">>" + error.getCause()
//                        + ">>" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers.isEmpty() ? headers : super.getHeaders();

            }
        };
        jsonObjectRequest.setRetryPolicy(new

                                                 RetryPolicy() {
                                                     @Override
                                                     public int getCurrentTimeout() {
                                                         return 50000;
                                                     }

                                                     @Override
                                                     public int getCurrentRetryCount() {
                                                         return 50000;
                                                     }

                                                     @Override
                                                     public void retry(VolleyError error) {

                                                     }
                                                 });
        MySingleton.getInstance(LoginActivity.this).

                addToRequestQueue(jsonObjectRequest);

    }

    private void init() {


        ilCode = findViewById(R.id.il_account);
        ilPassword = findViewById(R.id.il_password);
        etCode = findViewById(R.id.accountNo);
        etPassword = findViewById(R.id.password);
        etdeviceId = findViewById(R.id.imeiNo);
        login_btn = findViewById(R.id.btn_login);
    }


    private void setErrorMessage(EditText editText, TextInputLayout layout, String error) {
        editText.requestFocus();
        layout.setError(error);
    }

    private boolean check_validation() {
        if (Objects.requireNonNull(etCode.getText()).toString().isEmpty()) {
            setErrorMessage(etCode, ilCode, "Courier Code Required");
            return false;
        } else {
            ilCode.setErrorEnabled(false);
        }
        if (Objects.requireNonNull(etPassword.getText()).toString().isEmpty()) {
            setErrorMessage(etPassword, ilPassword, "Password Required");
            return false;
        } else if (etPassword.getText().toString().length() < 6) {
            setErrorMessage(etPassword, ilPassword, "Password should be 5 characters long");
            return false;
        } else {
            ilPassword.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (a == 0) {
            a = 1;
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            a = 0;
//            Intent a = new Intent(Intent.ACTION_MAIN);
//            a.addCategory(Intent.CATEGORY_HOME);
//            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(a);
//            finish();
            moveTaskToBack(true);
        }

    }


//    @SuppressLint("HardwareIds")
//    private String getImei() {
//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        imeiID = null;
//        int readIMEI = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//        if (imeiID == null) {
//            if (readIMEI == PackageManager.PERMISSION_GRANTED) {
//                imeiID = tm.getImei();
//            }
//        }
//        return imeiID;
//    }

}



