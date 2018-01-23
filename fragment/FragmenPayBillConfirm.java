package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.fragment.FragmentOrderStatus.context;

/**
 * Created by HP on 11/27/2017.
 */

public class FragmenPayBillConfirm extends EatndRunBaseFragment {
    public static final String TAG = "FragmenPayBillConfirm";
    Button payBill_btn_confirm;
    TextView txt_confirm_tableNo, txt_confirm_time, txt_confirm_your_bill_value, txt_confirm_tip_value, txt_confirm_total_value;
    String CreateProfileUri;
    String Success;
    private ProgressDialog progressBar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();
    }

    public void onResume() {
        //   Log.d(TAG, "onResume" + this.getClass().getName());
        Log.e("onResume", "=====  onResume");

        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;

       /* new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getActivity()));

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonGetCurrentOrderStatusByNumberStringReq(AppConstants.EatndRun_GETCURRENTORDERSTATUSBYNUMBER, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }*
        }).start();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_registerorlogin, container, false);
        View v = inflater.inflate(R.layout.layout_confirm_bill, container, false);
        initView(v);

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(context));
                    jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(context));
                    jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(context));
                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(context));

                    makeJsonGetEvenlyCustomerBillSummaryStringReq(AppConstants.EatndRun_GETEVENLYCUSTOMERBILLSUMMARY, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

        return v;
    }

    private String makeJsonGetEvenlyCustomerBillSummaryStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";

        try {
            RequestQueue queue = Volley.newRequestQueue(this.getActivity());

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
                                    txt_confirm_tip_value.setText(PreferenceUtil.getTip(context));
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

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

    private void initView(View v) {


        txt_confirm_tableNo = (TextView) v.findViewById(R.id.txt_confirm_tableNo);
        txt_confirm_time = (TextView) v.findViewById(R.id.txt_confirm_time);
        txt_confirm_your_bill_value = (TextView) v.findViewById(R.id.txt_confirm_your_bill_value);
        txt_confirm_tip_value = (TextView) v.findViewById(R.id.txt_confirm_tip_value);
        txt_confirm_total_value = (TextView) v.findViewById(R.id.txt_confirm_total_value);


        /*payBill_btn_confirm = (Button) v.findViewById(R.id.payBill_btn_confirm);
        payBill_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                            jsonObject.putOpt("SubscriptionID", "");
                            jsonObject.putOpt("orderRequestToken", "");
                            jsonObject.putOpt("Currency", "");
                            jsonObject.putOpt("amount", "");


                            makeJsonPayCustomerOrderStringReq(AppConstants.EatndRun_PAYCUSTOMERORDER, jsonObject);

                        } catch (Exception xx) {
                            xx.toString();
                        }

                    }
                }).start();


            }
        });*/


    }

    private String makeJsonPayCustomerOrderStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";

        try {
            RequestQueue queue = Volley.newRequestQueue(this.getActivity());

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {


                                    CreateProfileUri = jsonObj.getString("UpdateProfileUri");
                                    PreferenceUtil.setProfileUri(getActivity(), CreateProfileUri);


                                }

                                Log.e("CreateProfileUri == ", CreateProfileUri + "&order_id=" + PreferenceUtil.getOrderID(getActivity()) + "&amount=" + PreferenceUtil.getAmount(getActivity()) + "&currency=USD");


                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CreateProfileUri + "&order_id=" + PreferenceUtil.getOrderID(getActivity()) + "&amount=" + PreferenceUtil.getAmount(getActivity()) + "&currency=USD")));


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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

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

    private String makeJsonGetCurrentOrderStatusByNumberStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(Activity);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {
                                    Success = jsonObj.getString("OrderStatus");
                                }

                                if (Success.equals("close")) {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                    alert.setTitle("Transaction Status!");
                                    alert.setMessage("Transaction Successfully Completed!");
                                    alert.setPositiveButton("OK", null);
                                    alert.show();
                                    Activity.pushFragments(new RestaurantListFragment(), false, true);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    progressBar.dismiss();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

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

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause", "=====  onPause");

    }
}
