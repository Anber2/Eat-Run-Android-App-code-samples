package com.mawaqaa.eatandrun.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
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
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.fragment.RestaurantListFragment;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by HP on 11/26/2017.
 */

public class PayBillConfirmActivity extends EatndRunMainActivity {
    public static final String TAG = "PayBillConfirm";
    Button payBill_btn_confirm;
    TextView txt_confirm_tableNo, txt_confirm_time, txt_confirm_your_bill_value, txt_confirm_tip_value, txt_confirm_total_value;
    String CreateProfileUri;
    String Success;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_confirm_bill);
        initView();
        getBillSummary();
    }

    private void getBillSummary() {

        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getApplicationContext()));
                    jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(getApplicationContext()));
                    jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(getApplicationContext()));
                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getApplicationContext()));

                    makeJsonGetCustomerBillSummaryStringReq(AppConstants.EatndRun_GETEVENLYCUSTOMERBILLSUMMARY, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

    }
    private String makeJsonGetCustomerBillSummaryStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";

        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {

                                    String Amount = jsonObj.getString("SubAmount");
                                    String TotalAmount = jsonObj.getString("TotalAmount");
                                    String Time = jsonObj.getString("Time");
                                    String TableNo = jsonObj.getString("TableNo");

                                    txt_confirm_tableNo.setText("Table " + TableNo);
                                    txt_confirm_time.setText(Time);
                                    txt_confirm_your_bill_value.setText(Amount);
                                    txt_confirm_tip_value.setText(PreferenceUtil.getTip(getApplicationContext()));
                                    txt_confirm_total_value.setText(TotalAmount);

                                }


                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
                                xx.toString();
                            }
                            progressBar.dismiss();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    progressBar.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

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
            progressBar.dismiss();
            e.toString();
            return e.toString();
        }

        return resultConn[0];


    }

    private void initView( ) {


        txt_confirm_tableNo = (TextView)  findViewById(R.id.txt_confirm_tableNo);
        txt_confirm_time = (TextView)   findViewById(R.id.txt_confirm_time);
        txt_confirm_your_bill_value = (TextView)  findViewById(R.id.txt_confirm_your_bill_value);
        txt_confirm_tip_value = (TextView)  findViewById(R.id.txt_confirm_tip_value);
        txt_confirm_total_value = (TextView)  findViewById(R.id.txt_confirm_total_value);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Fragment fragment =  new RestaurantListFragment();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(RestaurantListFragment.class.getSimpleName())
                .commit();

    }
}