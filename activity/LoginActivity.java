package com.mawaqaa.eatandrun.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.dialog.RegisterAsDialog;
import com.mawaqaa.eatandrun.dialog.UpdateEmail;
import com.mawaqaa.eatandrun.interfaces.OnUpdateEmailListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class LoginActivity extends EatndRunBaseActivity {

    String TAG = "LoginActivity";
    ImageView btn_login, btn_register;
    EditText edtxt_username, edtxt_password;
    TextView lblforgotpswd;
    String isLoginSuccess = "";
    CallbackManager callbackManager;
    String CreateProfileUri;
    CheckBox checkBox_Keepmesigned_in;
    private LoginButton loginButton;
    private String fbEmail, fbUserName, fbFirstName, fbLastName;
    private String TWUserId, TWUserName, HasRegisteredCard;
    private ProgressDialog progressBar;
    private TwitterLoginButton twitterLogin;
    private OnUpdateEmailListener onUpdateEmailListener;
    private EatndRunBaseActivity Activity;

    private Boolean saveLogin;


    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        //((EatndRunBaseActivity) getActivity()).BaseFragment = this;
        //((AjaratyMainActivity) getActivity()).hideLogo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);

        setContentView(R.layout.activity_login);



         Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) LoginActivity.this;

        loginButton = (LoginButton) findViewById(R.id.login_button);
        checkBox_Keepmesigned_in = (CheckBox) findViewById(R.id.checkBox_Keepmesigned_in);
        loginButton.setText("dddd");
        loginButton.setReadPermissions("email");
        // If using in a fragment
        //loginButton.setFragment(this);
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "FBLogin .");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                // Application code
                                Log.v("QWLoginActivity",
                                        response.toString());

                                fbEmail = object.optString("email");
                                //fbEmail ="";
                                fbUserName = object.optString("id");
                                fbFirstName = object
                                        .optString("first_name");
                                fbLastName = object
                                        .optString("last_name");
                                Log.e(TAG, "fbRes" + fbEmail + ""
                                        + fbUserName + "" + fbFirstName
                                        + "" + fbLastName);

                                if (fbEmail != "") {
                                    Log.e(TAG, "FBLogin .CALL servic");
                                    postFBUserData();
                                } else {
                                    // call dialog for entering email
                                    Log.e("EMAILNULL", "");
                                    UpdateEmail updem = UpdateEmail
                                            .newInstance(
                                                    onUpdateEmailListener,
                                                    1);
                                   /* updem.show(getFragmentManager(),
                                            TAG);*/
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters
                        .putString("fields",
                                "id,first_name,last_name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                LoginManager.getInstance().logOut();


               /* PreferenceUtil.setUserSignedIn(Activity,
                        true);
                Fragment HomeFrag = new HomeFrag();
                Activity.pushFragments(HomeFrag, false, true);

                Toast.makeText(Activity, "Facebook login Success", Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook login Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Facebook login Error", Toast.LENGTH_LONG).show();
            }
        });

        lblforgotpswd = (TextView) findViewById(R.id.lblforgotpswd);


        lblforgotpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(i);
            }
        });

        //initview(v);
        edtxt_username = (EditText) findViewById(R.id.edtxt_username);
        edtxt_password = (EditText) findViewById(R.id.edtxt_password);
        btn_login = (ImageView) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LoginAuthentication()) {

                    fetchlogindata();

                }


            }
        });

        btn_register = (ImageView) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterAsDialog rgdlg = new RegisterAsDialog();
                rgdlg.show(getSupportFragmentManager(), TAG);


            }
        });


        twitterLogin = (TwitterLoginButton) findViewById(R.id.login_button_tw);
        twitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                HandleTwitterlogin(session);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Twitter login Cancel", Toast.LENGTH_LONG).show();

            }
        });

        checkBox_Keepmesigned_in.setChecked(PreferenceUtil.getrememberMe(this));







    }

    public void HandleTwitterlogin(final TwitterSession session) {

        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.CUST_UserName, session.getUserName());

                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonPostTwitterDatatringReq(AppConstants.EatndRun_LOGINWITHTWITTER, jsonObject);

                } catch (Exception e) {
                    Log.e(TAG, "Execption while Putting Json");
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void fetchlogindata() {
        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt("Cus_Username", edtxt_username.getText().toString().trim());
                    jsonObject.putOpt("Cus_Password", edtxt_password.getText().toString().trim());
                    // jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonLoginStringReq(AppConstants.EatndRun_LOGIN, jsonObject);
                    // startSpinwheel(false, true);


                } catch (Exception e) {
                    progressBar.dismiss();
                    Log.e(TAG, "Exception while Putting Json");
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private String makeJsonLoginStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj != null) {
                                    isLoginSuccess = jsonObj.getString("Success");
                                    Log.e(TAG, "LoginSuccess >>" + isLoginSuccess);
                                    HasRegisteredCard = jsonObj.getString("HasRegisteredCard");
                                    PreferenceUtil.setUserDetails(LoginActivity.this, jsonObj);


                                    if (isLoginSuccess.equalsIgnoreCase("Success")) {

                                        //Log.e("RES", "" + jsonObject.getString(AppConstants.CUST_Email));

                                        Log.e(TAG, " getUserId 00 === " + PreferenceUtil.getUserId(LoginActivity.this));

                                        if (HasRegisteredCard.equals("true")) {

                                            PreferenceUtil.setUserSignedIn(LoginActivity.this, true);

                                            Log.e(TAG, " getUserId === " + PreferenceUtil.getUserId(LoginActivity.this));

                                            saveLogin   = checkBox_Keepmesigned_in.isChecked();

                                            PreferenceUtil.seRememberMe(LoginActivity.this ,saveLogin );

                                            Intent i = new Intent();
                                            // i.setClass(sPlashScreen, AjaratyMainActivity.class);
                                            i.setClass(getApplicationContext(), EatndRunMainActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            //stopSpinWheel();
                                            Toast.makeText(
                                                    LoginActivity.this
                                                    , getString(R.string.login_please_reg_card), Toast.LENGTH_LONG).show();

                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setMessage(getString(R.string.login_dialog_add_card_msg))
                                                    .setCancelable(false)
                                                    .setPositiveButton(getString(R.string.string_yes), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Log.e(TAG, " getUserId 22 === " + PreferenceUtil.getUserId(LoginActivity.this));
                                                            progressBar.dismiss();

                                                            CreateCustomerProfileAndCardDetails();
                                                            //LoginActivity.this.finish();
                                                        }
                                                    })
                                                    .setNegativeButton(getString(R.string.string_no), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            progressBar.dismiss();

                                                        }
                                                    })
                                                    .show();
                                            progressBar.dismiss();

                                        }

                                        progressBar.dismiss();
                                    } else {
                                        Toast.makeText(
                                                LoginActivity.this
                                                , getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                                        progressBar.dismiss();

                                    }
                                    progressBar.dismiss();

                                } else {
                                    Toast.makeText(
                                            LoginActivity.this
                                            , "Something went wrong! please try again later.", Toast.LENGTH_LONG).show();
                                    progressBar.dismiss();

                                }
                                progressBar.dismiss();

                            } catch (Exception xx) {
                                progressBar.dismiss();
                                Log.e(TAG, "   " + xx.toString());
                                xx.toString();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    progressBar.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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
        progressBar.dismiss();
        return resultConn[0];

    }


    public boolean LoginAuthentication() {

        if (edtxt_username.getText().toString().equals("")) {
            Toast.makeText(
                    LoginActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_rest_error_username_empty), Toast.LENGTH_LONG).show();
            edtxt_username.requestFocus();
            return false;
        }
        if (edtxt_password.getText().toString().equals("")) {
            Toast.makeText(
                    LoginActivity.this, getString(R.string.empty_fields_error_msg) + ": " + getString(R.string.register_cus_error_pass_empty), Toast.LENGTH_LONG).show();
            edtxt_password.requestFocus();
            return false;
        }


        return true;
    }

    private void postFBUserData() {

        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.putOpt(AppConstants.CUST_UserName, fbUserName);
                    jsonObject.putOpt(AppConstants.CUST_FirstName, fbFirstName);
                    jsonObject.putOpt(AppConstants.CUST_LastName, fbLastName);
                    jsonObject.putOpt(AppConstants.CUST_Email, fbEmail);
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

                    makeJsonPostFBDatatringReq(AppConstants.EatndRun_LOGINWITHFACEBOOK, jsonObject);

                } catch (Exception e) {
                    Log.e(TAG, "Execption while Putting Json");
                    e.printStackTrace();
                }

            }
        }).start();


    }

    private String makeJsonPostTwitterDatatringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObjResp = new JSONObject(response);

                                if (jsonObjResp.getString(AppConstants.IsSucces).equalsIgnoreCase("Success")) {
                                    HasRegisteredCard = jsonObjResp.getString("HasRegisteredCard");

                                    if (HasRegisteredCard.equals("true")) {

                                        PreferenceUtil.setUserSignedIn(LoginActivity.this, true);

                                        Log.e(TAG, " getUserId === " + PreferenceUtil.getUserId(LoginActivity.this));


                                        Intent i = new Intent();
                                        // i.setClass(sPlashScreen, AjaratyMainActivity.class);
                                        i.setClass(getApplicationContext(), EatndRunMainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        //stopSpinWheel();
                                        Toast.makeText(
                                                LoginActivity.this
                                                , getString(R.string.login_please_reg_card), Toast.LENGTH_LONG).show();

                                        new AlertDialog.Builder(LoginActivity.this)
                                                .setMessage(getString(R.string.login_dialog_add_card_msg))
                                                .setCancelable(false)
                                                .setPositiveButton(getString(R.string.string_yes), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Log.e(TAG, " getUserId 22 === " + PreferenceUtil.getUserId(LoginActivity.this));
                                                        progressBar.dismiss();

                                                        CreateCustomerProfileAndCardDetails();
                                                        //LoginActivity.this.finish();
                                                    }
                                                })
                                                .setNegativeButton(getString(R.string.string_no), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        progressBar.dismiss();

                                                    }
                                                })
                                                .show();
                                        progressBar.dismiss();

                                    }
                                } else {

                                    Toast.makeText(LoginActivity.this, jsonObjResp.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this
                                    , error.toString(), Toast.LENGTH_LONG).show();

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


    private String makeJsonPostFBDatatringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";


        try {
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

            final String finalString_json = string_json;

            stringRequest = new StringRequest(Request.Method.POST, urlPost,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObjResp = new JSONObject(response);

                                if (jsonObjResp.getString(AppConstants.IsSucces).equalsIgnoreCase("Success")) {
                                    Toast.makeText(LoginActivity.this, jsonObjResp.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();
                                    Log.e("FBOPT", "" + jsonObjResp.getInt(AppConstants.CUST_ID) + "" + jsonObjResp.getString(AppConstants.CUST_Email));
                                    PreferenceUtil.setUserSignedIn(LoginActivity.this,
                                            true);
                                    PreferenceUtil.setUserDetails(LoginActivity.this, jsonObjResp);
                                    // Activity.clearAllBackStackEntries();
                                    /*HomeFrag HomeFrag = new HomeFrag();
                                    EatndRunBaseActivity.getExpoBaseActivity().pushFragments(HomeFrag, false, true);*/

                                    finish();
                                    Intent i = new Intent();
                                    // i.setClass(sPlashScreen, AjaratyMainActivity.class);
                                    i.setClass(getApplicationContext(), EatndRunMainActivity.class);
                                    startActivity(i);
                                } else {

                                    Toast.makeText(LoginActivity.this, jsonObjResp.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this
                                    , error.toString(), Toast.LENGTH_LONG).show();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        twitterLogin.onActivityResult(requestCode, resultCode, data);

    }

    private void CreateCustomerProfileAndCardDetails() {
        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(Activity));


                    makeJsonCreateCustomerProfileAndCardDetailsStringReq(
                            AppConstants.EatndRun_CREATECUSTOMERPROFILEANDCARDDETAILS, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();
    }

    private String makeJsonCreateCustomerProfileAndCardDetailsStringReq(String urlPost, final JSONObject jsonObject) {
        StringRequest stringRequest = null;
        final String[] resultConn = {""};
        String string_json = "";

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

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
                                            LoginActivity.this, CreateProfileUri);
                                }

                                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CreateProfileUri)));

                                startActivity(new Intent(LoginActivity.this, WebviewPupupActivity
                                        .class));


                                progressBar.dismiss();
                                //  finish();

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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
