package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.Constants.AppConstants.EatndRun_BASE_APP_URL;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantOfferItemListDetailFragment extends EatndRunBaseFragment {

    String TAG = "ResMenuFrg";

    TextView lbllistitemdtail, lbllistitemdtailpricevalue, lbllistitemdtailDescription, lbllistofferdtailOldpricevalue;
    String message, Success;
    private ProgressDialog progressBar;
    ImageView imgbanner_lstitemdtail;

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
        View v = inflater.inflate(R.layout.resturantoffer_itemlistdetails, container, false);

        lbllistitemdtail = (TextView) v.findViewById(R.id.lbllistofferdtail);
        lbllistitemdtailpricevalue = (TextView) v.findViewById(R.id.lbllistofferdtailpricevalue);
        lbllistofferdtailOldpricevalue = (TextView) v.findViewById(R.id.lbllistofferdtailOldpricevalue);
        lbllistitemdtailDescription = (TextView) v.findViewById(R.id.lbllistofferdtailDescription);

        imgbanner_lstitemdtail = (ImageView) v.findViewById(R.id.imgbanner_lstitemdtail);

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.CUST_ID, PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt(AppConstants.EatndRun_MENUITEMID, PreferenceUtil.getMenuItemId(getActivity()));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonRestaurantOfferItemListDetailStringReq(AppConstants.EatndRun_GETRESTAURANTOFFERSDETAILSBYMENUITEMID, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();


        return v;
    }

    private String makeJsonRestaurantOfferItemListDetailStringReq(String urlPost, final JSONObject jsonObject) {
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


                                    String MenuItemId = jsonObjResp.getString("MenuItemId");
                                    String MenuItemName = jsonObjResp.getString("MenuItemName");
                                    String MenuItemDescription = jsonObjResp.getString("MenuItemDescription");
                                    String MenuSectionId = jsonObjResp.getString("MenuSectionId");
                                    String MenuSectionName = jsonObjResp.getString("MenuSectionName");
                                    String MenuItemImage = jsonObjResp.getString("MenuItemImage");
                                    String Price = jsonObjResp.getString("Price");
                                    String Discount = jsonObjResp.getString("Discount");
                                    String AvailableFrom = jsonObjResp.getString("AvailableFrom");
                                    String AvailableTo = jsonObjResp.getString("AvailableTo");
                                    String IsActive = jsonObjResp.getString("IsActive");
                                    message = jsonObjResp.getString("Message");

                                    lbllistitemdtail.setText(MenuItemName);
                                    lbllistitemdtailpricevalue.setText(Price);
                                    lbllistofferdtailOldpricevalue.setText(Discount);
                                    lbllistitemdtailDescription.setText(MenuItemDescription);

                                    Picasso.with(getActivity()).load(EatndRun_BASE_APP_URL+MenuItemImage).placeholder(R.drawable.list_productimg).into( imgbanner_lstitemdtail);

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

