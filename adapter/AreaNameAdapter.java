package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.Area;

import java.util.ArrayList;

public class AreaNameAdapter extends BaseAdapter {
    String TAG = "CountryNameAdapter";
    Context context;
    ArrayList<Area> arrayListArea;
    private static LayoutInflater inflater = null;


    public AreaNameAdapter(Context context,
                           ArrayList<Area> arrayListArea) {
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
    public Area getItem(int position) {

        Area area = arrayListArea.get(position);

            return area;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        final Area arealist = arrayListArea.get(position);
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
        vh.item_CountryName.setText((arealist.getAreaName() == "null") ? context.getResources().getString(R.string.area)
                : arealist.getAreaName());
        return convertView;
    }

    private class ViewHolder {
        // TextView tv;
        TextView item_CountryName;
    }

}
