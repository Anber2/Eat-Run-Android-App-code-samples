package com.mawaqaa.eatandrun.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by siva on 11/24/2016.
 */
public class LanguageActivity extends EatndRunBaseActivity {
    private String TAG = "LanguageActivity";
    String message, Success;


    Button Lbtn_Arabic,Lbtn_English, Lbtn_French;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
         Log.d("LAnguageactvty", "onCreate");

        if(!isOnline()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("You are not online!");
            alert.setMessage("Please connect to the internet to use the application");
            alert.setPositiveButton(getString(R.string.string_ok), null);
            alert.show();
        }

        setLanguage();

        Lbtn_English = (Button)findViewById(R.id.Lbtn_English);
        Lbtn_English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                // i.setClass(sPlashScreen, AjaratyMainActivity.class);
                i.setClass(getApplicationContext(), EatndRunMainActivity.class);
                startActivity(i);
            }
        });

        Lbtn_Arabic = (Button)findViewById(R.id.Lbtn_Arabic);

        Lbtn_Arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i = new Intent();
                // i.setClass(sPlashScreen, AjaratyMainActivity.class);
                i.setClass(getApplicationContext(), EatndRunMainActivity.class);
                startActivity(i);
            }
        });


        Lbtn_French = (Button)findViewById(R.id.Lbtn_French);

        Lbtn_French.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                // i.setClass(sPlashScreen, AjaratyMainActivity.class);
                i.setClass(getApplicationContext(), EatndRunMainActivity.class);
                startActivity(i);
            }
        });

    }

    private String makeJsonLanguageStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {


                                    message = jsonObj.getString("Message");

                                    Success = jsonObj.getString("Success");


                                }
                             //   Toast.makeText(LanguageActivity.this, message, Toast.LENGTH_LONG).show();



                            } catch (Exception xx) {
                                Log.e(TAG, "****" + xx.toString());
                                xx.toString();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();

                     runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LanguageActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }) {


                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    try {

                        Iterator<?> keys = jsonObject.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            String value = jsonObject.getString(key);
                            params.put(key, value);

                        }


                    } catch (Exception xx) {
                        xx.toString();
                    }
                    return params;
                }


                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {

                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));

                        return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));


                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }


            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);


        } catch (Exception e) {
            e.toString();

            return e.toString();
        }

        return resultConn[0];

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }

    public void setLanguage(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonLanguageStringReq(AppConstants.EatndRun_LANGUAGELIST, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
