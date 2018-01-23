package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.mawaqaa.eatandrun.adapter.RestaurantMenuAdapter;
import com.mawaqaa.eatandrun.data.HomeBannerClass;
import com.mawaqaa.eatandrun.data.RestaurantMenuData;

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
public class RestaurantMenuFragment extends EatndRunBaseFragment implements TextWatcher {

    String TAG = "RestaurantMenuFragment";


    RestaurantMenuAdapter restaurantMenuAdapter;
    ListView listView;
    ArrayList<RestaurantMenuData> restaurantMenuDataArrayList;
    RestaurantMenuData restaurantMenuData;
    String MenuSectionId;
    EditText editText_menuSearch;
    String searchKey;
    // ads
    HashMap<String, String> Hash_file_maps;
    SliderLayout sliderLayout;
    HomeBannerClass homeBanner;
    ArrayList<HomeBannerClass> homeBannerClassArrayList;
    LinearLayout sliderLayoutLiner_home;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.resturantmenu, container, false);

        listView = (ListView) v.findViewById(R.id.listvwrestaurantmenu);
        editText_menuSearch = (EditText) v.findViewById(R.id.editText_menuSearch);
        editText_menuSearch.addTextChangedListener(this);
        sliderLayout = (SliderLayout) v.findViewById(R.id.slider);
        sliderLayoutLiner_home = (LinearLayout) v.findViewById(R.id.sliderLayoutLiner_home);
        currentAdClass = 3;

        getMenuItems();

        getAds();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppConstants.MenuSectionName = restaurantMenuDataArrayList.get(position).getMenuSectionName();

            }
        });

        return v;
    }

    private void getMenuItems() {



        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt(AppConstants.EatndRun_RES_ID, PreferenceUtil.getResID(Activity));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonRestaurantMenuStringReq(AppConstants.EatndRun_GETRESTAURANTMENUS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    private String makeJsonRestaurantMenuStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    JSONArray jsonArray = jsonObj.getJSONArray("menuSectionList");
                                    restaurantMenuDataArrayList = new ArrayList<RestaurantMenuData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        MenuSectionId = jsonObject1.getString("MenuSectionId");
                                        String MenuSectionName = jsonObject1.getString("MenuSectionName");
                                        String LanguageId = jsonObject1.getString("LanguageId");
                                        String SortOrder = jsonObject1.getString("SortOrder");
                                        String IsActive = jsonObject1.getString("IsActive");
                                        String MenuSectionCount = jsonObject1.getString("MenuSectionCount");
                                        String Logo = jsonObject1.getString("MenuSectionImage");

                                        restaurantMenuData = new RestaurantMenuData(MenuSectionId, MenuSectionName, LanguageId, SortOrder, IsActive, MenuSectionCount, Logo);
                                        restaurantMenuDataArrayList.add(restaurantMenuData);

                                    }

                                }

                                restaurantMenuAdapter = new RestaurantMenuAdapter(Activity, restaurantMenuDataArrayList);

                                listView.setAdapter(restaurantMenuAdapter);

                                if (listView.getCount() == 0) {

                                    Toast.makeText(getActivity(), R.string.restaurantmenufragment__reg_cust_txt_empty_txt, Toast.LENGTH_LONG).show();

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!editText_menuSearch.getText().toString().equals("")) {
            searchKey = s.toString().replaceAll("[$,.]", "");

            listView.setAdapter(null);

            sendSearchKey();


        } else {
            getMenuItems();
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
                    restObj.putOpt("MenuName", searchKey);
                    restObj.putOpt("Rest_ID", PreferenceUtil.getResID(getActivity()));

                    restObj.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(getActivity()));

                    restObj.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonRestaurantMenuStringReq(AppConstants.EatndRun_SearchRestaurantMenus, restObj);
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

