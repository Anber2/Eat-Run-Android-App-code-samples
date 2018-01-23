package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;
import com.mawaqaa.eatandrun.activity.LoginActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 11/27/2017.
 */

public class ForgetPassFragment extends EatndRunBaseFragment {
    public static final String TAG = "ForgetPassFragment";

    EditText edit_email_forgetpass;
    Button button_sendemail_forgetpass;
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

        View v = inflater.inflate(R.layout.fragment_forget_pass, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        edit_email_forgetpass = (EditText) v.findViewById(R.id.edit_email_forgetpass);

        button_sendemail_forgetpass = (Button) v.findViewById(R.id.button_sendemail_forgetpass);

        button_sendemail_forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (registerAuthentication()) {


                    try {

                        JSONObject jsonObject = new JSONObject();


                        jsonObject.putOpt(AppConstants.EatndRun_EMAIL, edit_email_forgetpass.getText().toString());

                        jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                        makeJsonForgetPasswordStringReq(AppConstants.EatndRun_FORGOTPASSWORD, jsonObject);

                    } catch (Exception xx) {
                        xx.toString();
                    }


                }
            }
        });


    }

    private String makeJsonForgetPasswordStringReq(String urlPost, final JSONObject jsonObject) {
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
                                     Activity.pushFragments(new LoginFragment(), false, true);

                                {
                                    Intent i = new Intent(getActivity(), LoginActivity.class);                                startActivity(i);

                                }


                            } catch (Exception xx) {
                                Log.e(TAG, "****" + xx.toString());
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


    public boolean registerAuthentication() {


        if (edit_email_forgetpass.getText().toString().equals("")) {
            Toast.makeText(Activity, "Email Field Is Empty!!", Toast.LENGTH_LONG).show();
            edit_email_forgetpass.requestFocus();
            return false;
        }
        return true;
    }
}
