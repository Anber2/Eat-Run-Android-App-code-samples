package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.CuisineListData;

import java.util.ArrayList;

/**
 * Created by Admin on 6/19/2017.
 */

    public class CuisineListAdapter extends BaseAdapter {

    private String TAG = "CuisineListAdapter";
    Context context;
    ArrayList<CuisineListData> cuisineListDataArrayList;
    CuisineListData cuisineListData;

    private static LayoutInflater inflater = null;



    public CuisineListAdapter(Context context, ArrayList<CuisineListData> cuisineListDataArrayList) {
        this.context = context;
        this.cuisineListDataArrayList = cuisineListDataArrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cuisineListDataArrayList.size();
    }

    @Override
    public CuisineListData getItem(int position) {
        CuisineListData cuisineListData = cuisineListDataArrayList.get(position);

        return cuisineListData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_cuisine_list, null);

            vh = new ViewHolder();


            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        cuisineListData = cuisineListDataArrayList.get(position);

        //vh.CuisineImage = (ImageView) convertView.findViewById(R.id.CuisineImage);

        vh.CuisineName = (TextView) convertView.findViewById(R.id.CuisineName);
        /*vh.CuisineDescription = (TextView) convertView.findViewById(R.id.CuisineDescription);
        vh.MenuItemCount = (TextView) convertView.findViewById(R.id.MenuItemCount);*/

        vh.CuisineName.setText(cuisineListData.getCuisineName());
        /*vh.CuisineDescription.setText(cuisineListData.getCuisineDescription());
        vh.MenuItemCount.setText(cuisineListData.getMenuItemCount());
        Picasso.with(context).load(cuisineListData.getCuisineImage()).into(vh.CuisineImage);*/

        return convertView;
    }

    private class ViewHolder {

        ImageView CuisineImage;
        TextView CuisineName, CuisineDescription, MenuItemCount;
    }
}
