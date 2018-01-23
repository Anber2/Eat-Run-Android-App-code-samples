package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;
import com.mawaqaa.eatandrun.adapter.RestaurantMenuItemListAdapter;
import com.mawaqaa.eatandrun.data.RestaurantMenuItemListData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantMenuItemListFragment extends EatndRunBaseFragment {

    String TAG = "ResMenuFrg";

    RestaurantMenuItemListAdapter restaurantMenuItemListAdapter;
    ListView listvwmenuitemlist;

    ArrayList<RestaurantMenuItemListData> restaurantMenuItemListDataArrayList;
    RestaurantMenuItemListData restaurantMenuItemListData;
    String message, Success;
    String MenuItemId;
    private ProgressDialog progressBar;

    TextView lbl_menuitemheading;

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
        //View v = inflater.inflate(R.layout.fragment_registerorlogin, container, false);
        View v = inflater.inflate(R.layout.resturantmenu_itemlist, container, false);
        listvwmenuitemlist = (ListView) v.findViewById(R.id.listvwmenuitemlist);

        lbl_menuitemheading = (TextView) v.findViewById(R.id.lbl_menuitemheading);

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt(AppConstants.EatndRun_RES_ID, PreferenceUtil.getResID(getActivity()));
                    jsonObject.putOpt(AppConstants.EatndRun_MENUSECTIONID, PreferenceUtil.getMenuSectionId(getActivity()));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonRestaurantMenuItemListStringReq(AppConstants.EatndRun_GETRESTAURANTMENUITEMBYMENUSECTIONID, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();


       /* listvwmenuitemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PreferenceUtil.setMenuItemId(getActivity(),restaurantMenuItemListDataArrayList.get(position).toString());
                Log.e("OnItemClick ResID:::: ", restaurantMenuItemListDataArrayList.get(position).toString());

                Fragment fragment = new RestaurantMenuItemListDetailFragment();
                EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
            }
        });*/

        return v;
    }


    private String makeJsonRestaurantMenuItemListStringReq(String urlPost, final JSONObject jsonObject) {
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


                                    JSONArray jsonArray = jsonObjResp.getJSONArray("menuItemList");
                                    restaurantMenuItemListDataArrayList = new ArrayList<RestaurantMenuItemListData>();

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);

                                        String MenuItemId = jsonObject1.getString("MenuItemId");
                                        String MenuItemName = jsonObject1.getString("MenuItemName");
                                        String MenuItemDescription = jsonObject1.getString("MenuItemDescription");
                                        String MenuItemImage = jsonObject1.getString("MenuItemImage");
                                        String Price = jsonObject1.getString("Price");

                                        restaurantMenuItemListData = new RestaurantMenuItemListData(MenuItemId, MenuItemName, MenuItemDescription, MenuItemImage, Price);
                                        restaurantMenuItemListDataArrayList.add(restaurantMenuItemListData);

                                    }

                                }

                                restaurantMenuItemListAdapter = new RestaurantMenuItemListAdapter(Activity, restaurantMenuItemListDataArrayList);

                                listvwmenuitemlist.setAdapter(restaurantMenuItemListAdapter);
                                lbl_menuitemheading.setText(AppConstants.MenuSectionName);

                                if(listvwmenuitemlist.getCount() == 0){
                                    Toast.makeText(getActivity(), "List is currently empty", Toast.LENGTH_LONG).show();

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

