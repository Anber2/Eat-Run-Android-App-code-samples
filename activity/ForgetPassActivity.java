package com.mawaqaa.eatandrun.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 7/18/2017.
 */

public class ForgetPassActivity extends EatndRunBaseActivity {

    public static final String TAG = "ForgetPassFragment";

    EditText edit_email_forgetpass;
    Button button_sendemail_forgetpass;
    String message, Success;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_forget_pass);

        edit_email_forgetpass = (EditText) findViewById(R.id.edit_email_forgetpass);

        button_sendemail_forgetpass = (Button) findViewById(R.id.button_sendemail_forgetpass);

        button_sendemail_forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (registerAuthentication()) {
                    progressBar = ProgressDialog.show(ForgetPassActivity.this, "", getString(R.string.progressbar_please_wait), true, false);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONObject jsonObject = new JSONObject();


                                jsonObject.putOpt(AppConstants.EatndRun_EMAIL, edit_email_forgetpass.getText().toString());

                                jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                                makeJsonForgetPasswordStringReq(AppConstants.EatndRun_FORGOTPASSWORD, jsonObject);

                            } catch (Exception xx) {
                                xx.toString();
                            }

                        }
                    }).start();




                }
            }
        });

    }

    private String makeJsonForgetPasswordStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(ForgetPassActivity.this);

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

                                Toast.makeText(ForgetPassActivity.this, message, Toast.LENGTH_LONG).show();

                                if (Success.equals("Success"))

                                {
                                    Intent i = new Intent(ForgetPassActivity.this, LoginActivity.class);
                                    startActivity(i);

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPassActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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


    public boolean registerAuthentication() {


        if (edit_email_forgetpass.getText().toString().equals("")) {
            Toast.makeText(ForgetPassActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_email_empty), Toast.LENGTH_LONG).show();
            edit_email_forgetpass.requestFocus();
            return false;
        }
        return true;
    }
}
