package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.GPSTracker;
import com.mawaqaa.eatandrun.Utilities.MyService;
import com.mawaqaa.eatandrun.Utilities.NotificationUtils;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.AdsActivity;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;
import com.mawaqaa.eatandrun.activity.EatndRunMainActivity;
import com.mawaqaa.eatandrun.activity.ExpAdsActivity;
import com.mawaqaa.eatandrun.adapter.AdBannerAdapter;
import com.mawaqaa.eatandrun.adapter.AreaNameAdapter;
import com.mawaqaa.eatandrun.adapter.CityListAdapter;
import com.mawaqaa.eatandrun.adapter.CountryNameAdapter;
import com.mawaqaa.eatandrun.data.Area;
import com.mawaqaa.eatandrun.data.CityData;
import com.mawaqaa.eatandrun.data.Country;
import com.mawaqaa.eatandrun.data.HomeBannerClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.mawaqaa.eatandrun.R.id.slider;
import static com.mawaqaa.eatandrun.Utilities.PreferenceUtil.getCountryId;

/**
 * Created by HP on 11/27/2017.
 */
public class HomeFrag extends EatndRunBaseFragment {

    public String lat, lng;
    String TAG = "HomFrg";
    Button btn_FindRestaurants,btn_home_JoinBill;
    TextView lbl_reg_rest_value, lbl_reg_cust_value, lbl_hello;
    Spinner spinner_country, spinner_area, spinnerTexCity;
    Country country;
    ArrayList<Country> arrayListCountry;
    CountryNameAdapter countrynameAdapter;
    RelativeLayout Rel_lay_custguide, Rel_lay_patent;
    Area area, area2;
    ArrayList<Area> arrayListArea;
    AreaNameAdapter areanameAdapter;
    String spinitemCountryId, spinitemAreaId, spinitemCityId, CityID;
    Intent serviceIntent;
    String Success;
    LinearLayout city_linyer_homefrag;

    CityListAdapter cityListAdapter;
    CityData cityData;
    ArrayList<CityData> cityDataArrayList;
    GPSTracker gps;
    // ads
    HashMap<String, String> Hash_file_maps;
    SliderLayout sliderLayout;
    HomeBannerClass homeBanner;
    ArrayList<HomeBannerClass> homeBannerClassArrayList;
    LinearLayout sliderLayoutLiner_home;

    AdBannerAdapter adBannerAdapter;
    EatndRunMainActivity eatndRunMainActivity;
    String adType;

    //progress bar
    private ProgressDialog progressBar;

    public static String getDeviceID() throws Throwable {
       /* String deviceId;
        final TelephonyManager mTelephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }*/

        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        return deviceId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();
        serviceIntent = new Intent(Activity, MyService.class);

    }

    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;

        NotificationUtils.clearNotifications(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initview(v);
        callhomeservice();
        getDeviceRegistration();

        Activity.startService(serviceIntent);
        //getlocation();
        getAds();


        return v;
    }

    private void getArealist() {


        Log.e(TAG, "in getArealist");

        progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.putOpt(AppConstants.COUNTRY_ID, getCountryId(getActivity()));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));
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

    public boolean FindRestaurantsAuthentication() {


        if (country.getCountryId().equals("0") || country.getCountryId().equals(null)) {
            Log.e("getCountryId", "" + country.getCountryId());
            Toast.makeText(getActivity(), getString(R.string.homefrag_spinner_select_country_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(city_linyer_homefrag.getVisibility() == View.GONE)) {
            if (cityData.getCityId().equals("0") || cityData.getCityId().equals(null)) {
                Toast.makeText(getActivity(), getString(R.string.homefrag_spinner_select_city_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (area.getAreaId().equals("0") || area.getAreaId().equals(null)) {
            Log.e("getAreaId", "" + area.getAreaId());
            Toast.makeText(getActivity(), getString(R.string.homefrag_spinner_select_area_error), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private void initview(View v) {


        lbl_reg_rest_value = (TextView) v.findViewById(R.id.lbl_reg_rest_value);
        lbl_reg_cust_value = (TextView) v.findViewById(R.id.lbl_reg_cust_value);
        lbl_hello = (TextView) v.findViewById(R.id.lbl_hello);
        sliderLayout = (SliderLayout) v.findViewById(slider);
        Rel_lay_custguide = (RelativeLayout) v.findViewById(R.id.Rel_lay_custguide);
        Rel_lay_patent = (RelativeLayout) v.findViewById(R.id.Rel_lay_patent);


        city_linyer_homefrag = (LinearLayout) v.findViewById(R.id.city_linyer_homefrag);
        sliderLayoutLiner_home = (LinearLayout) v.findViewById(R.id.sliderLayoutLiner_home);
        btn_home_JoinBill = (Button) v.findViewById(R.id.btn_home_JoinBill);

        btn_home_JoinBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinBillOfJoinBillSplitEvently fragmentJoinBillSplit = new JoinBillOfJoinBillSplitEvently();

                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(fragmentJoinBillSplit, false, true);
            }
        });
        btn_FindRestaurants = (Button) v.findViewById(R.id.btn_FindRestaurants);

        btn_FindRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if (FindRestaurantsAuthentication()) {

                        Activity.startService(serviceIntent);
                        getlocation();
                        Fragment RestFrag = new RestaurantListFragment();
                        Activity.pushFragments(RestFrag, false, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Rel_lay_custguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment RestFrag = new CustGuideFragment();
                Activity.pushFragments(RestFrag, false, true);

            }
        });

        Rel_lay_patent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment RestFrag = new PatentFragment();
                Activity.pushFragments(RestFrag, false, true);
            }
        });
        spinnerTexCity = (Spinner) v.findViewById(R.id.spinner_city_homefrag);

        spinner_area = (Spinner) v.findViewById(R.id.spinner_area);
        spinner_country = (Spinner) v.findViewById(R.id.spinner_country);


        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinner_country.getSelectedItemPosition();
                country = arrayListCountry.get(pos);
                spinitemCountryId = (country.getCountryId());
                if (!spinitemCountryId.equals("0")) {
                    PreferenceUtil.setCountryId(getActivity(), spinitemCountryId);

                    if (spinitemCountryId.equals("1")) {
                        getArealist();
                        CityID = "0";
                        PreferenceUtil.setCityId(getActivity(), CityID);

                        city_linyer_homefrag.setVisibility(View.GONE);
                        spinner_area.setSelection(0);

                    } else {
                        getCitylist();
                        city_linyer_homefrag.setVisibility(View.VISIBLE);
                        spinnerTexCity.setSelection(0);
                        spinner_area.setAdapter(null);


                    }
                } else if (spinitemCountryId.equals("0")) {
                    spinner_area.setAdapter(null);
                    spinnerTexCity.setAdapter(null);
                    city_linyer_homefrag.setVisibility(View.VISIBLE);

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
                    PreferenceUtil.setCityId(getActivity(), cityData.getCityId());
                    getArealist();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinner_area.getSelectedItemPosition();
                area = arrayListArea.get(pos);
                spinitemAreaId = (area.getAreaId());
                Log.e("hghg", "spinitemAreaId" + area.getAreaId());
                PreferenceUtil.setAreaId(getActivity(), area.getAreaId());
                /*if (!spinitemAreaId.equals("")) {
                    getArealist();

                } else {
                    Toast.makeText(Activity, "Please Select Area", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void callhomeservice() {


        progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonHomeStringReq(AppConstants.EatndRun_HomeContents, jsonObject);

                    //startSpinwheel(false, true);


                } catch (Exception e) {
                    Log.e(TAG, "Execption while Putting Json");
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void getCountrylist() {

        // progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    //  startSpinwheel(false, true);

                    makeJsongetCountrylistStringReq(AppConstants.EatndRun_GETCOUNTRY,
                            jsonObject);


                } catch (Exception e) {
                    Log.e(TAG, "Execption while Putting Json");
                    e.printStackTrace();
                }

            }
        }).start();

    }

    ///////////////////////////////////////////////////////////

    private void getCitylist() {

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.putOpt(AppConstants.COUNTRY_ID, getCountryId(getActivity()));

            jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(getActivity()));
            jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


            makeJsongetCitylistStringReq(AppConstants.EatndRun_GETCITY,
                    jsonObject);


        } catch (Exception e) {
            progressBar.dismiss();
            Log.e(TAG, "Execption while Putting Json");
            e.printStackTrace();
        }
    }

    private String makeJsongetCitylistStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    cityListAdapter = new CityListAdapter(getActivity(),
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

    private String makeJsonDeviceRegistrationStringReq(String urlPost, final JSONObject jsonObject) {
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
                                JSONObject jsonObjResp = new JSONObject(response);
                                if (jsonObjResp != null) {

                                    Success = jsonObjResp.getString("Success");
                                }

                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
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
                            Toast.makeText(Activity, error.toString(), Toast.LENGTH_LONG).show();

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

    private String makeJsonHomeStringReq(String urlPost, final JSONObject jsonObject) {

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

                                    if (jsonObj.getString(AppConstants.IsSucces).equalsIgnoreCase("Success")) {
                                        lbl_reg_rest_value.setText(jsonObj.getString(AppConstants.RES_COUNT));
                                        lbl_reg_cust_value.setText(jsonObj.getString(AppConstants.CUS_COUNT));
                                        lbl_hello.setText(getString(R.string.homefrag_hello) + " " + jsonObj.getString(AppConstants.CUST_FirstName));

                                        boolean ShowPatent = jsonObj.getBoolean("ShowPatent");

                                        if (ShowPatent) {
                                            Rel_lay_patent.setVisibility(View.VISIBLE);
                                        }


                                        PreferenceUtil.setRegRest(getActivity(), jsonObj.getString(AppConstants.RES_COUNT));
                                        PreferenceUtil.setRegCus(getActivity(), jsonObj.getString(AppConstants.CUS_COUNT));


                                        getCountrylist();
                                    } else {

                                        // Toast.makeText(Activity, jsonObj.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), R.string.string_semething_went_wrong, Toast.LENGTH_LONG).show();

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

    private String makeJsongetCountrylistStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    countrynameAdapter = new CountryNameAdapter(getActivity(),
                                            arrayListCountry);
                                    if (countrynameAdapter != null) {
                                        spinner_country.setAdapter(countrynameAdapter);


                                        spinner_country.setSelection(Integer.parseInt(PreferenceUtil.getCountryId(getActivity())));


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

    /////////////////////////////////////////////////////////

    private String makeJsonAreaStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    areanameAdapter = new AreaNameAdapter(HomeFrag.this.Activity,
                                            arrayListArea);
                                    if (areanameAdapter != null) {
                                        spinner_area.setAdapter(areanameAdapter);


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

    private void getDeviceRegistration() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();


                    jsonObject.putOpt("Cus_Id", PreferenceUtil.getUserId(getApplicationContext()));
                    jsonObject.putOpt(AppConstants.EatndRun_DEVICEREGISTRATION_DEVICE_PLATFORM, "0");
                    jsonObject.putOpt("Device_Id", getDeviceID());
                    jsonObject.putOpt(AppConstants.EatndRun_DEVICEREGISTRATION_DEVICE_TOKEN,
                            PreferenceUtil.getDeviceToken(getApplicationContext()));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


                    makeJsonDeviceRegistrationStringReq(AppConstants.EatndRun_DEVICEREGISTRATION, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }).start();


    }

    private void getlocation() {
        /*lat = PreferenceUtil.getlat(Activity);
        lng = PreferenceUtil.getlng(Activity);
        Log.e("LOCCCCCCCCCCCCCCCCCCCC", lat + "  " + lng);
*/

        gps = new GPSTracker(Activity);

        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

        } else {

            gps.showSettingsAlert();
        }


    }

    /////////////////////////////////////////////////////

    public void getAds() {


        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    JSONObject jsonObject = new JSONObject();


                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(getActivity()));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);
                    jsonObject.putOpt("AreaId", 1);

                    makeJsonGetAdsStringReq(AppConstants.EatndRun_APIGETADVERTISEMENTBANNER, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();


    }

    private String makeJsonGetAdsStringReq(String urlPost, final JSONObject jsonObject) {
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
                                if (jsonObj != null) {
                                    homeBannerClassArrayList = new ArrayList<HomeBannerClass>();
                                    Hash_file_maps = new HashMap<String, String>();

                                    JSONArray fullScreenAdArray = jsonObj.getJSONArray("AdvertisementInterstitialList");
                                    JSONArray barAdArray = jsonObj.getJSONArray("AdvertisementBannerList");
                                    final JSONArray expandableAdArray = jsonObj.getJSONArray("AdvertisementExpandableList");
                                    Log.d(TAG, " **- -" + fullScreenAdArray.length());


                                    if (fullScreenAdArray.length() != 0) {

                                        startActivity(new Intent(Activity, AdsActivity.class));

                                    }


                                    if (barAdArray.length() == 0) {
                                        sliderLayoutLiner_home.setVisibility(View.GONE);
                                    } else {
                                        sliderLayoutLiner_home.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < barAdArray.length(); i++) {
                                            JSONObject arr = barAdArray.getJSONObject(i);
                                            String Image = arr.getString("Image");
                                            String Duration = arr.getString("Duration");


                                            homeBanner = new HomeBannerClass(Image, "");
                                            Hash_file_maps.put(AppConstants.EatndRun_BASE_IMAGES_URL + homeBanner.getImgURL(), AppConstants.EatndRun_BASE_IMAGES_URL + homeBanner.getImgURL());

                                        }

                                        for (final String name : Hash_file_maps.keySet()) {

                                            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                                            textSliderView.description(name)
                                                    .image(Hash_file_maps.get(name))
                                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                                        @Override
                                                        public void onSliderClick(BaseSliderView slider) {

                                                        /*try {
                                                                if (expandableAdArray.length() != 0) {


                                                                    for (int i = 0; i < expandableAdArray.length(); i++) {
                                                                        JSONObject arr = expandableAdArray.getJSONObject(i);

                                                                        String exImage = arr.getString("Image");

                                                                        PreferenceUtil.setAdUrl(getActivity(), slider.getUrl());

                                                                    }

                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }*/
                                                            PreferenceUtil.setAdUrl(getActivity(), slider.getUrl());
                                                            Log.d(TAG, " **- -" + slider.getUrl());

                                                            startActivity(new Intent(Activity, ExpAdsActivity.class));

                                                        }
                                                    });

                                            textSliderView.getEmpty();
                                            textSliderView.bundle(new Bundle());
                                            textSliderView.getBundle()
                                                    .putString("extra", name);

                                            sliderLayout.addSlider(textSliderView);
                                        }
                                    }


                                }

                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
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
                            Toast.makeText(getActivity(), R.string.string_semething_went_wrong, Toast.LENGTH_LONG).show();

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

