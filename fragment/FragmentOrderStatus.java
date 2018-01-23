package com.mawaqaa.eatandrun.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
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
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
/**
 * Created by HP on 11/27/2017.
 */
public class FragmentOrderStatus extends EatndRunBaseFragment {


    public static ListView listvorderstatus;
    public static OrderStatusAdapter orderStatusAdapter;
    public static ArrayList<OrderStatusData> orderStatusDataArrayList;
    public static OrderStatusData orderStatusData;
    public static TextView txt_OrderStatus_billNo, txt_OrderStatus_restName, txt_OrderStatus_waiter, txt_OrderStatus_CusNo, txt_OrderStatus_TableNo, txt_OrderStatus_totalBillAmount, txt_OrderStatus_your_BillAmount, txt_OrderStatus_tip;
    public static List<String> idsorderPrice;
    public static ProgressDialog progressBar;
    public static Context context;
    public static Button button_Accept_orderBill;
    String TAG = "FragmentOrderStatus";
    int[] arryNum;
    View v;
    Button btn_SplitbillOrderpage, confirm_bill, changeBill;
    String message, Success;
    String Decision;
    List<String> quantity;
    SwipeRefreshLayout swipeLayout;



    public static void getOrderItemsList() {

        // progressBar = ProgressDialog.show(context, "", "Please Wait ...", true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(context));
                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(context));

                    makeJsonGetOrderItemsStringReq(AppConstants.EatndRun_GETORDERITEMS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                    //progressBar.dismiss();

                }

            }
        }).start();
    }

    public static String makeJsonGetOrderItemsStringReq(String urlPost, final JSONObject jsonObject) {
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
                                idsorderPrice = new ArrayList<String>();
                                if (jsonObj != null) {

                                    String RestaurantName = jsonObj.getString("RestaurantName");
                                    String WaiterName = jsonObj.getString("WaiterName");
                                    String NumberOfCustomer = jsonObj.getString("NumberOfCustomer");
                                    String TableNo = jsonObj.getString("TableNo");
                                    String OpenOrderNumber = jsonObj.getString("OpenOrderNumber");
                                    String Total = jsonObj.getString("Total");

                                    String OrderID = jsonObj.getString("OrderId");
                                    String SubTotal = jsonObj.getString("SubTotal");

                                    PreferenceUtil.setAmount(context, Total);
                                    PreferenceUtil.setOrderID(context, OrderID);

                                    txt_OrderStatus_billNo.setText(OpenOrderNumber);
                                    txt_OrderStatus_restName.setText(RestaurantName);
                                    txt_OrderStatus_waiter.setText(WaiterName);
                                    txt_OrderStatus_CusNo.setText(NumberOfCustomer);
                                    txt_OrderStatus_TableNo.setText(TableNo);
                                    txt_OrderStatus_totalBillAmount.setText("KD " + Total);
                                    txt_OrderStatus_your_BillAmount.setText("KD " + SubTotal);
                                    JSONArray jsonArray = jsonObj.getJSONArray("menuItemList");
                                    orderStatusDataArrayList = new ArrayList<OrderStatusData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        String MenuItemId = jsonObject1.getString("MenuItemId");
                                        String MenuItemName = jsonObject1.getString("MenuItemName");
                                        String MenuItemImage = jsonObject1.getString("MenuItemImage");

                                        String Price = jsonObject1.getString("Price");
                                        String Qty = jsonObject1.getString("Qty");
                                        String isActive = jsonObject1.getString("IsActive");
                                        PreferenceUtil.setIsActive(context, isActive);

                                        String sTotalPriceWithQty = Price + "," + Qty;

                                        //String menuItemId, String menuItemName, String menuItemImage, String price, String qny
                                        orderStatusData = new OrderStatusData(MenuItemId, MenuItemName,MenuItemImage ,Price, Qty);

                                        orderStatusDataArrayList.add(orderStatusData);

                                        idsorderPrice.add(sTotalPriceWithQty);


                                    }

                                    orderStatusAdapter = new OrderStatusAdapter(context, orderStatusDataArrayList);

                                    listvorderstatus.setAdapter(orderStatusAdapter);
                                    progressBar.dismiss();


                                } else {
                                    listvorderstatus.setVisibility(View.GONE);
                                    //progressBar.dismiss();

                                }

                            } catch (Exception xx) {
                                xx.toString();
                                //progressBar.dismiss();

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    //progressBar.dismiss();


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_orderstatus, container, false);

        initview(v);

        getOrderItemsList();

        return v;
    }

    private void initview(View v) {

        View includedLayout = v.findViewById(R.id.order_items_orders);
        listvorderstatus = (ListView) includedLayout.findViewById(R.id.listvorderstatus);
        txt_OrderStatus_totalBillAmount = (TextView) includedLayout.findViewById(R.id.txt_OrderStatus_totalBillAmount);
        txt_OrderStatus_tip  = (TextView) includedLayout.findViewById(R.id.txt_OrderStatus_tip);

        txt_OrderStatus_tip.setText(PreferenceUtil.getTip(Activity));

        txt_OrderStatus_your_BillAmount = (TextView) includedLayout.findViewById(R.id.txt_OrderStatus_your_BillAmount);
        context = getActivity();

        txt_OrderStatus_billNo = (TextView) v.findViewById(R.id.txt_OrderStatus_billNo);
        txt_OrderStatus_restName = (TextView) v.findViewById(R.id.txt_OrderStatus_restName);
        txt_OrderStatus_waiter = (TextView) v.findViewById(R.id.txt_OrderStatus_waiter);
        txt_OrderStatus_CusNo = (TextView) v.findViewById(R.id.txt_OrderStatus_CusNo);
        txt_OrderStatus_TableNo = (TextView) v.findViewById(R.id.txt_OrderStatus_TableNo);

        txt_OrderStatus_billNo.setText(PreferenceUtil.getOpenBillNumber(getActivity()));

        confirm_bill = (Button) v.findViewById(R.id.btn_Pay_Bill);
        btn_SplitbillOrderpage = (Button) v.findViewById(R.id.btn_SplitbillOrderpage);
        button_Accept_orderBill = (Button) v.findViewById(R.id.button_Accept_orderBill);
        changeBill = (Button) v.findViewById(R.id.btn_changeTip);


        button_Accept_orderBill.setVisibility(View.VISIBLE);

        button_Accept_orderBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_Accept_orderBill.setVisibility(View.GONE);

                progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.putOpt("Cust_ID", PreferenceUtil.getUserId(Activity));
                           /* jsonObject.putOpt("OrderID", PreferenceUtil.getOrderID(getActivity()));
                            jsonObject.putOpt("Rest_ID", PreferenceUtil.getResID(getActivity()));*/
                            jsonObject.putOpt("OpenBillNumber", PreferenceUtil.getOpenBillNumber(getActivity()));

                            makeJsonPreauthorizationStringReq(AppConstants.EatndRun_PreauthorizationUser, jsonObject);

                        } catch (Exception xx) {
                            progressBar.dismiss();
                            Toast.makeText(getActivity(), xx.toString(), Toast.LENGTH_LONG).show();

                        }

                    }
                }).start();

            }
        });


        listvorderstatus = (ListView) v.findViewById(R.id.listvorderstatus);


        btn_SplitbillOrderpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment FragmentSplitBillFrag = new FragmentSplitBill();
                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(FragmentSplitBillFrag, false, true);
            }
        });


        confirm_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity.pushFragments(new FragmenPayBillConfirm(), false, true);


            }
        });

        changeBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getString(R.string.restaurantlistdetailfragment_enter_tip_dialog) + ":");
                final EditText input = new EditText(getActivity());
                input.setHint(getString(R.string.restaurantlistdetailfragment_example_txt_dialog) + PreferenceUtil.getTip(getActivity()));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setPositiveButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        PreferenceUtil.setTip(getActivity(), input.getText().toString());
                        saveCusTip();

                    }
                });
                alert.setNegativeButton(getString(R.string.string_cancel), null);
                alert.show();
            }
        });

    }

    private void CreateBillPayment() {

        // progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    jsonObject.putOpt(AppConstants.EatndRun_ORDERID, PreferenceUtil.getOrderID(Activity));
                    jsonObject.putOpt(AppConstants.EatndRun_OPENBILLNUMBER, PreferenceUtil.getOpenBillNumber(Activity));
                    jsonObject.putOpt("TransactionId", "");
                    jsonObject.putOpt("Amount", PreferenceUtil.getAmount(getActivity()));
                    jsonObject.putOpt("Status", "false");


                    makeJsonCreateBillPaymentStringReq(AppConstants.EatndRun_CREATEBILLPAYMENT, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    private String makeJsonCreateBillPaymentStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    Success = jsonObj.getString("Success");
                                }

                                //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();


                                if (Success.equals("Success"))


                                    btn_SplitbillOrderpage.setVisibility(View.VISIBLE);

                                confirm_bill.setVisibility(View.VISIBLE);

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
                                if (jsonObj != null) {
                                    Decision = jsonObj.getString("Result");
                                }

                                Toast.makeText(getActivity(), Decision, Toast.LENGTH_LONG).show();


                                if (Decision.equals("ACCEPT")) {
                                     Activity.pushFragments(new FragmenPayBillConfirm(), false, true);

                                } else {
                                    button_Accept_orderBill.setVisibility(View.VISIBLE);
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

    private void saveCusTip() {

        //progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt("Cus_Tip", PreferenceUtil.getTip(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJson_saveCusTip_StringReq(AppConstants.EatndRun_SetCustomerTip, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

    }

    private String makeJson_saveCusTip_StringReq(String urlPost, final JSONObject jsonObject) {
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


                                if (response != null) {


                                    if (response.equalsIgnoreCase("\"1\"")) {


                                        Toast.makeText(getActivity(), "Tip Saved!", Toast.LENGTH_LONG).show();

                                    }


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
                    //progressBar.dismiss();
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
}

