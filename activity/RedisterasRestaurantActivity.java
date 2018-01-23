package com.mawaqaa.eatandrun.activity;

import android.app.ProgressDialog;
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
import com.mawaqaa.eatandrun.Utilities.GPSTracker;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.adapter.AreaNameAdapter;
import com.mawaqaa.eatandrun.adapter.CityListAdapter;
import com.mawaqaa.eatandrun.adapter.CountryNameAdapter;
import com.mawaqaa.eatandrun.adapter.CuisineListAdapter;
import com.mawaqaa.eatandrun.data.Area;
import com.mawaqaa.eatandrun.data.CityData;
import com.mawaqaa.eatandrun.data.Country;
import com.mawaqaa.eatandrun.data.CuisineListData;

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

public class RedisterasRestaurantActivity extends EatndRunBaseActivity implements View.OnClickListener {

    double lat, lng;
    String TAG = "LgnFrag";
    ImageView btn_login;
    EditText editTextNameRest, editTexpswdRest, editTexfnamRest, editTexlnamRest, editTexlpersonchrgeRest, editTexemailRest, editTexmobileRest, editTexlandlineRest, editTextAddressRest, editTexCuisine_name, editTexConfirmPass;
    Button btn_reg_Rest;
    TextView city_text, compant_comm_Txt, kent_comm_Txt, credit_comm_Txt;

    Country country;

    Spinner spinnerTexcountry, spinnerTexCuisine, spinnerTexArea, spinnerTexCity;
    ArrayList<Country> arrayListCountry;

    CountryNameAdapter countrynameAdapter;
    ArrayList<CuisineListData> cuisineListDataArrayList;
    CuisineListData cuisineListData;
    CuisineListAdapter cuisineListAdapter;
    Area area, area2;
    ArrayList<Area> arrayListArea;
    AreaNameAdapter areanameAdapter;
    String spinitemCountryId, spinitemCuisineId, spinitemAreaId, spinitemCityId;
    String message, Success;
    String CountryID, AreaID, CuisineID, CityID;
    CheckBox checkBox_add_rest;
    TextView viewtearm_txt_rest;
    GPSTracker gpsTracker;
    CityListAdapter cityListAdapter;
    CityData cityData;
    ArrayList<CityData> cityDataArrayList;
    private ProgressDialog progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regasrestaurantfragment);

        Log.d(TAG, "onCreate");

        btn_reg_Rest = (Button) findViewById(R.id.btn_reg_Rest);
        editTextNameRest = (EditText) findViewById(R.id.editTextNameRest);
        editTexpswdRest = (EditText) findViewById(R.id.editTexpswdRest);
        editTexfnamRest = (EditText) findViewById(R.id.editTexfnamRest);
        editTexlnamRest = (EditText) findViewById(R.id.editTexlnamRest);
        editTexlpersonchrgeRest = (EditText) findViewById(R.id.editTexlpersonchrgeRest);
        editTexemailRest = (EditText) findViewById(R.id.editTexemailRest);
        editTexmobileRest = (EditText) findViewById(R.id.editTexmobileRest);
        editTexlandlineRest = (EditText) findViewById(R.id.editTexlandlineRest);
        editTextAddressRest = (EditText) findViewById(R.id.editTextAddressRest);
        spinnerTexcountry = (Spinner) findViewById(R.id.spinnerTexcountry);
        spinnerTexCuisine = (Spinner) findViewById(R.id.spinnerTexCuisine);
        editTexCuisine_name = (EditText) findViewById(R.id.editTexCuisine_name);
        viewtearm_txt_rest = (TextView) findViewById(R.id.viewtearm_txt_rest);
        checkBox_add_rest = (CheckBox) findViewById(R.id.checkBox_add_rest);
        editTexConfirmPass = (EditText) findViewById(R.id.editTexConfirmPass);
        spinnerTexArea = (Spinner) findViewById(R.id.spinnerTexArea);
        spinnerTexCity = (Spinner) findViewById(R.id.spinnerTexCity);
        city_text = (TextView) findViewById(R.id.city_text);
        compant_comm_Txt = (TextView) findViewById(R.id.compant_comm_Txt);
        kent_comm_Txt = (TextView) findViewById(R.id.kent_comm_Txt);
        credit_comm_Txt = (TextView) findViewById(R.id.credit_comm_Txt);

        compant_comm_Txt.setText(AppConstants.EatndRun_Company_CommissionFee);
        kent_comm_Txt.setText(AppConstants.EatndRun_KNET_CardCommissionFee);
        credit_comm_Txt.setText(AppConstants.EatndRun_CardCommissionFee);
        Log.d(TAG, "--------" + AppConstants.EatndRun_Company_CommissionFee);
        Log.d(TAG, "++++++++++" + AppConstants.EatndRun_KNET_CardCommissionFee);
        Log.d(TAG, "********" + AppConstants.EatndRun_CardCommissionFee);


        getlocation();
        getCountrylist();
        getCuisinelist();

        btn_reg_Rest.setOnClickListener(this);

        spinnerTexcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinnerTexcountry.getSelectedItemPosition();
                country = arrayListCountry.get(pos);
                spinitemCountryId = (country.getCountryId());
                if (!spinitemCountryId.equals("0")) {
                    CountryID = spinitemCountryId;
                    PreferenceUtil.setCountryId(RedisterasRestaurantActivity.this, CountryID);

                    if (spinitemCountryId.equals("1")) {
                        getArealist();
                        spinnerTexCity.setVisibility(View.GONE);
                        CityID = "0";
                        PreferenceUtil.setCityId(RedisterasRestaurantActivity.this, CityID);

                        city_text.setVisibility(View.GONE);
                        spinnerTexArea.setSelection(0);

                    } else {
                        getCitylist();
                        spinnerTexCity.setVisibility(View.VISIBLE);
                        city_text.setVisibility(View.VISIBLE);
                        spinnerTexCity.setSelection(0);
                        spinnerTexArea.setAdapter(null);


                    }
                } else if (spinitemCountryId.equals("0")) {
                    spinnerTexArea.setAdapter(null);
                    spinnerTexCity.setAdapter(null);
                    spinnerTexCity.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTexCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinnerTexCity.getSelectedItemPosition();
                cityData = cityDataArrayList.get(pos);
                spinitemCityId = (cityData.getCityId());

                if (!spinitemCityId.equals("0")) {
                    CityID = spinitemCityId;
                    PreferenceUtil.setCityId(RedisterasRestaurantActivity.this, cityData.getCityId());
                    getArealist();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTexArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinnerTexArea.getSelectedItemPosition();
                area = arrayListArea.get(pos);
                spinitemAreaId = (area.getAreaId());
                Log.e("hghg", "spinitemAreaId" + area.getAreaId());
                PreferenceUtil.setAreaId(RedisterasRestaurantActivity.this, area.getAreaId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerTexCuisine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinnerTexCuisine.getSelectedItemPosition();
                cuisineListData = cuisineListDataArrayList.get(pos);
                spinitemCuisineId = (cuisineListData.getCuisineId());
                Log.e("getCuisineId :::", "" + cuisineListData.getCuisineId());
                if (!spinitemCuisineId.equals("")) {
                    CuisineID = spinitemCuisineId;
                    PreferenceUtil.setCuisineID(RedisterasRestaurantActivity.this, CuisineID);
                }
                if (spinitemCuisineId.equals("-1")) {
                    Toast.makeText(RedisterasRestaurantActivity.this, "Please Add Cuisine", Toast.LENGTH_SHORT).show();
                    editTexCuisine_name.setVisibility(View.VISIBLE);
                } else {
                    editTexCuisine_name.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewtearm_txt_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsandCondition_UserType = 2;
                startActivity(new Intent(RedisterasRestaurantActivity.this, TermsandConditionsActivity.class));

            }
        });
    }

    @Override
    public void onClick(View v) {


        if (registerAuthentication()) {

            progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {


                        JSONObject registrationJsonObject = new JSONObject();


                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_USERNAME, editTextNameRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_PASSWORD, editTexpswdRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_COMPANYNAME, editTexfnamRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_RESTAURANTNAME, editTexlnamRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_PERSONINCHARGE, editTexlpersonchrgeRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_EMAIL, editTexemailRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_COUNTRYID, PreferenceUtil.getCountryId(RedisterasRestaurantActivity.this));
                        registrationJsonObject.putOpt(AppConstants.AREAId, PreferenceUtil.getAreaId(RedisterasRestaurantActivity.this));
                        registrationJsonObject.putOpt(AppConstants.EatndRun_CITYID, PreferenceUtil.getCityId(RedisterasRestaurantActivity.this));


                        //CuisineOtherName
                        registrationJsonObject.putOpt("CuisineId", PreferenceUtil.getCuisineID(RedisterasRestaurantActivity.this));
                        registrationJsonObject.putOpt("CuisineOtherName", editTexCuisine_name.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_MOBILE, editTexmobileRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_TELEPHONE, editTexlandlineRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_ADDRESS, editTextAddressRest.getText().toString());
                        registrationJsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(RedisterasRestaurantActivity.this));
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_LATITUDE, lat);
                        registrationJsonObject.putOpt(AppConstants.EatndRun_RES_LONGITUDE, lng);

                        registrationJsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                        //startSpinwheel(false, true);
                        makeJsonRegistrationStringReq(AppConstants.EatndRun_ADDRESTAURANT,
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
            RequestQueue queue = Volley.newRequestQueue(RedisterasRestaurantActivity.this);

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

                                Toast.makeText(RedisterasRestaurantActivity.this, message, Toast.LENGTH_LONG).show();

                                if (Success.equals("Success"))
                                //Activity.pushFragments(new LoginFragment(), false, true);
                                {
                                    Intent i = new Intent(RedisterasRestaurantActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    RedisterasRestaurantActivity.this.finish();

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
                            Toast.makeText(RedisterasRestaurantActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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


        if (editTextNameRest.getText().toString().equals("")) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_username_empty), Toast.LENGTH_LONG).show();
            editTextNameRest.requestFocus();
            return false;
        }
        if (editTexfnamRest.getText().toString().equals("")) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_rest_name_empty), Toast.LENGTH_LONG).show();
            editTexfnamRest.requestFocus();
            return false;
        }
        if (editTexlpersonchrgeRest.getText().toString().equals("")) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_person_in_charge_empty), Toast.LENGTH_LONG).show();
            editTexlpersonchrgeRest.requestFocus();
            return false;
        }
        if (editTexlnamRest.getText().toString().equals("")) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_company_name_empty), Toast.LENGTH_LONG).show();
            editTexlnamRest.requestFocus();
            return false;
        }
        if (editTexemailRest.getText().toString().length() <= 0) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_email_empty), Toast.LENGTH_LONG).show();
            editTexemailRest.requestFocus();
            return false;
        } else {
            if (!editTexemailRest.getText().toString().matches(AppConstants.EMAIL_PATTERN)) {
                Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.register_cus_error_invalid_email), Toast.LENGTH_LONG).show();
                editTexemailRest.requestFocus();
                return false;
            }
        }

        if (editTexpswdRest.getText().toString().length() < 6) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_pass_empty), Toast.LENGTH_LONG).show();
            editTexpswdRest.requestFocus();
            return false;
        }
        if (!editTexConfirmPass.getText().toString().equals(editTexpswdRest.getText().toString())) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_confirm_pass), Toast.LENGTH_LONG).show();
            editTexConfirmPass.requestFocus();
            return false;
        }

        if (editTexmobileRest.getText().toString().length() < 6) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_mobileno_empty), Toast.LENGTH_LONG).show();
            editTexmobileRest.requestFocus();
            return false;
        }


        if (editTexlandlineRest.getText().toString().length() < 6) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_landline_empty), Toast.LENGTH_LONG).show();
            editTexlandlineRest.requestFocus();
            return false;
        }
        if (editTextAddressRest.getText().toString().equals("")) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_erroraddress_empty), Toast.LENGTH_LONG).show();
            editTextAddressRest.requestFocus();
            return false;
        }
        if (country.getCountryId().equals("0")) {
            Log.e("hghg", "" + country.getCountryId());
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_select_country), Toast.LENGTH_SHORT).show();
            spinnerTexcountry.requestFocus();
            return false;
        }
        if (!(spinnerTexCity.getVisibility() == View.GONE)) {
            if (cityData.getCityId().equals("0") || cityData.getCityId().equals(null)) {
                Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.homefrag_spinner_select_city_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (area.getAreaId().equals("0") || area.getAreaId().equals(null)) {
            Log.e("getAreaId", "" + area.getAreaId());
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.homefrag_spinner_select_area_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cuisineListData.getCuisineId().equals("0")) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_select_cuisine_empty), Toast.LENGTH_SHORT).show();
            spinnerTexCuisine.requestFocus();
            return false;
        }
        if (cuisineListData.getCuisineId().equals("-1") && editTexCuisine_name.getText().toString().equals("")) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_add_cuisine), Toast.LENGTH_SHORT).show();
            editTexCuisine_name.setVisibility(View.VISIBLE);
            editTexCuisine_name.requestFocus();
            return false;
        }
        if (!checkBox_add_rest.isChecked()) {
            Toast.makeText(RedisterasRestaurantActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_tearms), Toast.LENGTH_SHORT).show();
            checkBox_add_rest.requestFocus();
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

                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(RedisterasRestaurantActivity.this));
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

    private void getArealist() {


        Log.e(TAG, "in getArealist");

        progressBar = ProgressDialog.show(this, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.putOpt(AppConstants.COUNTRY_ID, PreferenceUtil.getCountryId(RedisterasRestaurantActivity.this));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(RedisterasRestaurantActivity.this));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    //startSpinwheel(false, true);
                    makeJsonAreaStringReq(AppConstants.EatndRun_GETAREA,
                            jsonObject);


                } catch (Exception e) {
                    Log.e(TAG, "Execption while Putting Json");
                    e.printStackTrace();
                }

            }
        }).start();


    }

    private void getCitylist() {

        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.putOpt(AppConstants.COUNTRY_ID, PreferenceUtil.getCountryId(this));

            jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(RedisterasRestaurantActivity.this));
            jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


            makeJsongetCitylistStringReq(AppConstants.EatndRun_GETCITY,
                    jsonObject);


        } catch (Exception e) {
            progressBar.dismiss();
            Log.e(TAG, "Execption while Putting Json");
            e.printStackTrace();
        }
    }

    private void getCuisinelist() {

        // progressBar = ProgressDialog.show(RedisterasRestaurantActivity.this, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(RedisterasRestaurantActivity.this));

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonCuisinListStringReq(AppConstants.EatndRun_GETRESTAURANTCUISINES, jsonObject);


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
            RequestQueue queue = Volley.newRequestQueue(RedisterasRestaurantActivity.this);

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

                                    countrynameAdapter = new CountryNameAdapter(RedisterasRestaurantActivity.this,
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
                            Toast.makeText(RedisterasRestaurantActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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

    private String makeJsonAreaStringReq(String urlPost, final JSONObject jsonObject) {
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
                                    JSONArray areaArr = jsonObj.getJSONArray("areaList");
                                    arrayListArea = new ArrayList<Area>();

                                    arrayListArea.add(new Area("0", getString(R.string.homefrag_spinner_select_area)));


                                    for (int t = 0; t < areaArr.length(); t++) {

                                        JSONObject jObjArrayObjects = areaArr.getJSONObject(t);

                                        String id = jObjArrayObjects.getString("AreaId");
                                        String name = jObjArrayObjects.getString("AreaName");

                                        area = new Area(id, name);

                                        arrayListArea.add(area);

                                    }

                                    areanameAdapter = new AreaNameAdapter(RedisterasRestaurantActivity.this,
                                            arrayListArea);
                                    if (areanameAdapter != null) {
                                        spinnerTexArea.setAdapter(areanameAdapter);


                                    }

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
                            Toast.makeText(RedisterasRestaurantActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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

    private String makeJsongetCitylistStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(RedisterasRestaurantActivity.this);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {
                                    JSONArray cityArr = jsonObj.getJSONArray("cityList");
                                    cityDataArrayList = new ArrayList<CityData>();

                                    cityDataArrayList.add(new CityData("0", getString(R.string.homefrag_spinner_select_city)));


                                    for (int t = 0; t < cityArr.length(); t++) {

                                        JSONObject jObjArrayObjects = cityArr.getJSONObject(t);

                                        String id = jObjArrayObjects.getString("CityId");
                                        String name = jObjArrayObjects.getString("CityName");

                                        cityData = new CityData(id, name);

                                        cityDataArrayList.add(cityData);

                                    }

                                    cityListAdapter = new CityListAdapter(RedisterasRestaurantActivity.this,
                                            cityDataArrayList);
                                    spinnerTexCity.setAdapter(cityListAdapter);
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
                            Toast.makeText(RedisterasRestaurantActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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

    private String makeJsonCuisinListStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    JSONArray CuisineArr = jsonObj.getJSONArray("cuisineList");
                                    cuisineListDataArrayList = new ArrayList<CuisineListData>();
//String cuisineId, String cuisineName, String cuisineDescription, String cuisineImage, String languageId, String sortOrder, boolean isActive, String menuItemCount
                                    cuisineListDataArrayList.add(new CuisineListData("0", getString(R.string.register_rest_spinner_select_cuisine), "", "", "", "", true, ""));

                                    for (int t = 0; t < CuisineArr.length(); t++) {
                                        JSONObject jsonObject1 = CuisineArr.getJSONObject(t);

                                        String CuisineId = jsonObject1.getString("CuisineId");
                                        String CuisineName = jsonObject1.getString("CuisineName");
                                        String CuisineDescription = jsonObject1.getString("CuisineDescription");
                                        String CuisineImage = jsonObject1.getString("CuisineImage");
                                        String LanguageId = jsonObject1.getString("LanguageId");
                                        String SortOrder = jsonObject1.getString("SortOrder");
                                        boolean IsActive = jsonObject1.getBoolean("IsActive");
                                        String MenuItemCount = jsonObject1.getString("MenuItemCount");

                                        cuisineListData = new CuisineListData(CuisineId, CuisineName, CuisineDescription, CuisineImage, LanguageId, SortOrder, IsActive, MenuItemCount);
                                        cuisineListDataArrayList.add(cuisineListData);
                                        progressBar.dismiss();

                                    }
                                    progressBar.dismiss();

                                }
                                cuisineListAdapter = new CuisineListAdapter(RedisterasRestaurantActivity.this, cuisineListDataArrayList);

                                spinnerTexCuisine.setAdapter(cuisineListAdapter);
                                progressBar.dismiss();

                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
                                xx.toString();
                                progressBar.dismiss();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    progressBar.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RedisterasRestaurantActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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

    private void getlocation() {


        String lat = PreferenceUtil.getlat(RedisterasRestaurantActivity.this);
        String lng = PreferenceUtil.getlng(RedisterasRestaurantActivity.this);

        Log.e("getlocation === ", lat + "  " + lng);

    }
}
