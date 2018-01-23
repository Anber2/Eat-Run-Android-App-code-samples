package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.DrawerListData;

import java.util.ArrayList;

public class DrawerListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<DrawerListData> mDrawerListItems;
    ViewHolder holder;
    LayoutInflater inflater;
    DrawerListData mDrawerData;

    public DrawerListViewAdapter(Context context, ArrayList<DrawerListData> mDrawerListItems) {
        this.context = context;
        this.mDrawerListItems = mDrawerListItems;
    }

    @Override
    public int getCount() {
        return mDrawerListItems.size();
    }

    @Override
    public DrawerListData getItem(int position) {
        DrawerListData drawerListData = mDrawerListItems.get(position);

        return drawerListData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerData = mDrawerListItems.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.drawer_listview_layout, parent, false);
            holder.lblListItem = (TextView) convertView.findViewById(R.id.lblDrawerListItem);
            holder.lblImageItem = (ImageView) convertView.findViewById(R.id.lblImageItem);
            //FontUtils.setEbrimaFont(context, holder.lblListItem);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.lblListItem.setText(mDrawerData.getName());
        holder.lblImageItem.setImageResource(mDrawerData.getImageId());
        return convertView;
    }

    class ViewHolder {
        TextView lblListItem;
        ImageView lblImageItem;
    }

}
