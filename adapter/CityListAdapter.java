package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.CityData;

import java.util.ArrayList;

/**
 * Created by HP on 8/22/2017.
 */

public class CityListAdapter extends BaseAdapter {
    String TAG = "CityListAdapter";
    Context context;
    ArrayList<CityData> arrayListArea;
    private static LayoutInflater inflater = null;


    public CityListAdapter(Context context,
                           ArrayList<CityData> arrayListArea) {
        this.context = context;
        this.arrayListArea = arrayListArea;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return arrayListArea.size();

    }

    @Override
    public CityData getItem(int position) {

        CityData area = arrayListArea.get(position);

        return area;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        final CityData arealist = arrayListArea.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_country_list,
                    parent, false);
            vh = new ViewHolder();
            vh.item_CountryName = (TextView) convertView
                    .findViewById(R.id.item_countryname);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.item_CountryName.setText((arealist.getCityName() == "null") ? context.getResources().getString(R.string.area)
                : arealist.getCityName());
        return convertView;
    }

    private class ViewHolder {
        // TextView tv;
        TextView item_CountryName;
    }

}
