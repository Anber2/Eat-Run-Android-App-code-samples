package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mawaqaa.eatandrun.activity.LoginActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 11/27/2017.
 */
public class RedisterasRestaurantFragment extends EatndRunBaseFragment implements View.OnClickListener {

    public String lat, lng;
    String TAG = "LgnFrag";
    ImageView btn_login;
    EditText editTextNameRest, editTexpswdRest, editTexfnamRest, editTexlnamRest, editTexlpersonchrgeRest, editTexemailRest, editTexcountryRest, editTexmobileRest, editTexlandlineRest, editTextAddressRest;
    Button btn_reg_Rest;
    String message, Success;


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
        //((AjaratyMainActivity) getActivity()).hideLogo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.regasrestaurantfragment, container, false);

        initView(v);


        return v;
    }

    private void initView(View v) {

        btn_reg_Rest = (Button) v.findViewById(R.id.btn_reg_Rest);
        editTextNameRest = (EditText) v.findViewById(R.id.editTextNameRest);
        editTexpswdRest = (EditText) v.findViewById(R.id.editTexpswdRest);
        editTexfnamRest = (EditText) v.findViewById(R.id.editTexfnamRest);
        editTexlnamRest = (EditText) v.findViewById(R.id.editTexlnamRest);
        editTexlpersonchrgeRest = (EditText) v.findViewById(R.id.editTexlpersonchrgeRest);
        editTexemailRest = (EditText) v.findViewById(R.id.editTexemailRest);
        editTexmobileRest = (EditText) v.findViewById(R.id.editTexmobileRest);
        editTexlandlineRest = (EditText) v.findViewById(R.id.editTexlandlineRest);
        editTextAddressRest = (EditText) v.findViewById(R.id.editTextAddressRest);


        btn_reg_Rest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if (registerAuthentication()) {

            try {
                JSONObject registrationJsonObject = new JSONObject();


                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_USERNAME, editTextNameRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_PASSWORD, editTexpswdRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_COMPANYNAME, editTexfnamRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_RESTAURANTNAME, editTexlnamRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_PERSONINCHARGE, editTexlpersonchrgeRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_EMAIL, editTexemailRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_COUNTRYID, "05");
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_MOBILE, editTexmobileRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_TELEPHONE, editTexlandlineRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_ADDRESS, editTextAddressRest.getText().toString());
                registrationJsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_LATITUDE, PreferenceUtil.getlat(Activity));
                registrationJsonObject.putOpt(AppConstants.EatndRun_RES_LONGITUDE, PreferenceUtil.getlng(Activity));

                registrationJsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                //startSpinwheel(false, true);
                makeJsonRegistrationStringReq(AppConstants.EatndRun_ADDRESTAURANT,
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

                                if (jsonObj != null) {
                                    message = jsonObj.getString("Message");

                                    Success = jsonObj.getString("Success");

                                }

                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                                if (Success.equals("Success"))
                                //Activity.pushFragments(new LoginFragment(), false, true);
                                {
                                    Intent i = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(i);
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


        if (editTextNameRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "name_empty", Toast.LENGTH_LONG).show();
            editTextNameRest.requestFocus();
            return false;
        }
        if (editTexfnamRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "first_name_empty", Toast.LENGTH_LONG).show();
            editTexfnamRest.requestFocus();
            return false;
        }
        if (editTexlnamRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "last_name_empty", Toast.LENGTH_LONG).show();
            editTexlnamRest.requestFocus();
            return false;
        }
        if (editTexemailRest.getText().toString().length() <= 0) {
            Toast.makeText(Activity, "email_name_empty", Toast.LENGTH_LONG).show();
            editTexemailRest.requestFocus();
            return false;
        } else {
            if (!editTexemailRest.getText().toString().matches(AppConstants.EMAIL_PATTERN)) {
                Toast.makeText(Activity, "invalid_email", Toast.LENGTH_LONG).show();
                editTexemailRest.requestFocus();
                return false;
            }
        }

        if (editTexpswdRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "password_field_empty", Toast.LENGTH_LONG).show();
            editTexpswdRest.requestFocus();
            return false;
        }
        if (editTexcountryRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "country_empty", Toast.LENGTH_LONG).show();
            editTexcountryRest.requestFocus();
            return false;
        }
        if (editTexmobileRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "mobile_no_empty", Toast.LENGTH_LONG).show();
            editTexmobileRest.requestFocus();
            return false;
        }

        if (editTexlpersonchrgeRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "person chrge_empty", Toast.LENGTH_LONG).show();
            editTexlpersonchrgeRest.requestFocus();
            return false;
        }
        if (editTexlandlineRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "landline_empty", Toast.LENGTH_LONG).show();
            editTexlandlineRest.requestFocus();
            return false;
        }
        if (editTextAddressRest.getText().toString().equals("")) {
            Toast.makeText(Activity, "Address_empty", Toast.LENGTH_LONG).show();
            editTextAddressRest.requestFocus();
            return false;
        }

        return true;

    }

    private void getlocation() {
        lat = PreferenceUtil.getlat(Activity);
        lng = PreferenceUtil.getlng(Activity);

  /*      final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
               lat = PreferenceUtil.getlat(Activity);
               lng = PreferenceUtil.getlng(Activity);

               // Log.e("LOCCCCCCCCCCCCCCCCCCCC", lat+"  "+lng);
               // lat = ms.getLatitude();
               // lng = ms.getLongitude();
              //  Log.e("Location",""+lat+""+lng);
            }
        }, 2000);*/

    }
}
