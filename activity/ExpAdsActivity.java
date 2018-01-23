package com.mawaqaa.eatandrun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by HP on 11/28/2017.
 */

public class ExpAdsActivity extends Activity {
    String TAG = "ExpAdsActivity";

    ImageButton imageButton_close;
    ImageView imageView_fillAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_ads);

        initView();


    }

    private void initView() {

        imageButton_close = (ImageButton) findViewById(R.id.imageButton_close);
        imageView_fillAd = (ImageView) findViewById(R.id.imageView_fillAd);

        Picasso.with(this).load( PreferenceUtil.getAdUrl(this)).into(imageView_fillAd);
        Log.d(TAG," **- -"+ AppConstants.EatndRun_BASE_IMAGES_URL + PreferenceUtil.getAdUrl(this));

        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
