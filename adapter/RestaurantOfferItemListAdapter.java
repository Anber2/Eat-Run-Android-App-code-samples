package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunMainActivity;
import com.mawaqaa.eatandrun.data.RestaurantOfferItemListData;
import com.mawaqaa.eatandrun.fragment.RestaurantOfferItemListDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantOfferItemListAdapter extends BaseAdapter {

    private String TAG = "RestaurantOfferItemListAdapter";
    Context context;
    ArrayList<RestaurantOfferItemListData> restaurantOfferItemListDataArrayList;
    RestaurantOfferItemListData restaurantOfferItemListData;


    private static LayoutInflater inflater = null;

    public RestaurantOfferItemListAdapter(Context context, ArrayList<RestaurantOfferItemListData> restaurantOfferItemListDataArrayList) {
        this.context = context;
        this.restaurantOfferItemListDataArrayList = restaurantOfferItemListDataArrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return restaurantOfferItemListDataArrayList.size();
    }

    @Override
    public RestaurantOfferItemListData getItem(int position) {

        RestaurantOfferItemListData restaurantOfferItemListData = restaurantOfferItemListDataArrayList.get(position);
        return restaurantOfferItemListData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_offer_itemlist, null);

            vh = new ViewHolder();


            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.imagvwResofferlist = (ImageView) convertView.findViewById(R.id.imagvwResofferlist);
        vh.imagvwResofferlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceUtil.setMenuItemId(context, restaurantOfferItemListDataArrayList.get(position).getMenuItemId());

                Fragment fragment = new RestaurantOfferItemListDetailFragment();
                EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
            }
        });

        vh.lbl_offer_itemname = (TextView) convertView.findViewById(R.id.lbl_offer_itemname);

        vh.lbl_offer_itemname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.setMenuItemId(context, restaurantOfferItemListDataArrayList.get(position).getMenuItemId());

                Fragment fragment = new RestaurantOfferItemListDetailFragment();
                EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
            }
        });
        vh.lbl_offer_itemprice = (TextView) convertView.findViewById(R.id.lbl_offer_itemprice);
        vh.lbl_offer_itemOldprice = (TextView) convertView.findViewById(R.id.lbl_offer_itemOldprice);



        restaurantOfferItemListData = restaurantOfferItemListDataArrayList.get(position);


        vh.lbl_offer_itemname.setText(restaurantOfferItemListDataArrayList.get(position).getMenuItemName());
        vh. lbl_offer_itemOldprice.setText(restaurantOfferItemListDataArrayList.get(position).getDiscount());
        vh.lbl_offer_itemprice.setText(restaurantOfferItemListDataArrayList.get(position).getPrice());

        Picasso.with(context).load(AppConstants.EatndRun_BASE_IMAGES_URL +restaurantOfferItemListDataArrayList.get(position).getMenuItemImage()).placeholder(R.drawable.list_productimg).into(vh.imagvwResofferlist);

        return convertView;
    }


    class ViewHolder {

        ImageView imagvwResofferlist;
        TextView lbl_offer_itemname, lbl_offer_itemprice, lbl_offer_itemOldprice;

    }
}
