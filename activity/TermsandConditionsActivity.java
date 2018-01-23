package com.mawaqaa.eatandrun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.Constants.AppConstants.TermsandCondition_UserType;

/**
 * Created by HP on 7/31/2017.
 */

public class TermsandConditionsActivity extends Activity {

    public static final String TAG = "Termsandconditions";

    TextView text_conditions_ac;
    Button agree_tearms_ac;

    CheckBox checkbox_terms_ac;

    String terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_termsandconditions_pupup);

        text_conditions_ac = (TextView) findViewById(R.id.text_conditions_ac);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                   // jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(TermsandConditionsActivity.this));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(TermsandConditionsActivity.this));
                    jsonObject.putOpt("UserType", TermsandCondition_UserType);

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


                    makeJsonTermsandConditionsStringReq(AppConstants.EatndRun_TERMSANDCONDITIONS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();


    }

    private String makeJsonTermsandConditionsStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {


                                    terms = jsonObj.getString("ContentText");
                                    Log.e(TAG, "=========" + terms);

                                    if(!terms.equalsIgnoreCase("null")){
                                        text_conditions_ac.setText(Html.fromHtml(terms));
                                    }else
                                    {text_conditions_ac.setText("TERMS & CONDITIONS ARE NOT AVAILABLE!");}


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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                       Toast.makeText(TermsandConditionsActivity.this, R.string.string_semething_went_wrong, Toast.LENGTH_LONG).show();

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

}