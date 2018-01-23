package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;
import com.mawaqaa.eatandrun.data.RestaurantListData;
import com.mawaqaa.eatandrun.fragment.RestaurantListDetailFragment;
import com.mawaqaa.eatandrun.fragment.RestaurantMenuFragment;
import com.mawaqaa.eatandrun.fragment.RestaurantOfferItemListFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<RestaurantListData> restaurantListDataArrayList_;
    RestaurantListData restaurantListData;
    private String TAG = "RestaurantListAdapter";

    public RestaurantListAdapter() {

    }

    public RestaurantListAdapter(Context context, ArrayList<RestaurantListData> restaurantListDataArrayList) {

        this.context = context;
        this.restaurantListDataArrayList_ = restaurantListDataArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return restaurantListDataArrayList_.size();

    }

    @Override
    public RestaurantListData getItem(int position) {
        RestaurantListData restaurantListData = restaurantListDataArrayList_.get(position);
        return restaurantListData;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_restaurantlist, null);

            vh = new ViewHolder();


            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.btn_menu = (Button) convertView.findViewById(R.id.btn_menu);
        vh.btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceUtil.setResID(context, restaurantListDataArrayList_.get(position).getRes_Id());
                Fragment RestMenuFrag = new RestaurantMenuFragment();
                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(RestMenuFrag, false, true);
            }
        });
        vh.btn_open = (Button) convertView.findViewById(R.id.btn_open);
        vh.btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceUtil.setResID(context, restaurantListDataArrayList_.get(position).getRes_Id());
                Fragment RestDeFrag = new RestaurantListDetailFragment();
                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(RestDeFrag, false, true);
            }
        });


        vh.imagvwReslist = (ImageView) convertView.findViewById(R.id.imagvwReslist);
        vh.imagvwReslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.setResID(context, restaurantListDataArrayList_.get(position).getRes_Id());
                Fragment RestDeFrag = new RestaurantListDetailFragment();
                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(RestDeFrag, false, true);
            }
        });


        vh.btn_offer = (Button) convertView.findViewById(R.id.btn_offer);
        vh.btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.setResID(context, restaurantListDataArrayList_.get(position).getRes_Id());
                Fragment RestMenuFrag = new RestaurantOfferItemListFragment();
                EatndRunBaseActivity.getExpoBaseActivity().pushFragments(RestMenuFrag, false, true);
            }
        });


        vh.lblRestname = (TextView) convertView.findViewById(R.id.lblRestname);
        vh.lblRestkmaway = (TextView) convertView.findViewById(R.id.lblRestkmaway);
        vh.lblRestAddress = (TextView) convertView.findViewById(R.id.lblRestAddress);
        vh.res_list_rating = (RatingBar) convertView.findViewById(R.id.res_list_rating);
        vh.textView_listrat = (TextView) convertView.findViewById(R.id.textView_listrat);

        vh.lblRestname.setText(restaurantListDataArrayList_.get(position).getRestaurantName());
        vh.lblRestkmaway.setText(restaurantListDataArrayList_.get(position).getRes_Distance());
        vh.lblRestAddress.setText(restaurantListDataArrayList_.get(position).getRes_Address());
        vh.textView_listrat.setText(restaurantListDataArrayList_.get(position).getRatingViewCount() + " Ratings");
        vh.res_list_rating.setRating(Float.parseFloat(restaurantListDataArrayList_.get(position).getRatingStarCount()));

        Picasso.with(context).load(AppConstants.EatndRun_BASE_IMAGES_URL +restaurantListDataArrayList_.get(position).getRes_Logo()).placeholder(R.drawable.list_productimg).into(vh.imagvwReslist);


        return convertView;
    }




    private class ViewHolder {
        Button btn_open, btn_menu, btn_offer;

        RatingBar res_list_rating;

        ImageView imagvwReslist;
        TextView lblRestname, lblRestkmaway, lblRestAddress, textView_listrat;
    }

}
