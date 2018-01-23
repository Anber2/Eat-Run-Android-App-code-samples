package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;

/**
 * Created by HP on 11/27/2017.
 */

public class JoinBillOfJoinBillSplitChooseItem extends EatndRunBaseFragment {
    String TAG = "JoinBillSplitChooseItem";

    Button btn_joinbill_choositem_addopenbillno;
    EditText edt_joinbill_choositem_addopenbillno;

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
        View v = inflater.inflate(R.layout.join_bill_split_choositem_add_openbill_no, container, false);

        btn_joinbill_choositem_addopenbillno = (Button) v.findViewById(R.id.btn_joinbill_choositem_addopenbillno);
        edt_joinbill_choositem_addopenbillno = (EditText) v.findViewById(R.id.edt_joinbill_choositem_addopenbillno);

        btn_joinbill_choositem_addopenbillno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SplitChooseItemAddPillNoAuthentication()) {
                    PreferenceUtil.setOpenBillNumber(getActivity(), edt_joinbill_choositem_addopenbillno.getText().toString());
                    Activity.pushFragments(new FragmentJoinBillChooseItemToSplit(), false, true);

                }
            }
        });

        return v;
    }

    public void getJoinToBillAPI() {


    }

/*
    private String makeJsonJoinToBillStringReq(String urlPost, final JSONObject jsonObject) {
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

                                // JSONObject jsonObj = new JSONObject(response);


                                if (response.equals("true"))
                                    Activity.pushFragments(new FragmenPayBillConfirm(), false, true);


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
*/


    public boolean SplitChooseItemAddPillNoAuthentication() {

        if (edt_joinbill_choositem_addopenbillno.getText().toString().equals("")) {
            Toast.makeText(Activity, getString(R.string.joinbillofjoinbillsplitevently_empty_msg), Toast.LENGTH_LONG).show();
            edt_joinbill_choositem_addopenbillno.requestFocus();
            return false;
        }
        if (edt_joinbill_choositem_addopenbillno.getText().length() < 5) {
            Toast.makeText(Activity,  getString(R.string.joinbillofjoinbillsplitevently_empty_msg2) , Toast.LENGTH_LONG).show();
            edt_joinbill_choositem_addopenbillno.requestFocus();
            return false;
        }
//editText.getText().length()

        return true;
    }
}
