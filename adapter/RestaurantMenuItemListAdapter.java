package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunMainActivity;
import com.mawaqaa.eatandrun.data.RestaurantMenuItemListData;
import com.mawaqaa.eatandrun.fragment.RestaurantMenuItemListDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.mawaqaa.eatandrun.Constants.AppConstants.EatndRun_BASE_APP_URL;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantMenuItemListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<RestaurantMenuItemListData> restaurantMenuItemListDataArrayList;
    RestaurantMenuItemListData restaurantMenuItemListData;
    private String TAG = "RestaurantMenuItemListAdapter";

    public RestaurantMenuItemListAdapter(Context context, ArrayList<RestaurantMenuItemListData> restaurantMenuItemListDataArrayList) {
        this.context = context;
        this.restaurantMenuItemListDataArrayList = restaurantMenuItemListDataArrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {


        return restaurantMenuItemListDataArrayList.size();

    }

    @Override
    public RestaurantMenuItemListData getItem(int position) {

        RestaurantMenuItemListData restaurantMenuItemListData = restaurantMenuItemListDataArrayList.get(position);

        return restaurantMenuItemListData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_restaurantlmenu_itemlist, null);

            vh = new ViewHolder();




            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.ResMenulistlogo = (ImageView) convertView.findViewById(R.id.imagvwResMenulist);
        vh.ResMenulistlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.setMenuItemId(context, restaurantMenuItemListDataArrayList.get(position).getMenuItemId());

                Fragment fragment = new RestaurantMenuItemListDetailFragment();
                EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
            }
        });

        vh.menu_itemname = (TextView) convertView.findViewById(R.id.lbl_menu_itemname);
        vh.menu_itemname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.setMenuItemId(context, restaurantMenuItemListDataArrayList.get(position).getMenuItemId());

                Fragment fragment = new RestaurantMenuItemListDetailFragment();
                EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
            }
        });

        vh.arrow_itemname = (ImageView) convertView.findViewById(R.id.arrow_itemname);
        vh.arrow_itemname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.setMenuItemId(context, restaurantMenuItemListDataArrayList.get(position).getMenuItemId());

                Fragment fragment = new RestaurantMenuItemListDetailFragment();
                EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
            }
        });
        vh.menu_itemprice = (TextView) convertView.findViewById(R.id.lbl_menu_itemprice);

        vh.menu_itemname.setText(restaurantMenuItemListDataArrayList.get(position).getMenuItemName());
        vh.menu_itemprice.setText(restaurantMenuItemListDataArrayList.get(position).getPrice());
        Picasso.with(context).load(EatndRun_BASE_APP_URL+restaurantMenuItemListDataArrayList.get(position).getMenuItemImage()).placeholder(R.drawable.list_productimg).into(vh.ResMenulistlogo);


        Log.d("MenuItemImage",EatndRun_BASE_APP_URL+restaurantMenuItemListDataArrayList.get(position).getMenuItemImage());
        return convertView;
    }


    class ViewHolder {
        ImageView ResMenulistlogo, arrow_itemname;

        TextView menu_itemname, menu_itemprice;

    }
}
