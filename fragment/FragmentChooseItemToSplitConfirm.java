package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
import com.mawaqaa.eatandrun.adapter.OrderStatusAdapter;
import com.mawaqaa.eatandrun.data.OrderStatusData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 11/27/2017.
 */

public class FragmentChooseItemToSplitConfirm extends EatndRunBaseFragment {
    public static final String TAG = "FragmentJoinBill";
    ListView listview_choose_item_confirm;

    TextView txt_table_ChooseItemToSplitConfirm, txt_time_ChooseItemToSplitConfirm, txt_your_bill_value_ChooseItemToSplitConfirm, txt_tip_value_ChooseItemToSplitConfirm, txt_total_value_ChooseItemToSplitConfirm;

    OrderStatusAdapter orderStatusAdapter;
    ArrayList<OrderStatusData> orderStatusDataArrayList;
    OrderStatusData orderStatusData;

    Button pay_btn_ChooseItemToSplitConfirm;
    String CreateProfileUri;

    String message, Success;
    private ProgressDialog progressBar;

    String Decision;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();
    }


    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
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

            }
        }).start();*/
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
                                    Success = jsonObj.getString("Success");
                                }

                                if (!Success.equals("Failed")) {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_listview_confirm_bill, container, false);
        initView(v);

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                   /* jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(getActivity()));
                    jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(getActivity()));*/
                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getActivity()));


                    makeJsonGetItemBaseCustomerBillSummaryStringReq(AppConstants.EatndRun_GETITEMBASECUSTOMERBILLSUMMARY, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

        return v;
    }

    private void initView(View v) {

        txt_table_ChooseItemToSplitConfirm = (TextView) v.findViewById(R.id.txt_table_ChooseItemToSplitConfirm);
        txt_time_ChooseItemToSplitConfirm = (TextView) v.findViewById(R.id.txt_time_ChooseItemToSplitConfirm);
        txt_your_bill_value_ChooseItemToSplitConfirm = (TextView) v.findViewById(R.id.txt_your_bill_value_ChooseItemToSplitConfirm);
        txt_tip_value_ChooseItemToSplitConfirm = (TextView) v.findViewById(R.id.txt_tip_value_ChooseItemToSplitConfirm);
        txt_total_value_ChooseItemToSplitConfirm = (TextView) v.findViewById(R.id.txt_total_value_ChooseItemToSplitConfirm);


        listview_choose_item_confirm = (ListView) v.findViewById(R.id.listview_choose_item_confirm);


        pay_btn_ChooseItemToSplitConfirm = (Button) v.findViewById(R.id.pay_btn_ChooseItemToSplitConfirm2);

        listview_choose_item_confirm.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


       /* pay_btn_ChooseItemToSplitConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject();


                            jsonObject.putOpt("Cust_ID", PreferenceUtil.getUserId(Activity));
                            //jsonObject.putOpt("OrderID", PreferenceUtil.getOrderID(getActivity()));
                            // jsonObject.putOpt("Rest_ID", PreferenceUtil.getResID(getActivity()));
                            jsonObject.putOpt("OpenBillNumber", PreferenceUtil.getOpenBillNumber(getActivity()));

                            makeJsonPreauthorizationStringReq(AppConstants.EatndRun_PreauthorizationUser, jsonObject);

                        } catch (Exception xx) {
                            progressBar.dismiss();
                            Toast.makeText(getActivity(), xx.toString(), Toast.LENGTH_LONG).show();

                        }

                    }
                }).start();


            }
        });*/

    }

    private String makeJsonPreauthorizationStringReq(String urlPost, final JSONObject jsonObject) {
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
                                if (response != null) {

                                    Decision = jsonObj.getString("Result");
                                }

                                // Toast.makeText(getActivity(), Decision, Toast.LENGTH_LONG).show();

                                if (Decision.equals("ACCEPT")) {

                                    Toast.makeText(getActivity(), "ACCEPT", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getActivity(), "DECLINED", Toast.LENGTH_LONG).show();
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


    private String makeJsonGetItemBaseCustomerBillSummaryStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    txt_table_ChooseItemToSplitConfirm.setText("Table " + TableNo);
                                    txt_time_ChooseItemToSplitConfirm.setText(Time);
                                    txt_your_bill_value_ChooseItemToSplitConfirm.setText(Amount);
                                    txt_tip_value_ChooseItemToSplitConfirm.setText(PreferenceUtil.getTip(getActivity()));
                                    txt_total_value_ChooseItemToSplitConfirm.setText(TotalAmount);

                                    JSONArray jsonArray = jsonObj.getJSONArray("lstCurrentMenuItems");
                                    orderStatusDataArrayList = new ArrayList<OrderStatusData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);


                                        String MenuItemId = jsonObject1.getString("MenuItemId");
                                        String MenuItemName = jsonObject1.getString("MenuItemName");
                                        String Price = jsonObject1.getString("Price");


                                        orderStatusData = new OrderStatusData(MenuItemId, MenuItemName, Price);

                                        orderStatusDataArrayList.add(orderStatusData);


                                    }

                                    orderStatusAdapter = new OrderStatusAdapter(Activity, orderStatusDataArrayList);

                                    listview_choose_item_confirm.setAdapter(orderStatusAdapter);

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


}
