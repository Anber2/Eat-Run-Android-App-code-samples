package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mawaqaa.eatandrun.adapter.CurrentOrderStatusAdapter;
import com.mawaqaa.eatandrun.data.CurrentOrderStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 11/27/2017.
 */

public class CurrentOrderStatusFragment extends EatndRunBaseFragment {
    public static final String TAG = "CurrentOrderStatus";
    ListView listview_order_status;
    CurrentOrderStatusAdapter orderstausAdapter;
    ArrayList<CurrentOrderStatus> currentOrderStatusArrayList;
    CurrentOrderStatus currentOrderStatus;
    private ProgressDialog progressBar;

    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;
        //((AjaratyMainActivity) getActivity()).hideLogo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_current_order_status, container, false);
        initView(v);
        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonCurrentOrderStatusStringReq(AppConstants.EatndRun_GETCURRENTORDERSTATUSLIST, jsonObject);
                    // startSpinwheel(false, true);


                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();


        return v;
    }

    private String makeJsonCurrentOrderStatusStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    JSONArray jsonArray = jsonObj.getJSONArray("lstOrderListStatus");
                                    currentOrderStatusArrayList = new ArrayList<CurrentOrderStatus>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        String orderId = jsonObject1.getString("OpenBillNumber");
                                        String OrderDateTime = jsonObject1.getString("OrderDateTime");
                                        String Price = jsonObject1.getString("Price");


                                        String orderStatus = jsonObject1.getString("Status");

                                        currentOrderStatus = new CurrentOrderStatus(orderId, OrderDateTime, Price, orderStatus);

                                        currentOrderStatusArrayList.add(currentOrderStatus);


                                    }

                                    orderstausAdapter = new CurrentOrderStatusAdapter(CurrentOrderStatusFragment.this.Activity, currentOrderStatusArrayList);

                                    if (orderstausAdapter != null) {
                                        listview_order_status.setAdapter(orderstausAdapter);
                                        orderstausAdapter.notifyDataSetChanged();

                                        stopSpinWheel();

                                    }

                                    if (listview_order_status.getCount() == 0) {
                                        Toast.makeText(getActivity(), "List is currently empty", Toast.LENGTH_LONG).show();

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


    private void initView(View v) {
        listview_order_status = (ListView) v.findViewById(R.id.listview_order_status);
       /* order_status_list=new ArrayList<>();
        orderstausAdapter=new CurrentOrderStatusAdapter(Activity,order_status_list);
        listview_order_status.setAdapter(orderstausAdapter);
        orderstausAdapter.notifyDataSetChanged();*/
    }
}
