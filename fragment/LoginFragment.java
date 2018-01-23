package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;
import com.mawaqaa.eatandrun.dialog.RegisterAsDialog;
import com.mawaqaa.eatandrun.dialog.UpdateEmail;
import com.mawaqaa.eatandrun.interfaces.OnUpdateEmailListener;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by HP on 11/27/2017.
 */
public class LoginFragment extends EatndRunBaseFragment {

    String TAG = "LoginFragment";
    ImageView btn_login, btn_register;
    EditText edtxt_username, edtxt_password;
    TextView lblforgotpswd;
    String isLoginSuccess = "";
    CallbackManager callbackManager;
    private LoginButton loginButton;
    private String fbEmail, fbUserName, fbFirstName, fbLastName;
    private String TWUserId, TWUserName;
    private ProgressDialog progressBar;
    private TwitterLoginButton twitterLogin;
    private OnUpdateEmailListener onUpdateEmailListener;

    private DrawerLayout drawerLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();

        TwitterConfig config = new TwitterConfig.Builder(getContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_CONSUMER_KEY), getString(R.string.twitter_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
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

        View v = inflater.inflate(R.layout.loginfragment, container, false);
        loginButton = (LoginButton) v.findViewById(R.id.login_button);

        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
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
                                    updem.show(getFragmentManager(),
                                            TAG);
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
                Toast.makeText(Activity, "Facebook login Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(Activity, "Facebook login Error", Toast.LENGTH_LONG).show();
            }
        });

        /*twitterLogin = (TwitterLoginButton) v.findViewById(R.id.twitter_login_button);

        twitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                Log.d("twitterUserName", result.data.getUserName());
                final String TwitterName = result.data.getUserName();
                String token = authToken.token;
                String secret = authToken.secret;
                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {

                        String TwitterEmail = result.data;
                        Log.d("TwitterData", "user Name : " + TwitterName + " email : " + TwitterEmail);
                        // Do something with the result, which provides the email address
                        postTWUserData();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                    }
                });

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });*/


        lblforgotpswd = (TextView) v.findViewById(R.id.lblforgotpswd);


        lblforgotpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Activity.pushFragments(new ForgetPassFragment(), false, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "=====" + e);
                }

            }
        });

        //initview(v);
        edtxt_username = (EditText) v.findViewById(R.id.edtxt_username);
        edtxt_password = (EditText) v.findViewById(R.id.edtxt_password);
        btn_login = (ImageView) v.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LoginAuthentication()) {

                    fetchlogindata();

                }


            }
        });

        btn_register = (ImageView) v.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterAsDialog rgdlg = new RegisterAsDialog();
                rgdlg.show(getFragmentManager(), TAG);

            }
        });


        // Email Update listener Interface
        /*onUpdateEmailListener = new OnUpdateEmailListener() {

            @Override
            public void onEmailUpdate(String email, int FBorTWTorINs) {
               *//* if (FBorTWT) {
                    fbEmail = email;
                    postFBUserData();
                } else {
                    twitter_email = email;
                    postTwitterUserData();
                }*//*
                switch (FBorTWTorINs) {
                    case 1:
                        fbEmail = email;
                        postFBUserData();
                        break;
                    case 2:
                        fbEmail = email;
                        postTWUserData();
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        };*/

//,,,,,,,,,,,,,,,,,,,,,,,,,FOR TWITTER ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,//


        //loginButton = (TwitterLoginButton) v.findViewById(R.id.twitter_login_button);
       /* loginButton.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(Activity, msg, Toast.LENGTH_LONG).show();
                TWUserName = session.getUserName();
                TWUserId = String.valueOf(session.getUserId());
                Log.d("TwitterKit", "" + msg);
                UpdateEmail updem = UpdateEmail
                        .newInstance(
                                onUpdateEmailListener,
                                2);
                updem.show(getFragmentManager(),
                        TAG);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });*/


//,,,,,,,,,,,,,,,,,,,,,,,,,FOR TWITTER ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,//

        return v;
    }

    public boolean LoginAuthentication() {

        if (edtxt_username.getText().toString().equals("")) {
            Toast.makeText(Activity, "username_empty", Toast.LENGTH_LONG).show();
            edtxt_username.requestFocus();
            return false;
        }
        if (edtxt_password.getText().toString().equals("")) {
            Toast.makeText(Activity, "password_empty", Toast.LENGTH_LONG).show();
            edtxt_password.requestFocus();
            return false;
        }


        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void fetchlogindata() {
        progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.putOpt("Cus_Username", edtxt_username.getText().toString().trim());
            jsonObject.putOpt("Cus_Password", edtxt_password.getText().toString().trim());
            // jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);

            makeJsonLoginStringReq(AppConstants.EatndRun_LOGIN, jsonObject);
            // startSpinwheel(false, true);


        } catch (Exception e) {
            Log.e(TAG, "Execption while Putting Json");
            e.printStackTrace();
        }
    }


    private String makeJsonLoginStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    isLoginSuccess = jsonObj.getString(AppConstants.IsSucces);
                                    Log.e(TAG, "LoginSuccess >>" + isLoginSuccess);

                                    if (isLoginSuccess.equals(AppConstants.IsSucces)) {

                                        //Log.e("RES", "" + jsonObject.getString(AppConstants.CUST_Email));
                                        PreferenceUtil.setUserSignedIn(Activity,
                                                true);
                                        PreferenceUtil.setUserDetails(Activity, jsonObj);
                                        Activity.clearAllBackStackEntries();

                                        Fragment HomeFrag = new HomeFrag();
                                        Activity.pushFragments(HomeFrag, false, true);
                                    } else {

                                        stopSpinWheel();
                                        Toast.makeText(Activity, "login failed", Toast.LENGTH_LONG).show();
                                    }
                                }

                            } catch (Exception xx) {
                                Log.e(TAG, "****" + xx.toString());
                                xx.toString();
                                stopSpinWheel();

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
            e.toString();
            progressBar.dismiss();

            return e.toString();
        }

        return resultConn[0];


    }

    @Override
    public void onLoginDataLoadedFailed(JSONObject jsonObject) {
        super.onLoginDataLoadedFailed(jsonObject);
        stopSpinWheel();
    }

    ////////////////////////////FB//////////////////////////////////////////////////////
    private void postFBUserData() {

        progressBar = ProgressDialog.show(getActivity(), "", "Please Wait ...", true, false);


        JSONObject jsonObject = new JSONObject();
        try {

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

    private String makeJsonPostFBDatatringReq(String urlPost, final JSONObject jsonObject) {
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

                                if (jsonObjResp.getString(AppConstants.IsSucces).equalsIgnoreCase("Success")) {
                                    Toast.makeText(Activity, jsonObjResp.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();
                                    Log.e("FBOPT", "" + jsonObjResp.getInt(AppConstants.CUST_ID) + "" + jsonObjResp.getString(AppConstants.CUST_Email));
                                    PreferenceUtil.setUserSignedIn(Activity,
                                            true);
                                    PreferenceUtil.setUserDetails(Activity, jsonObjResp);
                                    Activity.clearAllBackStackEntries();
                                    Fragment HomeFrag = new HomeFrag();
                                    Activity.pushFragments(HomeFrag, false, true);
                                } else

                                    Toast.makeText(Activity, jsonObjResp.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();


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


    @Override
    public void onFbLoginDataLoadedSuccessfully(JSONObject jsonObject) {
        super.onFbLoginDataLoadedSuccessfully(jsonObject);
        stopSpinWheel();
        //parseFblogindata(jsonObject);
    }


    @Override
    public void onFbLoginDataLoadedFailed(JSONObject jsonObject) {
        super.onFbLoginDataLoadedFailed(jsonObject);
        stopSpinWheel();
    }
    ////////////////////////////FB//////////////////////////////////////////////////////


    ////////////////////////////TWITTER//////////////////////////////////////////////////////
    private void postTWUserData() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.putOpt(AppConstants.CUST_UserName, TWUserId);
            jsonObject.putOpt(AppConstants.CUST_FirstName, TWUserName);
            jsonObject.putOpt(AppConstants.CUST_LastName, "");
            jsonObject.putOpt(AppConstants.CUST_Email, fbEmail);
            jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);


        } catch (Exception e) {
            Log.e(TAG, "Execption while Putting Json");
            e.printStackTrace();
        }

    }

    @Override
    public void onTwLoginDataLoadedSuccessfully(JSONObject jsonObject) {
        super.onTwLoginDataLoadedSuccessfully(jsonObject);
        stopSpinWheel();
        parseTwlogindata(jsonObject);
    }

    private void parseTwlogindata(JSONObject jsonObject) {

        try {
            if (jsonObject.getString(AppConstants.IsSucces).equalsIgnoreCase("Success")) {
                Toast.makeText(Activity, jsonObject.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();
                Log.e("TWITEROPT", "" + jsonObject.getInt(AppConstants.CUST_ID) + "" + jsonObject.getString(AppConstants.CUST_Email));
                PreferenceUtil.setUserSignedIn(Activity,
                        true);
                PreferenceUtil.setUserDetails(Activity, jsonObject);
                Activity.clearAllBackStackEntries();
                Fragment HomeFrag = new HomeFrag();
                Activity.pushFragments(HomeFrag, false, true);
            } else

                Toast.makeText(Activity, jsonObject.getString(AppConstants.MESSAGE), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTwLoginDataLoadedFailed(JSONObject jsonObject) {
        super.onTwLoginDataLoadedFailed(jsonObject);
        stopSpinWheel();
    }
    ////////////////////////////TWITTER//////////////////////////////////////////////////////


}