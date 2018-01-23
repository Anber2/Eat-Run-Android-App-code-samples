package com.mawaqaa.eatandrun.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.fragment.EatndRunBaseFragment;
import com.mawaqaa.eatandrun.volley.EatndRunResponse;
import com.mawaqaa.eatandrun.volley.VolleyUtils;

/**
 * Created by HP on 11/27/2017.
 */
public class EatndRunBaseActivity extends AppCompatActivity {
    protected static EatndRunBaseActivity BaseActivity;
    public EatndRunBaseFragment BaseFragment;
    private static final String TAG = "ExpoBaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowUtils.setFullScreenWithAction(this);
        BaseActivity = this;
         VolleyUtils.init(this);

        String t = this.getClass().getSimpleName();

      //  Toast.makeText(getApplicationContext(), t + "   333", Toast.LENGTH_SHORT).show();
    }

    public static EatndRunBaseActivity getExpoBaseActivity() {

        return BaseActivity;
    }

    public void serviceResponseSuccess(EatndRunResponse Response) {
        if (Response != null) {
            String reqUrl = Response.mReqUrl;
            Log.d(TAG, "serviceResponseSuccess" + reqUrl);
            switch (reqUrl) {
        case AppConstants.EatndRun_LOGIN:
                    BaseFragment.onLoginDataLoadedSuccessfully(Response.jsonObject);
                    break;
                /*case AppConstants.EatndRun_FBLOGIN:
                    BaseFragment.onFbLoginDataLoadedSuccessfully(Response.jsonObject);
                    break;
                case AppConstants.EatndRun_TWLOGIN:
                    BaseFragment.onTwLoginDataLoadedSuccessfully(Response.jsonObject);
                    break;*/
                case AppConstants.EatndRun_HomeContents:
                    BaseFragment.onHomecontentDataLoadedSuccessfully(Response.jsonObject);
                    break;
                case AppConstants.EatndRun_GETCOUNTRY:
                    BaseFragment.onCountryListLoadedSuccessfully(Response.jsonObject);
                    break;
                case AppConstants.EatndRun_GETAREA:
                    BaseFragment.onAreaListLoadedSuccessfully(Response.jsonObject);
                    break;
                default:
                    break;
            }
        }
    }


    public void serviceResponseError(EatndRunResponse Response) {
        if (Response != null) {
            String reqUrl = Response.mReqUrl;
            Log.d(TAG, "serviceResponseError" + reqUrl);
            switch (reqUrl) {
                case AppConstants.EatndRun_LOGIN:
                    BaseFragment.onLoginDataLoadedFailed(Response.jsonObject);
                    break;
                /*case AppConstants.EatndRun_FBLOGIN:
                    BaseFragment.onFbLoginDataLoadedFailed(Response.jsonObject);
                    break;
                case AppConstants.EatndRun_TWLOGIN:
                    BaseFragment.onTwLoginDataLoadedFailed(Response.jsonObject);
                    break;*/
                case AppConstants.EatndRun_HomeContents:
                    BaseFragment.onHomecontentDataLoadedFailed(Response.jsonObject);
                    break;
                case AppConstants.EatndRun_GETCOUNTRY:
                    BaseFragment.onCountryListLoadedFailed(Response.jsonObject);
                    break;
                case AppConstants.EatndRun_GETAREA:
                    BaseFragment.onAreaListLoadedFailed(Response.jsonObject);
                    break;
                default:
                    break;
            }
        }
    }


    public boolean isNetworkAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // You can use here an API which was added in Lollipop.
        }


        ConnectivityManager connectivityManager = (ConnectivityManager) BaseActivity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void pushFragments(Fragment fragment, boolean shouldAnimate,
                              boolean shouldAdd) {
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = fragment.getClass().getName();

        if (isNeedTransaction(backStateName)) {
            boolean fragmentPopped = manager.popBackStackImmediate(
                    backStateName, 0);

            if (!fragmentPopped) { // fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                if (shouldAnimate)
                    ft.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                ft.replace(R.id.fragment_container, fragment, backStateName);
                if (shouldAdd)
                    ft.addToBackStack(backStateName);
                ft.commit();
                manager.executePendingTransactions();
            }
        }
    }

    private boolean isNeedTransaction(String backStateName) {
        boolean needTransaction = true;
        if (BaseFragment != null) {
            String baseFrag = BaseFragment.getClass().getName();
            if (baseFrag.equals(backStateName)) {

                needTransaction = false;
            } else
                needTransaction = true;
        }
        return needTransaction;
    }

    public void clearAllBackStackEntries() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    public void popFragments(Fragment frag) {
        Log.e("Enterd here", "Inside pop fragment");
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            String fragName = frag.getClass().getName();
            manager.popBackStack(fragName,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.remove(frag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public void onBackPressed() {

        Log.e(TAG, "In else of BackPressed()");

        String fragTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        Log.e(TAG, "frag Tag : " + fragTag);
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }

    }*/




    public String getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        String fragmentName = currentFragment.getClass().getName();
        return fragmentName;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }


}
