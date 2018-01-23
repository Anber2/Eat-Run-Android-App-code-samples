package com.mawaqaa.eatandrun.fbgmsservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.volley.VolleyUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by JijoCJ on 12/20/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    public static String refreshedToken;
    Activity activity;

    public String m_deviceID_;

    // private ProgressDialog progressBar;

    public String getDeviceID(Context p_context) throws Throwable {
        String m_deviceID = null;
        TelephonyManager m_telephonyManager = null;
        m_telephonyManager = (TelephonyManager) p_context
                .getSystemService(Context.TELEPHONY_SERVICE);

        m_deviceID = m_telephonyManager.getDeviceId().toString();

        if (m_deviceID == null || "00000000000000".equalsIgnoreCase(m_deviceID)) {
            m_deviceID = "AAAAAAA";
        }

         return m_deviceID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyUtils.init(this);
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        PreferenceUtil.setDeviceToken(getApplicationContext(),""+refreshedToken);

        // sending reg id to your server
        // sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(AppConstants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        Log.d(TAG, "Token: " + refreshedToken);

        //progressBar = ProgressDialog.show(this, "", "Please Wait ...", true, false);

       /* new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();


                    jsonObject.putOpt("Cus_Id", PreferenceUtil.getUserId(getApplicationContext()));
                    jsonObject.putOpt(AppConstants.EatndRun_DEVICEREGISTRATION_DEVICE_PLATFORM, "0");
                    jsonObject.putOpt("DeviceId", getDeviceID(getApplicationContext()));
                    jsonObject.putOpt(AppConstants.EatndRun_DEVICEREGISTRATION_DEVICE_TOKEN, refreshedToken);
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonUpdateTokenIdStringReq(AppConstants.EatndRun_DEVICEREGISTRATION, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }).start();*/

    }


//    public void sendRegistrationToServer(final String token) {
//        // sending fcm token to server
//        try {
//            Log.e(TAG, "sendRegistrationToServer: " + token);
//            if(!PreferenceUtil.getUserId(this).equals("")){
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.putOpt(AppConstants.DEVICE_ID, Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
//                jsonObject.putOpt(AppConstants.DEVICE_TOKEN, token);
//                jsonObject.putOpt(AppConstants.DEVICE_PLATFORM, 0);
//                jsonObject.putOpt(AppConstants.USER_ID, PreferenceUtil.getUserId(this));
//                Log.e(TAG, "Json Req:"+ jsonObject);
//                if (VolleyUtils.volleyEnabled) {
//                    //Activity.BaseFragment.startSpinwheel(false, true);
//
//                    RequestQueue queue = VolleyUtils.getRequestQueue();
//                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(
//                            Request.Method.POST, AppConstants.HP_DEVICE_REGISTRATION, jsonObject,
//                            new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    Log.e(TAG,
//                                            "Response : Device Registration" + response.toString());
//                                    try {
//                                        Log.e(TAG, response.getString(AppConstants.MESSAGE));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    //onSaveMyAccountInfoSuccessfully(response);
//                                }
//
//
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e(TAG, "Response : Error Device Registration"+error.getMessage());
//
//                        }
//                    });
//                    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 2, 2));
//                    queue.add(jsObjRequest);
//
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private String makeJsonUpdateTokenIdStringReq(String urlPost, final JSONObject jsonObject) {
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
                                JSONObject jsonObjResp = new JSONObject(response);

                                Log.d(TAG, "Token update jsonObjResp: " + jsonObjResp);


                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
                                xx.toString();
                            }
                            //progressBar.dismiss();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    //progressBar.dismiss();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();

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
            //progressBar.dismiss();
            e.toString();
            return e.toString();
        }

        return resultConn[0];

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConstants.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    public interface DeviceRegistrationListener {
        public void onDeviceRegistration(String token);
    }

}