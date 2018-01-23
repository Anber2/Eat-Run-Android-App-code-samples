package com.mawaqaa.eatandrun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.data.HomeBannerClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mawaqaa.eatandrun.Constants.AppConstants.currentAdClass;

/**
 * Created by HP on 11/27/2017.
 */

public class AdsActivity extends Activity {

    ImageButton imageButton_close;
    ImageView imageView_fillAd;

    //Ada slider
    HashMap<String, String> Hash_file_maps, Hash_file_maps2;
    SliderLayout sliderLayout;
    HomeBannerClass homeBanner;
    ArrayList<HomeBannerClass> homeBannerClassArrayList;

    int duration;


    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        initView();

        getAds();

        durationClose(duration);

    }

    private void durationClose(int start) {

     timer=new CountDownTimer(50000, 1000) {
         @Override
         public void onTick(long millisUntilFinished) {

         }

         @Override
         public void onFinish()
         {
           finish();
         }
     }.start();
    }

    private void initView() {

        imageButton_close = (ImageButton) findViewById(R.id.imageButton_close);
        imageView_fillAd = (ImageView) findViewById(R.id.imageView_fillAd);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        /*Picasso.with(this).load(AppConstants.EatndRun_BASE_IMAGES_URL + PreferenceUtil.getAdUrl(this)).placeholder(R.drawable.list_productimg).into(imageView_fillAd);*/

        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void getAds() {


        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    JSONObject jsonObject = new JSONObject();


                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(AdsActivity.this));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(AdsActivity.this));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);
                    jsonObject.putOpt("AreaId", currentAdClass);


                    makeJsonGetAdsStringReq(AppConstants.EatndRun_APIGETADVERTISEMENTBANNER, jsonObject);

                } catch (Exception xx) {
                    xx.toString();
                }

            }
        }).start();

    }


    private String makeJsonGetAdsStringReq(String urlPost, final JSONObject jsonObject) {
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

                                    homeBannerClassArrayList = new ArrayList<HomeBannerClass>();
                                    Hash_file_maps = new HashMap<String, String>();

                                    JSONArray fullScreenAdArray = jsonObj.getJSONArray("AdvertisementInterstitialList");
                                    if (fullScreenAdArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < fullScreenAdArray.length(); i++) {
                                            JSONObject arr = fullScreenAdArray.getJSONObject(i);
                                            String Image = arr.getString("Image");

                                            duration = arr.getInt("Duration");

                                            homeBanner = new HomeBannerClass(Image, "");
                                            Hash_file_maps.put(AppConstants.EatndRun_BASE_IMAGES_URL + homeBanner.getImgURL(), AppConstants.EatndRun_BASE_IMAGES_URL + homeBanner.getImgURL());
                                        }

                                        for (final String name : Hash_file_maps.keySet()) {

                                            DefaultSliderView textSliderView = new DefaultSliderView(AdsActivity.this);
                                            textSliderView.description(name)
                                                    .image(Hash_file_maps.get(name))
                                                    .setScaleType(BaseSliderView.ScaleType.Fit);

                                            textSliderView.getEmpty();
                                            textSliderView.bundle(new Bundle());
                                            textSliderView.getBundle()
                                                    .putString("extra", name);

                                            sliderLayout.addSlider(textSliderView);
                                        }

                                        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                        sliderLayout.setCustomAnimation(new DescriptionAnimation());
                                        sliderLayout.setDuration(5000);
                                        sliderLayout.addOnPageChangeListener(new EatndRunMainActivity());


                                    }

                                }

                            } catch (Exception xx) {
                                Log.e("", "   " + xx.toString());
                                xx.toString();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    String xx = error.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AdsActivity.this, R.string.string_semething_went_wrong, Toast.LENGTH_LONG).show();

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
            return e.toString();
        }

        return resultConn[0];

    }

}
