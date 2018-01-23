package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by HP on 11/27/2017.
 */

public class JoinBillOfJoinBillSplitEvently extends EatndRunBaseFragment {
    String TAG = "JoinBillSplitEvently";
    Button btn_joinbill_evenly_addopenbillno;
    EditText edt_joinbill_evenly_addopenbillno;
    private ProgressDialog progressBar;
    String splitType;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
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
        View v = inflater.inflate(R.layout.join_bill_split_evenly_add_openbill_no, container, false);


        btn_joinbill_evenly_addopenbillno = (Button) v.findViewById(R.id.btn_joinbill_evenly_addopenbillno);
        edt_joinbill_evenly_addopenbillno = (EditText) v.findViewById(R.id.edt_joinbill_evenly_addopenbillno);


        btn_joinbill_evenly_addopenbillno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activity.pushFragments(new SplitBillEventlyConfirmFragmen(), false, true);
                if (SplitEvenlyAddPillNoAuthentication()) {
                    getJoinToBillAPI();
                    PreferenceUtil.setOpenBillNumber(getActivity(),edt_joinbill_evenly_addopenbillno.getText().toString());
                }

            }
        });


        return v;
    }

    public void getJoinToBillAPI() {

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("Cust_ID", PreferenceUtil.getUserId(getActivity()));
                    //jsonObject.putOpt("OrderID", PreferenceUtil.getOrderID(getActivity()));
                    jsonObject.putOpt("Rest_ID", PreferenceUtil.getResID(getActivity()));
                    jsonObject.putOpt("OpenBillNumber", edt_joinbill_evenly_addopenbillno.getText().toString());

                    makeJsonJoinToBillStringReq(AppConstants.EatndRun_JOINBILL, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();
                    xx.toString();
                }

            }
        }).start();

    }

    private String makeJsonJoinToBillStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null ) {
                                  /*  PreferenceUtil.setOpenBillNumber(getActivity(),  .
                                  edt_joinbill_evenly_addopenbillno.getText().toString());
                                    Activity.pushFragments(new FragmenPayBillConfirm(), false, true);*/

                                   int splitTypee = jsonObj.getInt("SplitType");
                                    if (splitTypee == 1){
                                        progressBar.dismiss();

                                        Activity.pushFragments(new FragmenSplitBillEvently(), false, true);}else

                                    if (splitTypee == 2) {
                                        //Activity.pushFragments(new FragmentChooseItemToSplit(), false, true);
                                        progressBar.dismiss();

                                        chooseItemAuthentication();
                                    }
                                }



                            } catch (Exception xx) {
                                progressBar.dismiss();
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
                            progressBar.dismiss();
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


    public boolean SplitEvenlyAddPillNoAuthentication() {

        if (edt_joinbill_evenly_addopenbillno.getText().toString().equals("")) {
            Toast.makeText(Activity, getString(R.string.joinbillofjoinbillsplitevently_empty_msg), Toast.LENGTH_LONG).show();
            edt_joinbill_evenly_addopenbillno.requestFocus();
            return false;
        }
        if (edt_joinbill_evenly_addopenbillno.getText().length() < 5) {
            Toast.makeText(Activity, getString(R.string.joinbillofjoinbillsplitevently_empty_msg2), Toast.LENGTH_LONG).show();
            edt_joinbill_evenly_addopenbillno.requestFocus();
            return false;
        }


        return true;
    }


    public void chooseItemAuthentication() {

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("Cust_ID", PreferenceUtil.getUserId(getActivity()));
                    //jsonObject.putOpt("OrderID", PreferenceUtil.getOrderID(getActivity()));
                    jsonObject.putOpt("OpenBillNumber", PreferenceUtil.getOpenBillNumber(getActivity()));


                    makeJson_chooseItemAuthentication_StringReq(AppConstants.EatndRun_GetNextCustomerID, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();


    }

    private String makeJson_chooseItemAuthentication_StringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {


                                if (response.equals("true"))

                                {
                                    Activity.pushFragments(new FragmentChooseItemToSplit(), false, true);

                                } else {

                                    Toast.makeText(getActivity(),
                                            "You are not authorized to use this option yet." + "\n" + " Please try again later!", Toast.LENGTH_LONG).show();
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
