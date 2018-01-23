package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.OrderStatusData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.mawaqaa.eatandrun.R.id.lbl_orderlist_itemprice;

/**
 * Created by HP on 11/27/2017.
 */

public class OrderStatusAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<OrderStatusData> orderStatusDataArrayList;
    OrderStatusData orderStatusData;
    private String TAG = "OrderStatusAdapter";

    public OrderStatusAdapter(Context context, ArrayList<OrderStatusData> orderStatusDataArrayList) {
        this.context = context;
        this.orderStatusDataArrayList = orderStatusDataArrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderStatusDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder vh;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_order, null);

            vh = new ViewHolder();

            vh.imagvworderlist = (ImageView) convertView.findViewById(R.id.imagvworderlist);

            vh.lbl_orderlist_itemname = (TextView) convertView.findViewById(R.id.lbl_orderlist_itemname);
            vh.lbl_orderlist_itemprice = (TextView) convertView.findViewById(lbl_orderlist_itemprice);

            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        orderStatusData = orderStatusDataArrayList.get(position);

        vh.lbl_orderlist_itemname.setText(orderStatusDataArrayList.get(position).getMenuItemName());

        vh.lbl_orderlist_itemprice.setText(orderStatusDataArrayList.get(position).getPrice() + " KD");
        //Picasso.with(context).load(orderStatusData.get()).into(vh.imagvworderlist);

        Picasso.with(context).load(AppConstants.EatndRun_BASE_IMAGES_URL +
                orderStatusDataArrayList.get(position).getMenuItemImage()).fit().placeholder(R.drawable.list_productimg).into(vh.imagvworderlist);
        return convertView;
    }

    private class ViewHolder {

        ImageView imagvworderlist;
        TextView lbl_orderlist_itemname, lbl_orderlist_itemprice;
    }

}
