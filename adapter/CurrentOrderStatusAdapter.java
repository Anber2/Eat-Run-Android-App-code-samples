package com.mawaqaa.eatandrun.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.CurrentOrderStatus;

import java.util.ArrayList;

/**
 * Created by HP on 11/27/2017.
 */

public class CurrentOrderStatusAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<CurrentOrderStatus> currentOrderStatusArrayList;

    public CurrentOrderStatusAdapter(Activity activity, ArrayList<CurrentOrderStatus> orderhistory_list) {
        this.context = activity;
        this.currentOrderStatusArrayList = orderhistory_list;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {


        return currentOrderStatusArrayList.size();

    }

    @Override
    public CurrentOrderStatus getItem(int position) {
        CurrentOrderStatus currentOrderStatus = currentOrderStatusArrayList.get(position);
        return currentOrderStatus;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;


        final CurrentOrderStatus currentOrderStatus = currentOrderStatusArrayList.get(position);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.single_current_order_status_item,
                    parent, false);
            viewholder = new ViewHolder();

            viewholder.orderid = (TextView) convertView.findViewById(R.id.txt_orderid_currentOrderStatus);
            viewholder.orderdate = (TextView) convertView.findViewById(R.id.txt_orderdate_currentOrderStatus);
            viewholder.orderprice = (TextView) convertView.findViewById(R.id.txt_orderprice_currentOrderStatus);
            viewholder.orderstatus = (TextView) convertView.findViewById(R.id.txt_status_currentOrderStatus);
            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.orderid.setText(currentOrderStatusArrayList.get(position).getOrderId());
        viewholder.orderdate.setText(currentOrderStatusArrayList.get(position).getOrderDate());
        viewholder.orderprice.setText(currentOrderStatusArrayList.get(position).getOrderPrice());
        viewholder.orderstatus.setText(currentOrderStatusArrayList.get(position).getOrderStatus());

        return convertView;
    }

    class ViewHolder {
        TextView orderid, orderdate, orderprice, orderstatus;
        Button btn_submit;

    }
}
