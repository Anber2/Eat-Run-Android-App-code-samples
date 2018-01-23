package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

/**
 * Created by HP on 11/27/2017.
 */

public class FragmentJoinBillChooseItemToSplit extends EatndRunBaseFragment {

    public static final String TAG = "ChooseItemToSplit";
    private static int checkBoxCounter = 0;
    ListView listview_chooseitem;
    Button btn_submit;
    ChooseItemSplitAdapter chooseitemSplitAdapter;
    ArrayList<OrderStatusData> orderStatusDataArrayList;
    OrderStatusData orderStatusData;
    String message, Success, Price, MenuItemId;
    JSONArray OrderItemListjsonArray, PriceItemListJsonArray;
    JSONObject OrderItemList, PriceItemList;

    List<String> idsorderID, idsorderPrice;
    Array array;
    private ProgressDialog progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
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

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getActivity()));
                    jsonObject.putOpt("RestaurantId", PreferenceUtil.getUserId(getActivity()));

                    makeJsonGetOrderItemsStringReq(AppConstants.EatndRun_GETORDERITEMSTOJOIN, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

        return v;
    }

    private String makeJsonGetOrderItemsStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    JSONArray jsonArray = jsonObj.getJSONArray("menuItemList");
                                    orderStatusDataArrayList = new ArrayList<OrderStatusData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        MenuItemId = jsonObject1.getString("MenuItemId");
                                        String MenuItemName = jsonObject1.getString("MenuItemName");
                                        Price = jsonObject1.getString("Price");
                                        String isActive = jsonObject1.getString("IsActive");

                                        PreferenceUtil.setIsActive(Activity, isActive);

                                        orderStatusData = new OrderStatusData(MenuItemId, MenuItemName, Price);

                                        orderStatusDataArrayList.add(orderStatusData);


                                    }

                                    chooseitemSplitAdapter = new ChooseItemSplitAdapter(Activity, orderStatusDataArrayList);

                                    listview_chooseitem.setAdapter(chooseitemSplitAdapter);

                                    if (listview_chooseitem.getCount() == 0) {

                                        Toast.makeText(getActivity(), "No Items!!", Toast.LENGTH_LONG).show();

                                    }
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


                                //Toast.makeText(getActivity(), message , Toast.LENGTH_LONG).show();

                                if (message.equals("Success"))
                                    Activity.pushFragments(new FragmentChooseItemToSplitConfirm(), false, true);


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
        listview_chooseitem = (ListView) v.findViewById(R.id.listview_chooseitem);


        btn_submit = (Button) v.findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                idsorderID = new ArrayList<String>();
                idsorderPrice = new ArrayList<String>();


                int listChild = listview_chooseitem.getCount();
                for (int i = 0; i < listChild; i++) {
                    LinearLayout relative = (LinearLayout) listview_chooseitem.getChildAt(i);
                    CheckBox cb = (CheckBox) relative.getChildAt(0);

                    if (cb.getId() == R.id.checkBox_item) {
                        if (cb.isChecked()) {
                            String orderID = orderStatusDataArrayList.get(i).getMenuItemId();
                            String orderPrice = orderStatusDataArrayList.get(i).getPrice();

                            idsorderID.add(orderID);
                            idsorderPrice.add(orderPrice);


                        }

                    }

                }


                callSplitBillByItems();


            }
        });

    }

    private void callSplitBillByItems() {
        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(getActivity()));
                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getActivity()));
                    //jsonObject.putOpt("OrderItemList", "['xxx', 'yy']");
                    jsonObject.putOpt("OrderItemIdList", android.text.TextUtils.join(",", idsorderID));
                    jsonObject.putOpt("OrderItemPriceList", android.text.TextUtils.join(",", idsorderPrice));


                    makeJsonSplitBillByItemsStringReq(AppConstants.EatndRun_SPLITBILLBYITEMS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }
}
