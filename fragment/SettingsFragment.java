package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class SettingsFragment extends EatndRunBaseFragment {
    public static final String TAG = "SettingsFragment";
    EditText edit_settings_uname, edit_settings_phno, edit_tip;
    TextView text_settings_email;
    String uname, mobile, tip, Cus_FirstName, Cus_LastName, Cus_Email, pass, Cus_Tip;

    RelativeLayout rlaout_changepassword;
    Button button_editinfo, button_save, button_logout;

    String message, Success;
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

        View v = inflater.inflate(R.layout.layout_settings, container, false);
        initView(v);
        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonCustomerAccountStringReq(AppConstants.EatndRun_VIEWCUSTOMER, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
        return v;
    }



    private void initView(View v) {
        edit_settings_uname = (EditText) v.findViewById(R.id.edit_settings_uname);
        edit_settings_phno = (EditText) v.findViewById(R.id.edit_settings_phno);
        edit_tip = (EditText) v.findViewById(R.id.edit_tip);
        text_settings_email = (TextView) v.findViewById(R.id.text_settings_email);

        rlaout_changepassword = (RelativeLayout) v.findViewById(R.id.rlaout_changepassword);


        button_editinfo = (Button) v.findViewById(R.id.button_editinfo);
        button_save = (Button) v.findViewById(R.id.button_save);

        rlaout_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.pushFragments(new ChangePasswordFragment(), false, true);
            }
        });


        button_editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_settings_uname.setEnabled(true);
                edit_settings_uname.requestFocus();

                edit_settings_phno.setEnabled(true);
                edit_tip.setEnabled(true);


            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));

                            jsonObject.putOpt(AppConstants.CUST_FirstName, Cus_FirstName);
                            jsonObject.putOpt(AppConstants.CUST_LastName, Cus_LastName);
                            jsonObject.putOpt(AppConstants.CUST_Email, Cus_Email);
                            jsonObject.putOpt(AppConstants.CUS_MOBILE, edit_settings_phno.getText().toString());
                            jsonObject.putOpt(AppConstants.CUS_TIP, edit_tip.getText().toString());


                            jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                            makeJsonUpdateUserInfoStringReq(AppConstants.EatndRun_EDITCUSTOMER, jsonObject);

                        } catch (Exception xx) {
                            xx.toString();
                        }

                    }
                }).start();
            }
        });

    }

    private String makeJsonUpdateUserInfoStringReq(String urlPost, final JSONObject jsonObject) {
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

                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                                if (Success.equals("Success"))
                                    edit_settings_uname.setEnabled(false);
                                edit_settings_phno.setEnabled(false);
                                edit_tip.setEnabled(false);


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
    private String makeJsonCustomerAccountStringReq(String urlPost, final JSONObject jsonObject) {
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

//   , Cus_Mobile, Cus_Tip;
                                    uname = jsonObj.getString("Cus_Username");
                                    mobile = jsonObj.getString("Cus_Mobile");
                                    tip = jsonObj.getString("Cus_Tip");

                                    Cus_FirstName = jsonObj.getString("Cus_FirstName");
                                    Cus_LastName = jsonObj.getString("Cus_LastName");
                                    Cus_Email = jsonObj.getString("Cus_Email");

                                    edit_settings_uname.setText(uname);
                                    edit_settings_phno.setText(mobile);
                                    edit_tip.setText(tip);
                                    text_settings_email.setText(Cus_Email);
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
