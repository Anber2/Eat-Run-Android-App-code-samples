package com.mawaqaa.eatandrun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.AdsActivity;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;
import com.mawaqaa.eatandrun.activity.ExpAdsActivity;
import com.mawaqaa.eatandrun.adapter.RestaurantListAdapter;
import com.mawaqaa.eatandrun.data.HomeBannerClass;
import com.mawaqaa.eatandrun.data.RestaurantListData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.Constants.AppConstants.currentAdClass;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantListFragment extends EatndRunBaseFragment implements TextWatcher {

    String TAG = "ResListFrg";
    RestaurantListAdapter rlistadapter;
    ListView listvwrestaurantlist;
    ArrayList<RestaurantListData> restaurantListDataArraylist;
    RestaurantListData restaurantListData;
    TextView lbl_reg_rest_value, lbl_reg_cust_value;
    EditText editText_restSearch;
    String searchKey;
    // ads
    HashMap<String, String> Hash_file_maps ;
    SliderLayout sliderLayout;
    HomeBannerClass homeBanner;
    ArrayList<HomeBannerClass> homeBannerClassArrayList;
    LinearLayout sliderLayoutLiner_home;
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
        View v = inflater.inflate(R.layout.resturantlist, container, false);


        listvwrestaurantlist = (ListView) v.findViewById(R.id.listvwrestaurantlist);
        editText_restSearch = (EditText) v.findViewById(R.id.editText_restSearch);
        editText_restSearch.addTextChangedListener(this);


        lbl_reg_rest_value = (TextView) v.findViewById(R.id.lbl_reg_rest_value);
        lbl_reg_cust_value = (TextView) v.findViewById(R.id.lbl_reg_cust_value);
        lbl_reg_rest_value.setText(PreferenceUtil.getRegRest(getActivity()));
        lbl_reg_cust_value.setText(PreferenceUtil.getRegCus(getActivity()));

        sliderLayout = (SliderLayout) v.findViewById(R.id.slider);
        sliderLayoutLiner_home = (LinearLayout) v.findViewById(R.id.sliderLayoutLiner_home);

        currentAdClass = 2;
        getRestList();
        getAds();

        return v;
    }

    private void getRestList() {

        // progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    JSONObject restObj = new JSONObject();

                    restObj.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));
                    restObj.putOpt(AppConstants.COUNTRY_ID, PreferenceUtil.getCountryId(getActivity()));
                    restObj.putOpt(AppConstants.AREAId, PreferenceUtil.getAreaId(getActivity()));
                    restObj.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));
                    restObj.putOpt("Cus_Latitude", PreferenceUtil.getlat(Activity));
                    restObj.putOpt("Cus_Longitude", PreferenceUtil.getlng(Activity));
                    restObj.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonStringReq(AppConstants.EatndRun_GETRESTAURANTLIST, restObj);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }

    private String makeJsonStringReq(String urlPost, final JSONObject jsonObject) {
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
                                JSONObject jsonObjResp = new JSONObject(response);
                                if (jsonObjResp != null) {

                                    JSONArray jsonArray = jsonObjResp.getJSONArray("apirestaurantmodelList");
                                    restaurantListDataArraylist = new ArrayList<RestaurantListData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        String Res_Id = jsonObject1.getString("Res_Id");
                                        String RestaurantName = jsonObject1.getString("RestaurantName");
                                        String Res_Address = jsonObject1.getString("Res_Address");
                                        String Res_Distance = jsonObject1.getString("Distance");
                                        String Res_Logo = jsonObject1.getString("RestaurantLogo");
                                        String Res_CityName = jsonObject1.getString("CityName");
                                        String RatingViewCount = jsonObject1.getString("RatingViewCount");
                                        String RatingStarCount = jsonObject1.getString("RatingStarCount");

                                        restaurantListData = new RestaurantListData(Res_Id, RestaurantName, Res_Address, Res_Distance, Res_Logo, Res_CityName, RatingViewCount, RatingStarCount);
                                        restaurantListDataArraylist.add(restaurantListData);

                                    }
                                }


                                rlistadapter = new RestaurantListAdapter(getActivity(), restaurantListDataArraylist);


                                listvwrestaurantlist.setAdapter(rlistadapter);

                                //     progressBar.dismiss();


                                if (listvwrestaurantlist.getCount() == 0) {

                                    Toast.makeText(getActivity(), R.string.restaurantlistfragment_empty_msg, Toast.LENGTH_LONG).show();

                                }


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
                    // progressBar.dismiss();
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
            //progressBar.dismiss();
            e.toString();
            return e.toString();
        }

        return resultConn[0];

    }


    public void getAds() {


        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    JSONObject jsonObject = new JSONObject();


                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(getActivity()));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);
                    jsonObject.putOpt("AreaId", currentAdClass);

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

                                                            PreferenceUtil.setAdUrl(getActivity(), slider.getUrl());

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


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!editText_restSearch.getText().toString().equals("")) {
            searchKey = s.toString().replaceAll("[$,.]", "");

            listvwrestaurantlist.setAdapter(null);

            sendSearchKey();


        } else {
            getRestList();
        }

    }

    private void sendSearchKey() {

        //progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    JSONObject restObj = new JSONObject();

                    restObj.putOpt("Cust_ID", PreferenceUtil.getUserId(getActivity()));
                    restObj.putOpt("RestaurantName", searchKey);

                    restObj.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(getActivity()));
                    restObj.putOpt("Cus_Latitude", PreferenceUtil.getlat(getActivity()));
                    restObj.putOpt("Cus_Longitude", PreferenceUtil.getlng(getActivity()));
                    restObj.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonStringReq(AppConstants.EatndRun_SearchRestaurant, restObj);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

