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
import com.mawaqaa.eatandrun.data.PaymentHistoryData;

import java.util.ArrayList;

/**
 * Created by HP on 11/27/2017.
 */

public class PaymentHistoryAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<PaymentHistoryData> historyDatas;

    public PaymentHistoryAdapter(Activity activity, ArrayList<PaymentHistoryData> historyDatas) {
        this.context = activity;
        this.historyDatas = historyDatas;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {


        return historyDatas.size();

    }

    @Override
    public PaymentHistoryData getItem(int position) {
        PaymentHistoryData paymentHistoryData = historyDatas.get(position);
        return paymentHistoryData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewholder;


        final PaymentHistoryData paymentHistoryData = historyDatas.get(position);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.single_payment_history_item,
                    parent, false);
            viewholder = new ViewHolder();

            viewholder.orderid = (TextView) convertView.findViewById(R.id.txt_orderid_PaymentHistory);
            viewholder.orderdate = (TextView) convertView.findViewById(R.id.txt_orderdate_PaymentHistory);
            viewholder.orderprice = (TextView) convertView.findViewById(R.id.txt_orderprice_PaymentHistory);
            viewholder.orderstatus = (TextView) convertView.findViewById(R.id.txt_status_PaymentHistory);
            viewholder.txt_transactionid_value = (TextView) convertView.findViewById(R.id.txt_transactionid_PaymentHistory);

            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }


        viewholder.orderid.setText(historyDatas.get(position).getOrderid());
        viewholder.orderdate.setText(historyDatas.get(position).getOrderdate());
        viewholder.orderprice.setText(historyDatas.get(position).getAmount());
        viewholder.orderstatus.setText(historyDatas.get(position).getStatus());
        viewholder.txt_transactionid_value.setText(historyDatas.get(position).getTransactionid());

        return convertView;


    }

    class ViewHolder {
        TextView orderid, orderdate, orderprice, orderstatus, txt_transactionid_value;
        Button btn_submit;

    }
}
