package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.mawaqaa.eatandrun.adapter.OrderHistoryDetailsAdapter;
import com.mawaqaa.eatandrun.data.OrderHistoryDetailsData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.Constants.AppConstants.openBillNoHistoryDetails;


/**
 * Created by HP on 11/27/2017.
 */

public class OrderHistoryDetailsFragment extends EatndRunBaseFragment {
    public static final String TAG = "HistoryDetailsFragment";
    ListView listview_orderdetails;
    OrderHistoryDetailsAdapter orderlistdapter;
    OrderHistoryDetailsData orderStatusData;
    ArrayList<OrderHistoryDetailsData> orderStatusDataArrayList;
    TextView txt_NoOfCust_value, txt_tableNo_valuew, txt_waiterid_value, txt_waitername_value, txt_resturant_value, txt_orderid_currentOrderStatus;
    private ProgressDialog progressBar;

    Button btn_totoalAmount;

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

        View v = inflater.inflate(R.layout.layout_orderhistory_details, container, false);
        initView(v);

        getOrderHistoryDetails();
        return v;
    }

    private void getOrderHistoryDetails() {

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("Cus_Id", PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt("OpenBillNumber", openBillNoHistoryDetails);
                    jsonObject.putOpt("LanguageId", PreferenceUtil.getLanguage(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonOrderHistoryDetailsStringReq(AppConstants.EatndRun_GETPAYMENTHISTORYDETAILS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    private void initView(View v) {
        listview_orderdetails = (ListView) v.findViewById(R.id.listview_orderdetails);
   /*     View footerView = ((LayoutInflater) Activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_listview_footer, null, false);
        listview_orderdetails.addFooterView(footerView);*/

        btn_totoalAmount = (Button) v.findViewById(R.id.btn_totoalAmount);

        txt_NoOfCust_value = (TextView) v.findViewById(R.id.txt_NoOfCust_value);

        txt_tableNo_valuew = (TextView) v.findViewById(R.id.txt_tableNo_valuew);
        txt_waiterid_value = (TextView) v.findViewById(R.id.txt_waiterid_value);
        txt_waitername_value = (TextView) v.findViewById(R.id.txt_waitername_value);
        txt_resturant_value = (TextView) v.findViewById(R.id.txt_resturant_value);
        txt_orderid_currentOrderStatus = (TextView) v.findViewById(R.id.txt_orderid_currentOrderStatus);


    }

    private String makeJsonOrderHistoryDetailsStringReq(String urlPost, final JSONObject jsonObject) {
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

                                  /* txt_tableNo_valuew, txt_waiterid_value, txt_waitername_value, txt_resturant_value, txt_orderid_currentOrderStatus ;*/

                                    txt_NoOfCust_value.setText(jsonObj.getString("NumberOfCustomers"));
                                    txt_tableNo_valuew.setText(jsonObj.getString("TableNumber"));
                                    txt_waiterid_value.setText(jsonObj.getString("WaiterID"));
                                    txt_waitername_value.setText(jsonObj.getString("WaiterName"));
                                    txt_resturant_value.setText(jsonObj.getString("RestaurantName"));
                                    txt_orderid_currentOrderStatus.setText(openBillNoHistoryDetails);
                                    btn_totoalAmount.setText(jsonObj.getString("TotalAmount"));

                                    JSONArray jsonArray = jsonObj.getJSONArray("MenuItemsList");
                                    orderStatusDataArrayList = new ArrayList<OrderHistoryDetailsData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        String MenuItemImage = jsonObject1.getString("MenuItemImage");

                                        String MenuItemName = jsonObject1.getString("MenuItemName");
                                        String Price = jsonObject1.getString("MenuItemPrice");


                                        orderStatusData = new OrderHistoryDetailsData(MenuItemImage, MenuItemName, Price);
                                        orderStatusDataArrayList.add(orderStatusData);

                                    }


                                    orderlistdapter = new OrderHistoryDetailsAdapter(getActivity(), orderStatusDataArrayList);

                                    if (orderlistdapter != null) {
                                        listview_orderdetails.setAdapter(orderlistdapter);
                                        orderlistdapter.notifyDataSetChanged();

                                        progressBar.dismiss();

                                    }

                                    if (listview_orderdetails.getCount() == 0) {
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


}
