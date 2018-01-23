package com.mawaqaa.eatandrun.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;

/**
 * Created by Admin on 6/14/2017.
 */

public class SocialMediaPupupActivity extends Activity {
    TextView textViewfb, textViewtw, textViewins, nodata_txt_socialmedia;

    LinearLayout fbLayout, twLayout, insLayout;

    Button button_FB, button_Tw, button_inst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialmedia);

        button_FB = (Button) findViewById(R.id.button_FB);
        button_Tw = (Button) findViewById(R.id.button_Tw);
        button_inst = (Button) findViewById(R.id.button_inst);

        fbLayout = (LinearLayout) findViewById(R.id.fbLayout);
        twLayout = (LinearLayout) findViewById(R.id.twLayout);
        insLayout = (LinearLayout) findViewById(R.id.insLayout);

        textViewfb = (TextView) findViewById(R.id.textViewfb);
        textViewtw = (TextView) findViewById(R.id.textViewtw);
        textViewins = (TextView) findViewById(R.id.textViewins);

        nodata_txt_socialmedia = (TextView) findViewById(R.id.nodata_txt_socialmedia);

        if (PreferenceUtil.getResFacebook(this).equalsIgnoreCase("null")) {
            fbLayout.setVisibility(View.GONE);
        } else {
            button_FB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PreferenceUtil.getResFacebook(SocialMediaPupupActivity.this)));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SocialMediaPupupActivity.this, "Can not open link", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (PreferenceUtil.getResTwitter(this).equalsIgnoreCase("null")) {
            twLayout.setVisibility(View.GONE);

        } else {
            button_Tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PreferenceUtil.getResTwitter(SocialMediaPupupActivity.this)));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SocialMediaPupupActivity.this, "Can not open link", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        if (PreferenceUtil.getResInstagram(this).equalsIgnoreCase("null")) {
            insLayout.setVisibility(View.GONE);

        } else {
            button_inst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PreferenceUtil.getResInstagram(SocialMediaPupupActivity.this)));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SocialMediaPupupActivity.this, "Can not open link!", Toast.LENGTH_SHORT).show();

                    }
                }
            });        }




    }


}
