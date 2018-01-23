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
import com.mawaqaa.eatandrun.data.OrderHistoryDetailsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 11/29/2017.
 */

public class OrderHistoryDetailsAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderHistoryDetailsData> orderHistoryDetailsDataArrayList;
    private static LayoutInflater inflater = null;

    public OrderHistoryDetailsAdapter(Context context, ArrayList<OrderHistoryDetailsData> orderHistoryDetailsDataArrayList) {
        this.context = context;
        this.orderHistoryDetailsDataArrayList = orderHistoryDetailsDataArrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderHistoryDetailsDataArrayList.size();
    }

    @Override
    public OrderHistoryDetailsData getItem(int position) {

        OrderHistoryDetailsData orderHistoryDetailsData = orderHistoryDetailsDataArrayList.get(position);
         return orderHistoryDetailsData;
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

            vh.lbl_orderlist_itemname = (TextView) convertView.findViewById(R.id.lbl_orderlist_itemname);
            vh.lbl_orderlist_itemprice = (TextView) convertView.findViewById(R.id.lbl_orderlist_itemprice);
            vh.imagvworderlist = (ImageView) convertView.findViewById(R.id.imagvworderlist);

            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        vh.lbl_orderlist_itemname.setText(orderHistoryDetailsDataArrayList.get(position).getMenuItemName());

        vh.lbl_orderlist_itemprice.setText(orderHistoryDetailsDataArrayList.get(position).getMenuItemPrice() + " KD");
         Picasso.with(context).load(AppConstants.EatndRun_BASE_IMAGES_URL +orderHistoryDetailsDataArrayList.get(position).getMenuItemImage()).placeholder(R.drawable.list_productimg).into(vh.imagvworderlist);
        return convertView;
    }

    class ViewHolder{

        ImageView imagvworderlist;
        TextView lbl_orderlist_itemname, lbl_orderlist_itemprice;
    }
}
