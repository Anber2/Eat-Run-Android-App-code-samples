package com.mawaqaa.eatandrun.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
import com.mawaqaa.eatandrun.adapter.ChooseItemSplitAdapter;
import com.mawaqaa.eatandrun.data.OrderStatusData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.mawaqaa.eatandrun.adapter.ChooseItemSplitAdapter.status;

/**
 * Created by HP on 11/27/2017.
 */

public class FragmentChooseItemToSplit extends EatndRunBaseFragment {

    public static final String TAG = "ChooseItemToSplit";
    public static ListView listview_chooseitem;
    public static ChooseItemSplitAdapter chooseitemSplitAdapter;
    public static ArrayList<OrderStatusData> orderStatusDataArrayList;
    public static OrderStatusData orderStatusData;
    public static String message, Success, Price, MenuItemId;
    public static List<String> idsorderID, idsorderPrice, idsorderQty;
    //public static ProgressDialog progressBar;
    public static int checkBoxCounter = 0;
    public static int Number[];
    public static Context context;
    Button btn_submit;
    JSONArray OrderItemListjsonArray, PriceItemListJsonArray;
    JSONObject OrderItemList, PriceItemList;
    Array array;
    String splitType;
    String Decision;


    public static String makeJsonGetOrderItems3StringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;/**/
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

                                    JSONArray jsonArray = jsonObj.getJSONArray("lstItemsToSplit");
                                    orderStatusDataArrayList = new ArrayList<OrderStatusData>();

                                    status = new ArrayList<Boolean>();

                                    Number = new int[jsonArray.length()];
                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        MenuItemId = jsonObject1.getString("MenuItemId");
                                        String MenuItemName = jsonObject1.getString("MenuItemName");
                                        Price = jsonObject1.getString("Price");
                                        Number[t] = jsonObject1.getInt("Quantity");
                                        String Qty = jsonObject1.getString("Quantity");

                                        orderStatusData = new OrderStatusData(MenuItemId, MenuItemName, Price, Qty);
                                        orderStatusDataArrayList.add(orderStatusData);

                                    }

                                    chooseitemSplitAdapter = new ChooseItemSplitAdapter(context, orderStatusDataArrayList);
                                    // chooseitemSplitAdapter.notifyDataSetChanged();

                                    listview_chooseitem.setAdapter(chooseitemSplitAdapter);


                                    if (listview_chooseitem.getCount() == 0) {

                                        Toast.makeText(getApplicationContext(), "No Items!!", Toast.LENGTH_LONG).show();

                                    }


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

        View v = inflater.inflate(R.layout.layout_choose_item_split_listview, container, false);
        initView(v);

        AppConstants.currentClass = "3";

        splitType = "2";

        //  progressBar.dismiss();

        SplitTheBill();

        GetItemsToSplit();


        return v;
    }

    private void GetItemsToSplit() {

        //progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();
                    //jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(getActivity()));
                    jsonObject.putOpt("Rest_ID", PreferenceUtil.getResID(getActivity()));
                    jsonObject.putOpt("OpenBillNumber", PreferenceUtil.getOpenBillNumber(getActivity()));

                    makeJsonGetOrderItems3StringReq(AppConstants.EatndRun_GetItemsToSplit, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    private String makeJsonSplitBillByItemsStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    message = jsonObj.getString("Message");


                                }


                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                                if (message.equals("Success")) {
                                    preauthorization();
                                } else {
                                    btn_submit.setVisibility(View.VISIBLE);
                                }

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
                    // progressBar.dismiss();
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
            // progressBar.dismiss();
            e.toString();
            return e.toString();
        }

        return resultConn[0];


    }

    private void preauthorization() {

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
                    Toast.makeText(getActivity(), xx.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }).start();
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

                                    //Toast.makeText(getActivity(), "ACCEPT", Toast.LENGTH_LONG).show();
                                    Activity.pushFragments(new FragmentChooseItemToSplitConfirm(), false, true);

                                } else {
                                    btn_submit.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), "DECLINED", Toast.LENGTH_LONG).show();
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
            e.toString();
            return e.toString();
        }

        return resultConn[0];


    }


    private void initView(View v) {
        listview_chooseitem = (ListView) v.findViewById(R.id.listview_chooseitem);


        btn_submit = (Button) v.findViewById(R.id.btn_submit);
        btn_submit.setVisibility(View.VISIBLE);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_submit.setVisibility(View.GONE);


                if (SplitBillAuthentication()) {

                    idsorderID = new ArrayList<String>();
                    idsorderPrice = new ArrayList<String>();
                    idsorderQty = new ArrayList<String>();

                    int listPosition = orderStatusDataArrayList.size();


                    Log.d("listPosition", " " + listPosition);

                    for (int t = 0; t < listPosition; t++) {
                        if (status.get(t).equals(true)) {

                            Log.d("isChecked ", " " + status.get(t).equals(true) + " " + t);

                            String orderID = orderStatusDataArrayList.get(t).getMenuItemId();
                            String orderPrice = orderStatusDataArrayList.get(t).getPrice();
                            String Qty = Number[t] + "";
                            Log.d("Qty  ", " " + Qty);

                            idsorderQty.add(Qty);
                            idsorderID.add(orderID);
                            idsorderPrice.add(orderPrice);
                        }
                    }

                    callSplitBillByItems();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.fragmentjoinbillchooseitemtosplit_txt_submit_error_msg), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void callSplitBillByItems() {
        // progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    //jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(getActivity()));
                    jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(getActivity()));
                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getActivity()));
                    jsonObject.putOpt("OrderItemIdList", android.text.TextUtils.join(",", idsorderID));
                    jsonObject.putOpt("OrderItemPriceList", android.text.TextUtils.join(",", idsorderPrice));
                    jsonObject.putOpt("OrderItemQuantities", android.text.TextUtils.join(",", idsorderQty));

                    makeJsonSplitBillByItemsStringReq(AppConstants.EatndRun_SPLITBILLBYITEMS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();

                }

            }
        }).start();
    }

    private void SplitTheBill() {

        //  progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("Cust_ID", PreferenceUtil.getUserId(Activity));
                    //jsonObject.putOpt(AppConstants.EatndRun_ORDERID, PreferenceUtil.getOrderID(Activity));
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
            e.toString();
            return e.toString();
        }

        return resultConn[0];


    }

    private boolean SplitBillAuthentication() {


        boolean ischecked = false;

        for (int t = 0; t < status.size(); t++) {

            if (status.get(t).equals(true)) {

                ischecked = true;

                break;

            }

        }


        return ischecked;

    }
}
