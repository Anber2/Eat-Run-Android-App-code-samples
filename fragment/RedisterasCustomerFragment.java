package com.mawaqaa.eatandrun.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 11/27/2017.
 */
public class RedisterasCustomerFragment extends EatndRunBaseFragment implements View.OnClickListener {

    String TAG = "LgnFrag";
    ImageView btn_login;
    EditText editTextname, editTexpswd, editTexfnam, editTexlnam, editTexemail, editTexcountry, editTexmobile, editTexcommison;
    Button registerBtn;

    String name, pswd, fnam, lnam, email, country, mobile, commison;
    Spinner spinner_country;
    CheckBox checkBox_add_cust;

    String message, Success;


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
        View v = inflater.inflate(R.layout.regascustomerfragment, container, false);

        initView(v);


        return v;
    }

    private void initView(View v) {


        registerBtn = (Button) v.findViewById(R.id.btnREgister);
        editTextname = (EditText) v.findViewById(R.id.editTextname);
        editTexpswd = (EditText) v.findViewById(R.id.editTexpswd);
        editTexfnam = (EditText) v.findViewById(R.id.editTexfnam);
        editTexlnam = (EditText) v.findViewById(R.id.editTexlnam);
        editTexemail = (EditText) v.findViewById(R.id.editTexemail);
         editTexmobile = (EditText) v.findViewById(R.id.editTexmobile);
        editTexcommison = (EditText) v.findViewById(R.id.editTexcommison);
        // spinner_country = (Spinner) v.findViewById(R.id.spinner_country_add_costumer);
        checkBox_add_cust = (CheckBox) v.findViewById(R.id.checkBox_add_cust);


        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name, pswd, fnam, lnam, email, country, mobile, commison;


        if (registerAuthentication()) {

          /* Toast.makeText(getActivity(), "You have registered successful!!", Toast.LENGTH_LONG).show();


            Intent i = new Intent(getActivity(), CybersourceCustomer.class);
            startActivity(i);
*/
            try {
                JSONObject registrationJsonObject = new JSONObject();

                registrationJsonObject.putOpt(AppConstants.EatndRun_USERNAME, editTextname.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_PASSWORD, editTexpswd.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_F_NAME, editTexfnam.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_L_NAME, editTexlnam.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_EMAIL, editTexemail.getText().toString());
                //registrationJsonObject.putOpt(AppConstants.EatndRun_COUNTRY, editTexcountry.getText().toString());
                registrationJsonObject.putOpt(AppConstants.COUNTRY_ID, 1);
                registrationJsonObject.putOpt(AppConstants.EatndRun_MOBILE, editTexmobile.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_COMMISON, "45.6");
                registrationJsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                //startSpinwheel(false, true);
                makeJsonRegistrationStringReq(AppConstants.EatndRun_ADDCOUSTUMER,
                        registrationJsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private String makeJsonRegistrationStringReq(String urlPost, final JSONObject jsonObject) {

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
                                Log.e(TAG, "==================" + response);

                                if (jsonObj != null) {
                                    message = jsonObj.getString("Message");

                                    Success = jsonObj.getString("Success");

                                }
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                                if (Success.equals("Success")) {
                                   /* Intent i = new Intent(getActivity(), CybersourceCustomer.class);
                                    startActivity(i);*/
                                   // Activity.pushFragments(new CybersourceCustomer(), false, true);
                                    Activity.pushFragments(new CybersourceCustomer(), false, true);

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


        if (editTextname.getText().toString().equals("")) {
            Toast.makeText(Activity, "name empty", Toast.LENGTH_LONG).show();
            editTextname.requestFocus();
            return false;
        }
        if (editTexfnam.getText().toString().equals("")) {
            Toast.makeText(Activity, "first name empty", Toast.LENGTH_LONG).show();
            editTexfnam.requestFocus();
            return false;
        }
        if (editTexlnam.getText().toString().equals("")) {
            Toast.makeText(Activity, "last name empty", Toast.LENGTH_LONG).show();
            editTexlnam.requestFocus();
            return false;
        }
        if (editTexemail.getText().toString().length() <= 0) {
            Toast.makeText(Activity, "email name empty", Toast.LENGTH_LONG).show();
            editTexemail.requestFocus();
            return false;
        } else {
            if (!editTexemail.getText().toString().matches(AppConstants.EMAIL_PATTERN)) {
                Toast.makeText(Activity, "invalid email", Toast.LENGTH_LONG).show();
                editTexemail.requestFocus();
                return false;
            }
        }

        if (editTexpswd.getText().toString().equals("")) {
            Toast.makeText(Activity, "password field empty", Toast.LENGTH_LONG).show();
            editTexpswd.requestFocus();
            return false;
        }
      /*  if (editTexcountry.getText().toString().equals("")) {
            Toast.makeText(Activity, "country_empty", Toast.LENGTH_LONG).show();
            editTexcountry.requestFocus();
            return false;
        }*/
        if (editTexmobile.getText().toString().equals("")) {
            Toast.makeText(Activity, "mobile number empty", Toast.LENGTH_LONG).show();
            editTexmobile.requestFocus();
            return false;
        }

        if (editTexcommison.getText().toString().equals("")) {
            Toast.makeText(Activity, "commison empty", Toast.LENGTH_LONG).show();
            editTexcommison.requestFocus();
            return false;
        }

        return true;

    }
}
