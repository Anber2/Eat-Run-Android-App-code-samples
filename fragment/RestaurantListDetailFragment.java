package com.mawaqaa.eatandrun.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.mawaqaa.eatandrun.activity.SocialMediaPupupActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantListDetailFragment extends EatndRunBaseFragment {

    String TAG = "ResListDetailFrg";
    Dialog rankDialog;
    ImageView imgvwcall, imgv_socialmedia, map_imgv, imgv_cuisine, imgv_whours, imgv_email, reslstdet_btnmenu, imgvoffers, imageView_rest_details;
    TextView tv_Youcan, lblRestDkmaway, lblRestD, lblRestDAddress, lblRestDCurrentlyStatus, textView_total_visitors;
    Button btn_RestDetaOpenBill, btn_RestDetaJoinBill, btn_RestDetaTip;

    LinearLayout linearLayout_rate;
    String RestaurantName, Res_Telephone, Res_Address, Res_Latitude, Res_Longitude, Distance, RestaurantLogo, CityName, Res_WorkingStatus, Res_Instagram, Res_Facebook, Res_Twitter, Res_OpenTime, Res_CloseTime, Message, Res_Email, CuisineName, CuisineOtherName;
    RelativeLayout imageRelativeLayout;

    String message, Success, resRate;
    RatingBar res_list_rating2, dialog_ratingbar;

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
        //View v = inflater.inflate(R.layout.fragment_registerorlogin, container, false);
        View v = inflater.inflate(R.layout.resturantlistdetails, container, false);
        initview(v);


        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("Cus_Id", PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt(AppConstants.EatndRun_RES_ID, PreferenceUtil.getResID(Activity));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(Activity));
                    jsonObject.putOpt(AppConstants.EatndRun_CUS_LATITUDE, PreferenceUtil.getlat(Activity));
                    jsonObject.putOpt(AppConstants.EatndRun_CUS_LONGITUDE, PreferenceUtil.getlng(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonRestaurantListDetailStringReq(AppConstants.EatndRun_GETRESTAURANTDETAILS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

        getCusTip();


        return v;
    }

    private String makeJsonRestaurantListDetailStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    RestaurantName = jsonObj.getString("RestaurantName");
                                    Res_Telephone = jsonObj.getString("Res_Telephone");
                                    Res_Address = jsonObj.getString("Res_Address");
                                    AppConstants.Res_Latitude_ = jsonObj.getString("Res_Latitude");
                                    AppConstants.Res_Longitude_ = jsonObj.getString("Res_Longitude");
                                    Distance = jsonObj.getString("Distance");
                                    RestaurantLogo = jsonObj.getString("RestaurantLogo");
                                    CityName = jsonObj.getString("CityName");
                                    Res_WorkingStatus = jsonObj.getString("Res_WorkingStatus");
                                    Res_Instagram = jsonObj.getString("Res_Instagram");
                                    Res_Facebook = jsonObj.getString("Res_Facebook");
                                    Res_Twitter = jsonObj.getString("Res_Twitter");
                                    Res_OpenTime = jsonObj.getString("Res_OpenTime");
                                    Res_CloseTime = jsonObj.getString("Res_CloseTime");
                                    Message = jsonObj.getString("Message");
                                    Res_Email = jsonObj.getString("Res_Email");
                                    String RatingStarCount = jsonObj.getString("RatingStarCount");
                                    String TotalVisitors = jsonObj.getString("TotalVisitors");

                                    CuisineOtherName = jsonObj.getString("CuisineOtherName");
                                    CuisineName = jsonObj.getString("CuisineName");

                                    Picasso.with(getActivity()).load(AppConstants.EatndRun_BASE_IMAGES_URL +
                                            RestaurantLogo).fit().placeholder(R.drawable.listdetail_banner).into(imageView_rest_details);


                                    res_list_rating2.setRating(Float.parseFloat(RatingStarCount));

                                    textView_total_visitors.setText(TotalVisitors);
                                    lblRestD.setText(RestaurantName);
                                    lblRestDAddress.setText(Res_Address);
                                    lblRestDkmaway.setText(Distance);
                                    lblRestDCurrentlyStatus.setText(Res_WorkingStatus);

                                    if (Res_WorkingStatus.equals("Open")) {
                                        lblRestDCurrentlyStatus.setTextColor(Color.parseColor("#008000"));
                                    } else {
                                        lblRestDCurrentlyStatus.setTextColor(Color.parseColor("#ff0000"));

                                        btn_RestDetaOpenBill.setEnabled(false);
                                        btn_RestDetaJoinBill.setEnabled(false);
                                    }

                                    PreferenceUtil.setResFacebook(getActivity(), Res_Facebook);
                                    PreferenceUtil.setResTwitter(getActivity(), Res_Twitter);
                                    PreferenceUtil.setResInstagram(getActivity(), Res_Instagram);
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


    private void initview(View v) {

        imgvwcall = (ImageView) v.findViewById(R.id.imgvwcall);
        imageRelativeLayout = (RelativeLayout) v.findViewById(R.id.image_rest_layout);
        imgv_socialmedia = (ImageView) v.findViewById(R.id.imgv_socialmedia);
        imgv_email = (ImageView) v.findViewById(R.id.imgv_email);
        res_list_rating2 = (RatingBar) v.findViewById(R.id.res_list_rating2);
        imageView_rest_details = (ImageView) v.findViewById(R.id.imageView_rest_details);
        map_imgv = (ImageView) v.findViewById(R.id.map_imgv);
        imgv_cuisine = (ImageView) v.findViewById(R.id.imgv_cuisine);
        imgv_whours = (ImageView) v.findViewById(R.id.imgv_whours);
        reslstdet_btnmenu = (ImageView) v.findViewById(R.id.reslstdet_btnmenu);
        imgvoffers = (ImageView) v.findViewById(R.id.imgvoffers);

        btn_RestDetaOpenBill = (Button) v.findViewById(R.id.btn_RestDetaOpenBill);
        btn_RestDetaTip = (Button) v.findViewById(R.id.btn_RestDetaTip);
        btn_RestDetaJoinBill = (Button) v.findViewById(R.id.btn_RestDetaJoinBill);

        lblRestDkmaway = (TextView) v.findViewById(R.id.lblRestDkmaway);
        lblRestD = (TextView) v.findViewById(R.id.lblRestD);
        lblRestDAddress = (TextView) v.findViewById(R.id.lblRestDAddress);
        lblRestDCurrentlyStatus = (TextView) v.findViewById(R.id.lblRestDCurrentlyStatus);
        tv_Youcan = (TextView) v.findViewById(R.id.tv_Youcan);
        textView_total_visitors = (TextView) v.findViewById(R.id.textView_total_visitors);


        linearLayout_rate = (LinearLayout) v.findViewById(R.id.linearLayout_rate);
        linearLayout_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog = new Dialog(getActivity(), R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                dialog_ratingbar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);

                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);

                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);

                resRate = Float.toString(dialog_ratingbar.getRating());

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    JSONObject jsonObject = new JSONObject();

                                    jsonObject.putOpt("Cus_Id", PreferenceUtil.getUserId(Activity));
                                    jsonObject.putOpt("Res_Id", PreferenceUtil.getResID(Activity));
                                    jsonObject.putOpt("Rating", Math.round(dialog_ratingbar.getRating()));
                                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                                    makeJsonUpdteResRateStringReq(AppConstants.EatndRun_SETRESTAURANTRATING, jsonObject);

                                } catch (Exception xx) {
                                    xx.toString();
                                }

                            }
                        }).start();


                        rankDialog.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();

            }
        });

        imgvoffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment RestMapFrag = new RestaurantOfferItemListFragment();
                Activity.pushFragments(RestMapFrag, false, true);


            }
        });

        imgv_whours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getString(R.string.restaurantlistdetailfragment_working_hours_txt));
                alert.setMessage(getString(R.string.restaurantlistdetailfragment_open_time_txt) + Res_OpenTime + "\n" + getString(R.string.restaurantlistdetailfragment_close_time_txt) + Res_CloseTime);
                alert.setPositiveButton(getString(R.string.string_ok), null);
                alert.show();
            }
        });


        map_imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment RestMapFrag = new RestaurntMapPupupFragment();
                Activity.pushFragments(RestMapFrag, false, true);

            }
        });

        imgv_cuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CuisineName == "null") {

                    Toast.makeText(Activity, getString(R.string.restaurantlistdetailfragment_cuisine_rest_null),
                            Toast.LENGTH_SHORT).show();
                } else {
                    /*Toast.makeText(Activity,
                            CuisineName + " " + getString(R.string.restaurantlistdetailfragment_cuisine_rest_txt),
                            Toast.LENGTH_SHORT).show();*/

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(getString(R.string.restaurantlistdetailfragment_cuisine_title));
                    alert.setMessage(getString(R.string.restaurantlistdetailfragment_cuisine_mesg) + " " + CuisineName + " " + getString(R.string.restaurantlistdetailfragment_cuisine_rest_txt));
                    alert.setPositiveButton(getString(R.string.string_ok), null);
                    alert.show();

                }

            }

        });

        imgv_socialmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Res_Facebook == "null" && Res_Instagram == "null" && Res_Twitter == "null") {

                    Toast.makeText(Activity, getString(R.string.restaurantlistdetailfragment_socialmedia),
                            Toast.LENGTH_SHORT).show();

                } else {
                    startActivity(new Intent(Activity, SocialMediaPupupActivity.class));

                }
            }
        });


        imgv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Res_Email == "null") {

                    Toast.makeText(Activity, getString(R.string.restaurantlistdetailfragment_email_rest_null),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{Res_Email});

                    try {
                        startActivity(Intent.createChooser(i, getString(R.string.restaurantlistdetailfragment_send_email_txt)
                        ));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }}


            }
        });


        imgvwcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Res_Telephone == "null") {

                    Toast.makeText(Activity, getString(R.string.restaurantlistdetailfragment_email_rest_null),
                            Toast.LENGTH_SHORT).show();
                } else { try {
                    String number = Res_Telephone;
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + number));
                    startActivity(phoneIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Activity,
                            getString(R.string.restaurantlistdetailfragment_call_error_msg_txt), Toast.LENGTH_SHORT).show();
                }}


            }
        });


        reslstdet_btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment RestMenuFrag = new RestaurantMenuFragment();
                Activity.pushFragments(RestMenuFrag, false, true);
            }
        });


        String text = "<font color='#FF0000'>" + getString(R.string.restaurantlistdetailfragment_bottom_txt) + "\n"  +"</font> <font color='#0000FF'>'" + getString(R.string.joinbillofjoinbillsplitevently_joinbill) + "'</font>.";


        tv_Youcan.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);


        btn_RestDetaJoinBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentJoinBill joinbill = new FragmentJoinBill();
                JoinBillOfJoinBillSplitEvently fragmentJoinBillSplit = new JoinBillOfJoinBillSplitEvently();

                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(fragmentJoinBillSplit, false, true);
            }
        });

        btn_RestDetaTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(Activity,
                        "No Data!", Toast.LENGTH_SHORT).show();*/
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getString(R.string.restaurantlistdetailfragment_enter_tip_dialog) + ":");
                final EditText input = new EditText(getActivity());
                input.setHint(getString(R.string.restaurantlistdetailfragment_example_txt_dialog) + PreferenceUtil.getTip(getActivity()));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setPositiveButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        PreferenceUtil.setTip(getActivity(), input.getText().toString());
                        saveCusTip();

                    }
                });
                alert.setNegativeButton(getString(R.string.string_cancel), null);
                alert.show();
            }
        });

        btn_RestDetaOpenBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity.pushFragments(new FragmentOpenBill(), false, true);


            }
        });


    }


    private String makeJsonUpdteResRateStringReq(String urlPost, final JSONObject jsonObject) {
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

                                }
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();


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

    private void saveCusTip() {

        //progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt("Cus_Tip", PreferenceUtil.getTip(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJson_saveCusTip_StringReq(AppConstants.EatndRun_SetCustomerTip, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

    }

    private String makeJson_saveCusTip_StringReq(String urlPost, final JSONObject jsonObject) {
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


                                if (response != null) {


                                    if (response.equalsIgnoreCase("\"1\"")) {


                                        Toast.makeText(getActivity(), "Tip Saved!", Toast.LENGTH_LONG).show();

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


    private void getCusTip() {

        //progressBar = ProgressDialog.show(getActivity(), "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(Activity));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJson_getCusTip_StringReq(AppConstants.EatndRun_GetCustomerTip, jsonObject);

                } catch (Exception xx) {
                    progressBar.dismiss();
                    xx.toString();
                }
                progressBar.dismiss();

            }
        }).start();


    }

    private String makeJson_getCusTip_StringReq(String urlPost, final JSONObject jsonObject) {
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


                                    PreferenceUtil.setTip(getActivity(), jsonObj.getString("CustomerTip"));


                                }


                            } catch (Exception xx) {
                                Log.e(TAG, "   " + xx.toString());
                                xx.toString();
                                progressBar.dismiss();

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
            progressBar.dismiss();


        } catch (Exception e) {
            progressBar.dismiss();
            e.toString();
            return e.toString();
        }

        return resultConn[0];

    }


}