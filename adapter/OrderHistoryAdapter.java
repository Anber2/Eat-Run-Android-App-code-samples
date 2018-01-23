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
import com.mawaqaa.eatandrun.activity.EatndRunMainActivity;
import com.mawaqaa.eatandrun.data.OrderHistoryData;
import com.mawaqaa.eatandrun.fragment.OrderHistoryDetailsFragment;

import java.util.ArrayList;

import static com.mawaqaa.eatandrun.Constants.AppConstants.openBillNoHistoryDetails;

/**
 * Created by HP on 11/27/2017.
 */
public class OrderHistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<OrderHistoryData> orderhistory_list;
    private static LayoutInflater inflater = null;

    public OrderHistoryAdapter(Activity activity, ArrayList<OrderHistoryData> orderhistory_list) {
        this.context=activity;
        this.orderhistory_list=orderhistory_list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

            return orderhistory_list.size();

    }

    @Override
    public OrderHistoryData getItem(int position) {
        OrderHistoryData orderHistoryData = orderhistory_list.get(position);
            return orderHistoryData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewholder;


          OrderHistoryData orderHistoryData = orderhistory_list.get(position);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.singlr_order_history_item,
                    parent, false);
            viewholder = new ViewHolder();

            viewholder.orderid=(TextView)convertView.findViewById(R.id.txt_orderid_OrderHistory);
            viewholder.orderdate=(TextView)convertView.findViewById(R.id.txt_orderdate_OrderHistory);
            viewholder.orderprice=(TextView)convertView.findViewById(R.id.txt_orderprice_OrderHistory);
            viewholder.orderstatus=(TextView)convertView.findViewById(R.id.txt_status_OrderHistory);
            viewholder.btn_submit=(Button)convertView.findViewById(R.id.btn_order_submit);
            viewholder.btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openBillNoHistoryDetails = ""+orderhistory_list.get(position).getOrderId();

                    EatndRunMainActivity.getExpoBaseActivity().pushFragments(new OrderHistoryDetailsFragment(), true, true);
                }
            });
            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }


        viewholder.orderid.setText(orderhistory_list.get(position).getOrderId());
        viewholder.orderdate.setText(orderhistory_list.get(position).getOrderDate());
        viewholder.orderprice.setText(orderhistory_list.get(position).getOrderPrice());
        viewholder.orderstatus.setText(orderhistory_list.get(position).getOrderStatus());


        return convertView;
    }
    class ViewHolder{
        TextView orderid,orderdate,orderprice,orderstatus;
        Button btn_submit;

    }
}
