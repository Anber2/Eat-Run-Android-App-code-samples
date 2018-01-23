package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.Constants.AppConstants.EatndRun_OpenBillNumber;

/**
 * Created by HP on 11/27/2017.
 */
public class FragmentOpenBill extends EatndRunBaseFragment {

    public static TextView txt_pressNest;
    public static Button btn_Register_bill;
    String TAG = "FragmentOpenBill";
    public static TextView txt_openBillNo, txt_restName, txt_waiterName, txt_waiterID, txt_Cus_No, txt_table_No;
    public static String message, Success;
    public static  boolean isResponse = false;

    public static ProgressDialog progressBar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();
    }


    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;
        //   ((AjaratyMainActivity) getActivity()).hideLogo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_registerorlogin, container, false);
        View v = inflater.inflate(R.layout.fragment_openbill, container, false);

        initview(v);


        getOpenBillNo();

       // getOrderItemsList2();


        return v;
    }

    private void getOpenBillNo() {

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(getActivity()));
                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    //jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


                    makeJsonOpenBillStringReq
                            (AppConstants.EatndRun_OPENBILL, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    public void initview(View v) {

        txt_openBillNo = (TextView) v.findViewById(R.id.txt_openBillNo);
        txt_restName = (TextView) v.findViewById(R.id.txt_restName);
        txt_waiterName = (TextView) v.findViewById(R.id.txt_waiterName);
        txt_waiterID = (TextView) v.findViewById(R.id.txt_waiterID);
        txt_Cus_No = (TextView) v.findViewById(R.id.txt_Cus_No);
        txt_table_No = (TextView) v.findViewById(R.id.txt_table_No);
        txt_pressNest = (TextView) v.findViewById(R.id.txt_pressNest);

        btn_Register_bill = (Button) v.findViewById(R.id.btn_Register_bill);

        btn_Register_bill.setVisibility(View.INVISIBLE);
        txt_pressNest.setVisibility(View.INVISIBLE);

        btn_Register_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getOrderItemsList();

               /* Fragment frordersts = new FragmentOrderStatus();
                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(frordersts, true, true);*/


            }
        });

    }

    private void getOrderItemsList() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("OpenOrderNumber", EatndRun_OpenBillNumber);
                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));


                    // jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(getActivity()));

                    makeJsonGetOrderItemsStringReq(AppConstants.EatndRun_GETORDERITEMS, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();

                    xx.toString();
                }

            }
        }).start();
    }

    private void getOrderItemsList2() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("OpenOrderNumber", EatndRun_OpenBillNumber);
                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));


                    // jsonObject.putOpt("RestaurantId", PreferenceUtil.getResID(getActivity()));

                    makeJsonGetOrderItemsStringReq2(AppConstants.EatndRun_GETORDERITEMS, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();

                    xx.toString();
                }

            }
        }).start();
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

                            if ((response.equalsIgnoreCase("\"-1\"")))

                            {
                                Toast.makeText(getActivity(),
                                        R.string.fragmentopenbill_txt_error, Toast.LENGTH_LONG).show();
                            } else {
                                Fragment frordersts = new FragmentOrderStatus();
                                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(frordersts, true, true);
                               /* btn_Register_bill.setVisibility(View.VISIBLE);
                              txt_pressNest.setVisibility(View.VISIBLE);*/

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

    private String makeJsonGetOrderItemsStringReq2(String urlPost, final JSONObject jsonObject) {
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

                            if ((response.equalsIgnoreCase("\"-1\"")))

                            {
                               /* Toast.makeText(getActivity(),
                                        R.string.fragmentopenbill_txt_error, Toast.LENGTH_LONG).show();*/
                            } else {

                                btn_Register_bill.setVisibility(View.VISIBLE);
                                txt_pressNest.setVisibility(View.VISIBLE);

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


    private String makeJsonOpenBillStringReq(String urlPost, final JSONObject jsonObject) {
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


                                    String OpenBillNumber = jsonObj.getString("OpenBillNumber");

                                    EatndRun_OpenBillNumber = OpenBillNumber;


                                    txt_openBillNo.setText(EatndRun_OpenBillNumber);


                                    PreferenceUtil.setOpenBillNumber(Activity, EatndRun_OpenBillNumber);

                                    getOrderItemsList2();



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

