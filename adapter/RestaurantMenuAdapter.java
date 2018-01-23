package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunMainActivity;
import com.mawaqaa.eatandrun.data.RestaurantMenuData;
import com.mawaqaa.eatandrun.fragment.RestaurantMenuItemListFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 11/27/2017.
 */
public class RestaurantMenuAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<RestaurantMenuData> restaurantMenuDataArrayList;
    RestaurantMenuData restaurantMenuData;
    private String TAG = "RestaurantMenuAdapter";


    public RestaurantMenuAdapter(Context context, ArrayList<RestaurantMenuData> restaurantMenuDataArrayList) {
        this.context = context;
        this.restaurantMenuDataArrayList = restaurantMenuDataArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {

        return restaurantMenuDataArrayList.size();
    }

    @Override
    public RestaurantMenuData getItem(int position) {

        RestaurantMenuData restaurantMenuData = restaurantMenuDataArrayList.get(position);

        return restaurantMenuData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_restaurantmenu, null);

            vh = new ViewHolder();

            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        vh.Logo = (ImageView) convertView.findViewById(R.id.imgVwMenu);

        vh.MenuSectionName = (TextView) convertView.findViewById(R.id.MenuSectionName);
        vh.MenuSectionCount = (TextView) convertView.findViewById(R.id.MenuSectionCount);
        vh.imgnotavalable = (TextView) convertView.findViewById(R.id.imgnotavalable);

        vh.Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!restaurantMenuDataArrayList.get(position).getMenuSectionCount().equalsIgnoreCase("0")) {
                    PreferenceUtil.setMenuSectionId(context, restaurantMenuDataArrayList.get(position).getMenuSectionId());
                    Fragment fragment = new RestaurantMenuItemListFragment();
                    EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
                   AppConstants.MenuSectionName = restaurantMenuDataArrayList.get(position).getMenuSectionName();
                } else {
                    Toast.makeText(context, "This menu is currently empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vh.MenuSectionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!restaurantMenuDataArrayList.get(position).getMenuSectionCount().equalsIgnoreCase("0")) {
                    PreferenceUtil.setMenuSectionId(context, restaurantMenuDataArrayList.get(position).getMenuSectionId());
                    Fragment fragment = new RestaurantMenuItemListFragment();
                    EatndRunMainActivity.getExpoBaseActivity().pushFragments(fragment, true, true);
                    AppConstants.MenuSectionName = restaurantMenuDataArrayList.get(position).getMenuSectionName();

                } else {
                    Toast.makeText(context, "This menu is currently empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        restaurantMenuData = restaurantMenuDataArrayList.get(position);

        vh.MenuSectionName.setText(restaurantMenuDataArrayList.get(position).getMenuSectionName());
        vh.MenuSectionCount.setText(restaurantMenuDataArrayList.get(position).getMenuSectionCount() + " Items");

         Picasso.with(context).load(AppConstants.EatndRun_BASE_IMAGES_URL +
                restaurantMenuDataArrayList.get(position).getLogo()).fit().into(vh.Logo);

        return convertView;
    }


    class ViewHolder {

        ImageView Logo;
        TextView MenuSectionName, MenuSectionCount, imgnotavalable;

    }
}
