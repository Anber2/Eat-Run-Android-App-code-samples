package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
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

/**
 * Created by HP on 11/27/2017.
 */

public class FragmentSplitBill extends EatndRunBaseFragment implements View.OnClickListener {
    public static final String TAG = "Fragment_SplitBill";
    Button btn_splitbill_evently, btn_chooseitem;
    String splitType;
    private ProgressDialog progressBar;
    TextView txt_splitbill_openBillNo;

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
        //View v = inflater.inflate(R.layout.fragment_registerorlogin, container, false);
        View v = inflater.inflate(R.layout.layout_splitbill, container, false);
        initView(v);

        return v;
    }

    private void initView(View v) {
        btn_splitbill_evently = (Button) v.findViewById(R.id.btn_splitbill_evently);
        btn_chooseitem = (Button) v.findViewById(R.id.btn_chooseitem);

        txt_splitbill_openBillNo = (TextView) v.findViewById(R.id.txt_splitbill_openBillNo);

        txt_splitbill_openBillNo.setText(PreferenceUtil.getOpenBillNumber(getActivity()));

        btn_splitbill_evently.setOnClickListener(this);
        btn_chooseitem.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_splitbill_evently:


                Activity.pushFragments(new FragmenSplitBillEvently(), false, true);
                break;
            case R.id.btn_chooseitem:

                chooseItemAuthentication();

                break;
        }
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
