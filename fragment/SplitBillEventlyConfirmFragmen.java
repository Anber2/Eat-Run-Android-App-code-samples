package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by HP on 7/17/2017.
 */

public class SplitBillEventlyConfirmFragmen extends EatndRunBaseFragment {
    public static final String TAG = "FragmenSplitBillEvently";
    Button split_evenky_confirm_payBill_btn;

    TextView txt_split_evenky_confirm_tableNo, txt_split_evenky_confirm_time, txt_split_evenky_confirm_your_bill_value, txt_split_evenky_confirm_tip_value, txt_split_evenky_confirm_total_value;

    String message, Success;
    String CreateProfileUri;
    private ProgressDialog progressBar;

   /* public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();
    }


    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;
       *//* new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getActivity()));

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonGetCurrentOrderStatusByNumberStringReq(AppConstants.EatndRun_GETCURRENTORDERSTATUSBYNUMBER, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();*//*
    }
    private String makeJsonGetCurrentOrderStatusByNumberStringReq(String urlPost, final JSONObject jsonObject) {
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
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {
                                    Success = jsonObj.getString("Success");
                                }

                                if (!Success.equals("Failed")) {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                    alert.setTitle("Transaction Status!");
                                    alert.setMessage("Transaction Successfully Completed!");
                                    alert.setPositiveButton("OK", null);
                                    alert.show();
                                    Activity.pushFragments(new RestaurantListFragment(), false, true);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.confirm_pill_split_evenly, container, false);
        initView(v);

        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                    jsonObject.putOpt("OrderId", PreferenceUtil.getOrderID(getActivity()));
                    jsonObject.putOpt("Tip", PreferenceUtil.getTip(getActivity()));


                    makeJsonGetEvenlyCustomerBillSummaryStringReq(AppConstants.EatndRun_GETEVENLYCUSTOMERBILLSUMMARY, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

        return v;
    }


    private String makeJsonGetEvenlyCustomerBillSummaryStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    String Amount = jsonObj.getString("Amount");
                                    String TotalAmount = jsonObj.getString("TotalAmount");
                                    String Time = jsonObj.getString("Time");
                                    String TableNo = jsonObj.getString("TableNo");
                                    String Tip = jsonObj.getString("Tip");

                                    txt_split_evenky_confirm_tableNo.setText("Table " + TableNo);
                                    txt_split_evenky_confirm_time.setText(Time);
                                    txt_split_evenky_confirm_your_bill_value.setText(Amount);
                                    txt_split_evenky_confirm_tip_value.setText(Tip);
                                    txt_split_evenky_confirm_total_value.setText(TotalAmount);


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


    private void initView(View v) {

        txt_split_evenky_confirm_tableNo = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_tableNo);
        txt_split_evenky_confirm_time = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_time);
        txt_split_evenky_confirm_your_bill_value = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_your_bill_value);
        txt_split_evenky_confirm_tip_value = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_tip_value);
        txt_split_evenky_confirm_total_value = (TextView) v.findViewById(R.id.txt_split_evenky_confirm_total_value);


        split_evenky_confirm_payBill_btn = (Button) v.findViewById(R.id.split_evenky_confirm_payBill_btn);

        split_evenky_confirm_payBill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getActivity()));
                            jsonObject.putOpt("SubscriptionID", "");
                            jsonObject.putOpt("orderRequestToken", "");
                            jsonObject.putOpt("Currency", "");
                            jsonObject.putOpt("amount", "");


                            makeJsonPayCustomerOrderStringReq(AppConstants.EatndRun_PAYCUSTOMERORDER, jsonObject);

                        } catch (Exception xx) {
                            xx.toString();
                        }

                    }
                }).start();
            }
        });


    }

    private String makeJsonPayCustomerOrderStringReq(String urlPost, final JSONObject jsonObject) {
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


                                    CreateProfileUri = jsonObj.getString("UpdateProfileUri");
                                    PreferenceUtil.setProfileUri(getActivity(), CreateProfileUri);

                                }

                                Log.e("CreateProfileUri == ", CreateProfileUri + "&order_id=" + PreferenceUtil.getOrderID(getActivity()) + "&amount=" + PreferenceUtil.getAmount(getActivity()) + "&currency=USD");

                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CreateProfileUri + "&order_id=" + PreferenceUtil.getOrderID(getActivity()) + "&amount=" + PreferenceUtil.getAmount(getActivity()) + "&currency=USD")));


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

*/
}
