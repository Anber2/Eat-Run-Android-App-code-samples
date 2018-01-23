package com.mawaqaa.eatandrun.fragment;

import android.app.ActionBar;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ProgressBar;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;

import org.json.JSONObject;

/**
 * Created by siva on 11/24/2016.
 */
public class EatndRunBaseFragment extends Fragment {
    // can dismiss spinweheel afrer a
    // specific time
    public static final int SPINWHEEL_LIFE_TIME = 700; /*
                                                         * Dismiss spin wheel
														 * after 5 seconds
														 */
    private static final String TAG = "ExpoBaseFragment";
    public EatndRunBaseActivity Activity;
    Handler spinWheelTimer = new Handler(); // Handler to post a runnable that
    private DrawerLayout drawerLayout;
    private Dialog spinWheelDialog;
    Runnable dismissSpinner = new Runnable() {

        @Override
        public void run() {
            stopSpinWheel();
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();

        String t = this.getClass().getSimpleName();

        //Toast.makeText(getApplicationContext(), t + "  22", Toast.LENGTH_SHORT).show();

        /*drawerLayout = (DrawerLayout) getView().findViewById(R.id.drawer_layout);

        if(t.equals("LoginFragment")){drawerLayout.closeDrawer(Gravity.RIGHT);}
*/
    }

    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;

    }

    public void onLoginDataLoadedSuccessfully(JSONObject jsonObject) {
    }

    public void onLoginDataLoadedFailed(JSONObject jsonObject) {
    }

    public void onFbLoginDataLoadedSuccessfully(JSONObject jsonObject) {
    }

    public void onFbLoginDataLoadedFailed(JSONObject jsonObject) {
    }

    public void onTwLoginDataLoadedSuccessfully(JSONObject jsonObject) {
    }

    public void onTwLoginDataLoadedFailed(JSONObject jsonObject) {
    }

    public void onHomecontentDataLoadedSuccessfully(JSONObject jsonObject) {
    }

    public void onHomecontentDataLoadedFailed(JSONObject jsonObject) {
    }

    public void onCountryListLoadedSuccessfully(JSONObject jsonObject) {
    }

    public void onCountryListLoadedFailed(JSONObject jsonObject) {
    }

    public void onAreaListLoadedSuccessfully(JSONObject jsonObject) {
    }

    public void onAreaListLoadedFailed(JSONObject jsonObject) {
    }

    public void startSpinwheel(boolean setDefaultLifetime, boolean isCancelable) {
        // Log.d(TAG, "startSpinwheel"+getCurrentActivity().getClass() );
        // If already showing no need to create.
        if (spinWheelDialog != null && spinWheelDialog.isShowing())
            return;
        spinWheelDialog = new Dialog(Activity, R.style.wait_spinner_style);
        ProgressBar progressBar = new ProgressBar(Activity);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        spinWheelDialog.addContentView(progressBar, layoutParams);
        spinWheelDialog.setCancelable(isCancelable);
        spinWheelDialog.show();
        // start timer for SPINWHEEL_LIFE_TIME
        spinWheelTimer.removeCallbacks(dismissSpinner);
        if (setDefaultLifetime) // If requested for default dismiss time.
            spinWheelTimer.postAtTime(dismissSpinner, SystemClock.uptimeMillis() + SPINWHEEL_LIFE_TIME);

        spinWheelDialog.setCanceledOnTouchOutside(false);
    }

    public void startSpinwheel(boolean isCancelable, int layoutid, int timeOutSec) {
        startSpinwheel(true, isCancelable);
        spinWheelTimer.removeCallbacks(dismissSpinner);
        spinWheelTimer.postAtTime(dismissSpinner, SystemClock.uptimeMillis() + timeOutSec);
        spinWheelDialog.setContentView(layoutid);
    }

    /**
     * Closes the spin wheel dialog
     */

    public void stopSpinWheel() {
        // Log.d(TAG, "stopSpinWheel"+getCurrentActivity().getClass());
        if (spinWheelDialog != null)
            try {
                spinWheelDialog.dismiss();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Parent is died while tryingto dismiss spin wheel dialog ");
                e.printStackTrace();
            }
        spinWheelDialog = null;
    }

    // Callback for spin wheel dismissal
    protected void onSpinWheelDismissed() {
        Log.d(TAG, "Spin wheel disconnected");
    }

}