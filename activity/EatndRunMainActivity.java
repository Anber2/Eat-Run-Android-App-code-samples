package com.mawaqaa.eatandrun.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.DrawerUtils;
import com.mawaqaa.eatandrun.Utilities.MyService;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.adapter.AdBannerAdapter;
import com.mawaqaa.eatandrun.adapter.DrawerListViewAdapter;
import com.mawaqaa.eatandrun.data.DrawerListData;
import com.mawaqaa.eatandrun.data.HomeBannerClass;
import com.mawaqaa.eatandrun.fragment.ContactUsFragment;
import com.mawaqaa.eatandrun.fragment.CurrentOrderStatusFragment;
import com.mawaqaa.eatandrun.fragment.CustomerAccountFragment;
import com.mawaqaa.eatandrun.fragment.FragmentLanguage;
import com.mawaqaa.eatandrun.fragment.HomeFrag;
import com.mawaqaa.eatandrun.fragment.JoinBillOfJoinBillSplitEvently;
import com.mawaqaa.eatandrun.fragment.NotificationListFragment;
import com.mawaqaa.eatandrun.fragment.OrderHistoryFragment;
import com.mawaqaa.eatandrun.fragment.RestaurantListFragment;
import com.mawaqaa.eatandrun.fragment.TermsandConditionsFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class EatndRunMainActivity extends EatndRunBaseActivity implements View.OnClickListener, ViewPagerEx.OnPageChangeListener {

    private static ViewPager mPager;
    public String lat, lng;
    ImageView btnMenu;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    //...........Drawer item initialization ....................//
    DrawerListViewAdapter mDrawerListAdpater;
    ArrayList<DrawerListData> mDrawerItems;
    //  private String[] mDrawerItemNames;
    Intent serviceIntent;
    Class fragmentClass = null;
   /* HashMap<String, String> Hash_file_maps, Hash_file_maps2;
    SliderLayout sliderLayout;*/
    TextView lbl_reg_rest_value, lbl_reg_cust_value, btnBack;
    HomeBannerClass homeBanner;
    ArrayList<HomeBannerClass> homeBannerClassArrayList;
    AdBannerAdapter adBannerAdapter;
    RelativeLayout layout_menu;

    private String TAG = "EtndRunMainActvty";
    private Toolbar Aj_toolbar;
    private ImageButton btnMoreMenu;
    private ProgressDialog progressBar;

    @Override
    public String getCurrentFragment() {
        return super.getCurrentFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eatnd_run_main);

        serviceIntent = new Intent(this, MyService.class);

        //sliderLayout = (SliderLayout) findViewById(R.id.slider);

        layout_menu = (RelativeLayout) findViewById(R.id.layout_menu);

        setDrawerLayout();
        btnMenu = (ImageView) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);


        if (PreferenceUtil.isUserSignedIn(this)&&PreferenceUtil.getrememberMe(this)) {

            Fragment HomeFrag = new HomeFrag();
            pushFragments(HomeFrag, false, true);
            //getApi();
        } else {


            Intent i = new Intent(EatndRunMainActivity.this, LoginActivity.class);
            startActivity(i);

            EatndRunMainActivity.this.finish();
        }

        btnBack = (TextView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFrag homeFrag = new HomeFrag();
                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(homeFrag, false, true);
            }
        });


    }

    /*public void getApi() {

        progressBar = ProgressDialog.show(this, "", getString(R.string.progressbar_please_wait), true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    JSONObject jsonObject = new JSONObject();


                    jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(EatndRunMainActivity.this));
                    jsonObject.putOpt(AppConstants.LanguageId, PreferenceUtil.getLanguage(EatndRunMainActivity.this));
                    jsonObject.putOpt(AppConstants.SecurityKey, AppConstants.SecurityKeyValue);
                    jsonObject.putOpt("AreaId",  1);



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

                                    JSONArray adsImagearry = jsonObj.getJSONArray("AdvertisementBannerList");
                                    if (adsImagearry.length() == 0) {
                                        sliderLayout.setVisibility(View.GONE);
                                    } else {
                                        sliderLayout.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < adsImagearry.length(); i++) {
                                            JSONObject arr = adsImagearry.getJSONObject(i);
                                            String Image = arr.getString("Image");
                                            String NavigateUrl = arr.getString("NavigateUrl");
                                            homeBanner = new HomeBannerClass(Image, NavigateUrl);
                                            Hash_file_maps.put(AppConstants.EatndRun_BASE_IMAGES_URL + homeBanner.getImgURL(), AppConstants.EatndRun_BASE_IMAGES_URL + homeBanner.getImgURL());

                                        }

                                        for (final String name : Hash_file_maps.keySet()) {

                                            DefaultSliderView textSliderView = new DefaultSliderView(EatndRunMainActivity.this);
                                            textSliderView.description(name)
                                                    .image(Hash_file_maps.get(name))
                                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                            ;

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
                                        sliderLayout.addOnPageChangeListener(EatndRunMainActivity.this);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EatndRunMainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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

    }*/


    //.................... SET Drawer Layout ..........................//

    public void setDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerItems = new ArrayList<DrawerListData>();
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        // mDrawerItemNames = getResources().getStringArray(R.array.more_items);


        try {
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_cus_acc), R.drawable.menu_customer));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_language), R.drawable.menu_language));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_join_bill), R.drawable.menu_join));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_currentorder), R.drawable.menu_orderstatus));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_order_history), R.drawable.menu_orderhistory));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_notification), R.drawable.menu_notifications));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_contact_us), R.drawable.menu_contact));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_terms_and_conditions), R.drawable.menu_orderhistory));
            mDrawerItems.add(new DrawerListData(getString(R.string.eatandrum_main_activity_drawerlist_data_logout), R.drawable.menu_logout));
        } catch (Exception e) {
            Log.e(TAG, "Exception in setDrawer Method");
            e.printStackTrace();
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        CustomerAccountFragment customeraccount = new CustomerAccountFragment();
                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(customeraccount, false, true);
                        break;
                    case 1:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        FragmentLanguage LangFrag = new FragmentLanguage();
                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(LangFrag, false, true);
                        break;

                    case 2:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        JoinBillOfJoinBillSplitEvently fragmentJoinBillSplit = new JoinBillOfJoinBillSplitEvently();

                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(fragmentJoinBillSplit, false, true);
                        break;
                    case 3:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        CurrentOrderStatusFragment currentorderstaus = new CurrentOrderStatusFragment();
                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(currentorderstaus, false, true);
                        break;
                    case 4:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        OrderHistoryFragment orderhistoryFrag = new OrderHistoryFragment();
                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(orderhistoryFrag, false, true);
                        break;
                    case 5:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        NotificationListFragment nfrag = new NotificationListFragment();
                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(nfrag, false, true);
                        break;
                    case 6:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        ContactUsFragment cfrag = new ContactUsFragment();
                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(cfrag, false, true);
                        break;
                    case 7:
                        DrawerUtils.closeDrawerVeiw(getApplicationContext(), mDrawerLayout);
                        TermsandConditionsFragment termsandcon = new TermsandConditionsFragment();
                        EatndRunBaseActivity.getExpoBaseActivity().pushFragments(termsandcon, false, true);

                        break;

                    case 8:

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                EatndRunMainActivity.this);
                        alertDialogBuilder
                                .setMessage(R.string.eatandrum_main_activity_confirm_logout)
                                .setCancelable(false)
                                .setPositiveButton(R.string.string_yes,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {


                                                PreferenceUtil.setKeepMeSignedIn(
                                                        EatndRunMainActivity.this, false);
                                                PreferenceUtil.setUserSignedIn(EatndRunMainActivity.this,
                                                        false);
                                                Toast.makeText(EatndRunMainActivity.this, R.string.eatandrum_main_activity_logged_out_msg,
                                                        Toast.LENGTH_SHORT).show();

                                                DrawerUtils.closeDrawerVeiw(EatndRunMainActivity.this,
                                                        mDrawerLayout);
                                               /* Fragment loginFragment = new LoginFragment();

                                                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(loginFragment, false, true);
*/

                                                Intent i = new Intent(EatndRunMainActivity.this, LoginActivity.class);

                                                startActivity(i);
                                                EatndRunMainActivity.this.finish();

                                            }
                                        })
                                .setNegativeButton(R.string.string_no,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {

                                                dialog.cancel();
                                            }
                                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();

                        alertDialog.show();


                        break;


                    default:
                        break;
                }
            }
        });

        mDrawerListAdpater = new DrawerListViewAdapter(getApplicationContext(),
                mDrawerItems);
        mDrawerList.setAdapter(mDrawerListAdpater);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMenu:
                DrawerUtils.openDrawerView(this, mDrawerLayout, btnMenu);
                break;
            default:
                break;
        }
    }
    //.................... SET Drawer Layout ..........................//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // FragmentManager fragment = getSupportFragmentManager();
        String fragTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        //Log.e(TAG, "frag Tag : " + fragTag);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragTag);
        if (fragment != null) {

            fragment.onActivityResult(requestCode, resultCode, data);
        } else Log.d("Twitter", "fragment is null");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 201) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Gratend", Toast.LENGTH_LONG).show();
                this.startService(serviceIntent);
                lat = PreferenceUtil.getlat(this);
                lng = PreferenceUtil.getlng(this);
                Log.e("LOCCCMMMMMMM", lat + "  " + lng);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onBackPressed() {


        try {

            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT); //CLOSE Nav Drawer!
            }

            String tag = getSupportFragmentManager().
                    getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();


            int fragments = getSupportFragmentManager().getBackStackEntryCount();


            if (tag.equalsIgnoreCase("com.mawaqaa.eatandrun.fragment.FragmenPayBillConfirm")
                    || tag.equalsIgnoreCase("com.mawaqaa.eatandrun.fragment.FragmenSplitBillEvently")
                    || tag.equalsIgnoreCase("com.mawaqaa.eatandrun.fragment.FragmentChooseItemToSplitConfirm")
                    ) {

                Fragment frag = new RestaurantListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }   else if (fragments == 1) {
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EatndRunMainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else


                super.onBackPressed();

        } catch (Exception xx) {
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        invokeFragmentManagerNoteStateNotSaved();

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void invokeFragmentManagerNoteStateNotSaved() {
        /**
         * For post-Honeycomb devices
         */
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        try {
            Class cls = getClass();
            do {
                cls = cls.getSuperclass();
            } while (!"Activity".equals(cls.getSimpleName()));
            Field fragmentMgrField = cls.getDeclaredField("mFragments");
            fragmentMgrField.setAccessible(true);

            Object fragmentMgr = fragmentMgrField.get(this);
            cls = fragmentMgr.getClass();

            Method noteStateNotSavedMethod = cls.getDeclaredMethod("noteStateNotSaved", new Class[]{});
            noteStateNotSavedMethod.invoke(fragmentMgr, new Object[]{});
            Log.d("DLOutState", "Successful call for noteStateNotSaved!!!");
        } catch (Exception ex) {
            Log.e("DLOutState", "Exception on worka FM.noteStateNotSaved", ex);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}