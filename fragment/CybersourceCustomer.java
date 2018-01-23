package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;
import com.mawaqaa.eatandrun.activity.WebviewPupupActivity;
import com.mawaqaa.eatandrun.adapter.CuisineListAdapter;
import com.mawaqaa.eatandrun.data.CuisineListData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Admin on 6/4/2017.
 */

public class CybersourceCustomer extends EatndRunBaseFragment {

    String TAG = "CybersourceCustomer";
    Button btn_updatecard, btnremovecard, btn_addcard;
    String message, Success;
    String CreateProfileUri;
    String Decision;
    Spinner spinnerTexCards;
    ArrayList<CuisineListData> cuisineListDataArrayList;
    CuisineListData cuisineListData;
    CuisineListAdapter cuisineListAdapter;
    String spinitemCardId, CardId;
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

        View v = inflater.inflate(R.layout.cybersource_coustumer, container, false);

        spinnerTexCards = (Spinner) v.findViewById(R.id.spinnerTexCards);


        btn_updatecard = (Button) v.findViewById(R.id.btn_updatecard);
        btnremovecard = (Button) v.findViewById(R.id.btnremovecard);
        btn_addcard = (Button) v.findViewById(R.id.btn_addcard);

        getCardsList();
        btn_updatecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCard();
            }
        });
        btnremovecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveCard();
            }
        });
        btn_addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard();
            }
        });

        spinnerTexCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spinnerTexCards.getSelectedItemPosition();
                cuisineListData = cuisineListDataArrayList.get(pos);
                spinitemCardId = (cuisineListData.getCuisineId());
                Log.e("getCuisineId :::", "" + cuisineListData.getCuisineId());
                if (!spinitemCardId.equals("")) {
                    CardId = spinitemCardId;
                    PreferenceUtil.setCuisineID(getActivity(), CardId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    private void addCard() {
        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    createCustomerProfileAndCardDetailsStringReq(AppConstants.EatndRun_CREATECUSTOMERPROFILEANDCARDDETAILS, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();
                    xx.toString();
                }

            }
        }).start();

    }

    public void getCardsList() {

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    makeJsongetCardsListStringReq(AppConstants.EatndRun_RETRIEVECUSTOMERPROFILEANDCARDDETAILS, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();
                    xx.toString();
                }

            }
        }).start();

    }

    public void UpdateCard() {
        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerToken", CardId);
                    makeJsonUpdateCardStringReq(AppConstants.EatndRun_UPDATECUSTOMERPROFILEANDCARDDETAILS, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();
                    xx.toString();
                }

            }
        }).start();
    }

    public void RemoveCard() {
        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    makeJsonRemoveCardStringReq(AppConstants.EatndRun_DELETECUSTOMERPROFILEANDCARDDETAILS, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();
                    xx.toString();
                }

            }
        }).start();
    }

    private String createCustomerProfileAndCardDetailsStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    CreateProfileUri = jsonObj.getString("CreateProfileUri");
                                    PreferenceUtil.setProfileUri(
                                            getActivity(), CreateProfileUri);
                                }

                                startActivity(new Intent(getActivity(), WebviewPupupActivity
                                        .class));


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


    private String makeJsongetCardsListStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    JSONArray CuisineArr = jsonObj.getJSONArray("CustomerCardsList");
                                    cuisineListDataArrayList = new ArrayList<CuisineListData>();
//String cuisineId, String cuisineName, String cuisineDescription, String cuisineImage, String languageId, String sortOrder, boolean isActive, String menuItemCount
                                    cuisineListDataArrayList.add(new CuisineListData("0", getString(R.string.cybersource_cus_spinner_select_card), "", "", "", "", true, ""));

                                    for (int t = 0; t < CuisineArr.length(); t++) {
                                        JSONObject jsonObject1 = CuisineArr.getJSONObject(t);

                                        String CardAccountNumber = jsonObject1.getString("CardAccountNumber");
                                        String CardToken = jsonObject1.getString("CardToken");


                                        cuisineListData = new CuisineListData(CardToken, CardAccountNumber, "", "", "", "", true, "");
                                        cuisineListDataArrayList.add(cuisineListData);
                                        progressBar.dismiss();

                                    }
                                    progressBar.dismiss();

                                }
                                cuisineListAdapter = new CuisineListAdapter(getActivity(), cuisineListDataArrayList);

                                spinnerTexCards.setAdapter(cuisineListAdapter);
                                progressBar.dismiss();


                            } catch (Exception xx) {
                                progressBar.dismiss();
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
                            progressBar.dismiss();
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

    private String makeJsonUpdateCardStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    CreateProfileUri = jsonObj.getString("UpdateProfileUri");
                                    PreferenceUtil.setProfileUri(getActivity(), CreateProfileUri);
                                }
                                startActivity(new Intent(getActivity(), WebviewPupupActivity
                                        .class));

                            } catch (Exception xx) {
                                progressBar.dismiss();
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
        progressBar.dismiss();
        return resultConn[0];

    }

    private String makeJsonRemoveCardStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    Decision = jsonObj.getString("Decision");

                                    if (Decision.equals("ACCEPT")) {
                                        Toast.makeText(getActivity(),
                                                getString(R.string.cybersource_cus_remove_card_msg), Toast.LENGTH_LONG).show();

                                        spinnerTexCards.setAdapter(null);

                                        getCardsList();

                                    }else{ Toast.makeText(getActivity(),
                                            Decision, Toast.LENGTH_LONG).show();}

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
