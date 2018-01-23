package com.mawaqaa.eatandrun.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
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
 * Created by HP on 11/27/2017.
 */
public class EatndRunSplashActivity extends EatndRunBaseActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "gCQvB4kb87e0FkY1NST2vSwlA";
    private static final String TWITTER_SECRET = "XBPW9wIdTsmR7DmuFQ0gGqk19g2dhFuijUMPcJYwI6RbfXc68G";
    protected int _splashTime = 3000;
    String TAG = "EatndRunSplashActivity";
    RelativeLayout mainLO;
    Animation anim;
    Context context;
    private Thread splashTread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));*/
        setContentView(R.layout.activity_splash);
        // setContentView(R.layout.fragment_registerorlogin);
        Log.d("SplashActivity", "onCreate");


        mainLO = (RelativeLayout) findViewById(R.id.mainSplashLO);
        anim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        mainLO.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(EatndRunSplashActivity.this, LanguageActivity.class);
                startActivity(i);

                finish();
            }
        }, _splashTime);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();


                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


                    makeJsonGetGlobalParamsStringReq(AppConstants.EatndRun_APIGETGLOBALPARAMS, jsonObject);

                } catch (Exception xx) {

                }

            }
        }).start();

    }


    private String makeJsonGetGlobalParamsStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    AppConstants.EatndRun_UserCommissionFee = jsonObj.getString("UserCommissionFee");
                                    AppConstants.EatndRun_Company_CommissionFee = jsonObj.getString("RestaurantCommissionFee");
                                    AppConstants.EatndRun_KNET_CardCommissionFee = jsonObj.getString("Visa_CardCommissionFee");
                                    AppConstants.EatndRun_CardCommissionFee = jsonObj.getString("Master_CardCommissionFee");
                                    Log.d(TAG,"UserCommissionFee"+AppConstants.EatndRun_UserCommissionFee);
                                }



                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
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
                            Toast.makeText(getApplicationContext(), R.string.string_semething_went_wrong, Toast.LENGTH_LONG).show();

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


}
