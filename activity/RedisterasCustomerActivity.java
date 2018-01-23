package com.mawaqaa.eatandrun.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.mawaqaa.eatandrun.adapter.CountryNameAdapter;
import com.mawaqaa.eatandrun.data.Country;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.Constants.AppConstants.TermsandCondition_UserType;

/**
 * Created by HP on 7/17/2017.
 */

public class RedisterasCustomerActivity extends EatndRunBaseActivity implements View.OnClickListener {

    String TAG = "RedisterasCustomer";
    ImageView btn_login;
    EditText editTextname, editTexpswd, editTexfnam, editTexlnam, editTexemail, editTexcountry, editTexmobile, editTexcommison, editTexConfirmPass_cus;
    Button registerBtn;
    Country country;
    Spinner spinnerTexcountry;
    ArrayList<Country> arrayListCountry;
    CountryNameAdapter countrynameAdapter;
    String spinitemCountryId;
    String message, Success, Cus_Id;
    String CountryID;
    String CreateProfileUri;
    CheckBox checkBox_add_cust;
    TextView viewtearm_txt;
    private ProgressDialog progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regascustomerfragment);

        Log.d(TAG, "onCreate");

        registerBtn = (Button) findViewById(R.id.btnREgister);
        editTextname = (EditText) findViewById(R.id.editTextname);
        editTexpswd = (EditText) findViewById(R.id.editTexpswd);
        editTexfnam = (EditText) findViewById(R.id.editTexfnam);
        editTexlnam = (EditText) findViewById(R.id.editTexlnam);
        editTexemail = (EditText) findViewById(R.id.editTexemail);
        editTexmobile = (EditText) findViewById(R.id.editTexmobile);
        editTexcommison = (EditText) findViewById(R.id.editTexcommison);
        editTexConfirmPass_cus = (EditText) findViewById(R.id.editTexConfirmPass_cus);

        editTexcommison.setText(AppConstants.EatndRun_UserCommissionFee);
        Log.d(TAG, "UserCommissionFee" + AppConstants.EatndRun_UserCommissionFee);

        viewtearm_txt = (TextView) findViewById(R.id.viewtearm_txt);
        checkBox_add_cust = (CheckBox) findViewById(R.id.checkBox_add_cust);

        // spinner_country = (Spinner) v.findViewById(R.id.spinner_country_add_costumer);
        getCountrylist();

        registerBtn.setOnClickListener(this);

        spinnerTexcountry = (Spinner) findViewById(R.id.spinnerTexcountry);
        spinnerTexcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinnerTexcountry.getSelectedItemPosition();
                country = arrayListCountry.get(pos);
                spinitemCountryId = (country.getCountryId());
                Log.e("hghg", "" + country.getCountryId());
                if (!spinitemCountryId.equals("")) {
                    CountryID = spinitemCountryId;
                    PreferenceUtil.setCountryId(RedisterasCustomerActivity.this, CountryID);

                }/* else {
                    Toast.makeText(RedisterasCustomerActivity.this, "Please Select Country", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewtearm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsandCondition_UserType = 1;
                startActivity(new Intent(RedisterasCustomerActivity.this, TermsandConditionsActivity.class));

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (registerAuthentication()) {

          /* Toast.makeText(getActivity(), "You have registered successful!!", Toast.LENGTH_LONG).show();


            Intent i = new Intent(getActivity(), CybersourceCustomer.class);
            startActivity(i);
*/
            progressBar = ProgressDialog.show(RedisterasCustomerActivity.this, "", "Please Wait ...", true, false);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {


                        JSONObject registrationJsonObject = new JSONObject();

                        registrationJsonObject.putOpt(AppConstants.EatndRun_USERNAME, editTextname.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_PASSWORD, editTexpswd.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_F_NAME, editTexfnam.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_L_NAME, editTexlnam.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_EMAIL, editTexemail.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.COUNTRY_ID, PreferenceUtil.getCountryId(RedisterasCustomerActivity.this));
                        registrationJsonObject.putOpt(AppConstants.EatndRun_MOBILE, editTexmobile.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_COMMISON, 45.0);
                        registrationJsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                        makeJsonRegistrationStringReq(AppConstants.EatndRun_ADDCOUSTUMER,
                                registrationJsonObject);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }
    }


    private String makeJsonRegistrationStringReq(String urlPost, final JSONObject jsonObject) {

        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(RedisterasCustomerActivity.this);

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

                                    Cus_Id = jsonObj.getString("Cus_Id");
                                    PreferenceUtil.setUserDetails(RedisterasCustomerActivity.this, jsonObj);
                                }

                                Toast.makeText(RedisterasCustomerActivity.this, message, Toast.LENGTH_LONG).show();

                                if (Success.equals("Success")) {

                                    new AlertDialog.Builder(RedisterasCustomerActivity.this)
                                            .setMessage(message + "\n" + "Do you want to add a credit card now?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    CreateCustomerProfileAndCardDetails();
                                                    RedisterasCustomerActivity.this.finish();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(RedisterasCustomerActivity.this, LoginActivity.class));
                                                    RedisterasCustomerActivity.this.finish();
                                                }
                                            })
                                            .show();


                                } /*else {

                                    finish();
                                    startActivity(new Intent(RedisterasCustomerActivity.this, RedisterasCustomerActivity.class));
                                }*/

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
                            Toast.makeText(RedisterasCustomerActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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
        Log.e("country getCountryId()", "" + country.getCountryId());


        if (editTextname.getText().toString().equals("")) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_name_empty), Toast.LENGTH_LONG).show();
            editTextname.requestFocus();
            return false;
        }
        if (editTexfnam.getText().toString().equals("")) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_first_name_empty), Toast.LENGTH_LONG).show();
            editTexfnam.requestFocus();
            return false;
        }
        if (editTexlnam.getText().toString().equals("")) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_last_name_empty), Toast.LENGTH_LONG).show();
            editTexlnam.requestFocus();
            return false;
        }
        if (editTexemail.getText().toString().length() <= 0) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_email_empty), Toast.LENGTH_LONG).show();
            editTexemail.requestFocus();
            return false;
        } else {
            if (!editTexemail.getText().toString().matches(AppConstants.EMAIL_PATTERN)) {
                Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.register_cus_error_invalid_email), Toast.LENGTH_LONG).show();
                editTexemail.requestFocus();
                return false;
            }
        }

        if (editTexpswd.getText().toString().length() <6) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_pass_empty), Toast.LENGTH_LONG).show();
            editTexpswd.requestFocus();
            return false;
        }
        if (!editTexpswd.getText().toString().equals(editTexConfirmPass_cus.getText().toString())) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_confirm_pass), Toast.LENGTH_LONG).show();
            editTexConfirmPass_cus.requestFocus();
            return false;
        }

        if ( editTexmobile.getText().toString().length() < 6) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_mobileno_empty), Toast.LENGTH_LONG).show();
            editTexmobile.requestFocus();
            return false;
        }

        if (editTexcommison.getText().toString().equals("")) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_select_country), Toast.LENGTH_LONG).show();
            editTexcommison.requestFocus();
            return false;
        }
        if (country.getCountryId().equals("0")) {
            Log.e("hghg", "" + country.getCountryId());
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_select_country), Toast.LENGTH_SHORT).show();
            spinnerTexcountry.requestFocus();
            return false;
        }
        if (!checkBox_add_cust.isChecked()) {
            Toast.makeText(RedisterasCustomerActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_tearms), Toast.LENGTH_SHORT).show();
            checkBox_add_cust.requestFocus();
            return false;
        }


        return true;

    }

    private void getCountrylist() {

        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(RedisterasCustomerActivity.this));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


                    makeJsongetCountrylistStringReq(AppConstants.EatndRun_GETCOUNTRY,
                            jsonObject);


                } catch (Exception e) {
                    Log.e(TAG, "Execption while Putting Json");
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private String makeJsongetCountrylistStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(RedisterasCustomerActivity.this);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {

                                    JSONArray countryArr = jsonObj.getJSONArray("countryList");
                                    arrayListCountry = new ArrayList<Country>();

                                    arrayListCountry.add(new Country("0", getString(R.string.homefrag_spinner_select_country), ""));


                                    for (int t = 0; t < countryArr.length(); t++) {

                                        JSONObject jObjArrayObjects = countryArr.getJSONObject(t);

                                        String id = jObjArrayObjects.getString("CountryId");
                                        String name = jObjArrayObjects.getString("CountryName");
                                        String code = jObjArrayObjects.getString("CountryCode");

                                        country = new Country(id, name, code);

                                        arrayListCountry.add(country);

                                    }

                                    countrynameAdapter = new CountryNameAdapter(RedisterasCustomerActivity.this,
                                            arrayListCountry);
                                    if (countrynameAdapter != null) {
                                        spinnerTexcountry.setAdapter(countrynameAdapter);

                                    }


                                }
                            } catch (Exception xx) {
                                Log.e(TAG, "****" + xx.toString());
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
                            Toast.makeText(RedisterasCustomerActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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

    private void CreateCustomerProfileAndCardDetails() {
        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(RedisterasCustomerActivity.this));

                    makeJsonCreateCustomerProfileAndCardDetailsStringReq(
                            AppConstants.EatndRun_CREATECUSTOMERPROFILEANDCARDDETAILS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    private String makeJsonCreateCustomerProfileAndCardDetailsStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    CreateProfileUri = jsonObj.getString("CreateProfileUri");
                                    PreferenceUtil.setProfileUri(
                                            RedisterasCustomerActivity.this, CreateProfileUri);
                                }


                                 startActivity(new Intent(RedisterasCustomerActivity.this, WebviewPupupActivity
                                        .class));
                             /*   try {
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CreateProfileUri));
                                    startActivity(myIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(RedisterasCustomerActivity.this, "No application can handle this request."
                                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }*/
                                finish();


                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
                                xx.toString();
                            }
                            //  progressBar.dismiss();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    progressBar.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RedisterasCustomerActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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
