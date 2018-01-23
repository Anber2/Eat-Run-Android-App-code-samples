package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP on 11/27/2017.
 */

public class FragmenSplitBillEvently extends EatndRunBaseFragment implements View.OnClickListener {


    public static final String TAG = "FragmenSplitBillEvently";
    public static TextView txt_OrderStatus_totalBillAmount, txt_OrderStatus_your_BillAmount;
    public static ListView listvorderstatus_evenly;
    public static OrderStatusAdapter orderStatusAdapter;
    public static ArrayList<OrderStatusData> orderStatusDataArrayList;
    public static OrderStatusData orderStatusData;
    public static ProgressDialog progressBar;
    public static TextView txt_split_evenky_confirm_tableNo, txt_split_evenky_confirm_time, txt_split_evenky_confirm_your_bill_value, txt_split_evenky_confirm_tip_value, txt_split_evenky_confirm_total_value;
    public static Context context;
    Button btn_splitbill_submit;
    EditText txt_splitbill_count;
    String message, Success;
    String Decision;
    String splitType;

    public static String makeJsonGetOrderItems2StringReq(String urlPost, final JSONObject jsonObject) {
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


                                /*    String Total = jsonObj.getString("Total");

                                    String SubTotal = jsonObj.getString("SubTotal");
                                    txt_OrderStatus_your_BillAmount.setText("KD " + SubTotal);
                                    PreferenceUtil.setAmount(context, Total);

                                    txt_OrderStatus_totalBillAmount.setText("KD " + Total);*/

                                    JSONArray jsonArray = jsonObj.getJSONArray("menuItemList");
                                    orderStatusDataArrayList = new ArrayList<OrderStatusData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        String MenuItemId = jsonObject1.getString("MenuItemId");
                                        String MenuItemName = jsonObject1.getString("MenuItemName");
                                        String Price = jsonObject1.getString("Price");
                                        String Qty = jsonObject1.getString("Qty");
                                        String isActive = jsonObject1.getString("IsActive");
                                        PreferenceUtil.setIsActive(context, isActive);

                                        orderStatusData = new OrderStatusData(MenuItemId, MenuItemName, Price);

                                        orderStatusDataArrayList.add(orderStatusData);


                                    }

                                    orderStatusAdapter = new OrderStatusAdapter(context, orderStatusDataArrayList);

                                    listvorderstatus_evenly.setAdapter(orderStatusAdapter);

                                    //  progressBar.dismiss();

                                    // getBillSammary();


                                }

                            } catch (Exception xx) {
                                xx.toString();
                            }
                            //   progressBar.dismiss();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    //  progressBar.dismiss();


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
            //  progressBar.dismiss();
            e.toString();
            return e.toString();
        }

        return resultConn[0];


    }

    public static void getBillSammary() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getApplicationContext()));
                    //  jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(getApplicationContext()));
                    jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(getApplicationContext()));
                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getApplicationContext()));

                    makeJsonGetEvenlyCustomerBillSummaryStringReq(AppConstants.EatndRun_GETEVENLYCUSTOMERBILLSUMMARY, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

    }

    public static String makeJsonGetEvenlyCustomerBillSummaryStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    txt_split_evenky_confirm_tableNo.setText("Table " + TableNo);
                                    txt_split_evenky_confirm_time.setText(Time);
                                    txt_split_evenky_confirm_your_bill_value.setText(Amount);
                                    txt_split_evenky_confirm_tip_value.setText(PreferenceUtil.getTip(context));

                                    txt_split_evenky_confirm_total_value.setText(TotalAmount);


                                }

                                //     progressBar.dismiss();

                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
                                xx.toString();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();


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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        context = getApplicationContext();

        Activity = (EatndRunBaseActivity) this.getActivity();
    }

    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;
        //((AjaratyMainActivity) getActivity()).hideLogo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_registerorlogin, container, false);
        View v = inflater.inflate(R.layout.layout_splitbillevently, container, false);
        initView(v);

        AppConstants.currentClass = "2";

        splitType = "1";

        SplitTheBill();

        getBillSammary();

        //FragmentOrderStatus.getOrderItemsList();

        return v;
    }

    private void initView(View v) {
        btn_splitbill_submit = (Button) v.findViewById(R.id.btn_splitbill_submit);
        // txt_splitbill_count = (EditText) v.findViewById(R.id.txt_splitbill_count);
        btn_splitbill_submit.setVisibility(View.VISIBLE);


        listvorderstatus_evenly = (ListView) v.findViewById(R.id.listvorderstatus_evenly);

        txt_split_evenky_confirm_tableNo = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_tableNo);
        txt_split_evenky_confirm_time = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_time);
        txt_split_evenky_confirm_your_bill_value = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_your_bill_value);
        txt_split_evenky_confirm_tip_value = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_tip_value);
        txt_split_evenky_confirm_total_value = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_total_value);


        // progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getApplicationContext()));
                    jsonObject.putOpt("CustomerId",  PreferenceUtil.getUserId(getActivity()) );

                    makeJsonGetOrderItems2StringReq(AppConstants.EatndRun_GETORDERITEMS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                    //  progressBar.dismiss();
                }

            }
        }).start();


        btn_splitbill_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        btn_splitbill_submit.setVisibility(View.GONE);

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


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


        // if (SplitCountAuthentication()) {

           /* progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        JSONObject jsonObject = new JSONObject();

                        jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                        jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getActivity()));
                        jsonObject.putOpt("SplitCount", txt_splitbill_count.getText().toString());


                        makeJsonSplitBillEvenlyStringReq(AppConstants.EatndRun_SPLITBILLEVENLY, jsonObject);

                    } catch (Exception xx) {
                        xx.toString();
                    }

                }
            }).start();*/

        //    }

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
                                    btn_splitbill_submit.setVisibility(View.VISIBLE);


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

    private void SplitTheBill() {

        //progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    // jsonObject.putOpt(AppConstants.EatndRun_ORDERID, PreferenceUtil.getOrderID(Activity));
                    jsonObject.putOpt("Cust_ID", PreferenceUtil.getUserId(Activity));

                    jsonObject.putOpt("Rest_ID", PreferenceUtil.getResID(Activity));
                    jsonObject.putOpt("OpenBillNumber", PreferenceUtil.getOpenBillNumber(Activity));

                    jsonObject.putOpt("SplitType", splitType);

                    makeJson_SplitTheBill_StringReq(AppConstants.EatndRun_SplitTheBill, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    private String makeJson_SplitTheBill_StringReq(String urlPost, final JSONObject jsonObject) {
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
