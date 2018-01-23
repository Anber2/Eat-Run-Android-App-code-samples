package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.PaymentMethodData;

import java.util.ArrayList;

/**
 * Created by HP on 11/27/2017.
 */

public class PaymentMethodAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    private ArrayList<PaymentMethodData> paymentMethodDataArrayList;
    private Context context;

    public PaymentMethodAdapter(Context context, ArrayList<PaymentMethodData> paymentMethodDataArrayList) {
        this.context = context;
        this.paymentMethodDataArrayList = paymentMethodDataArrayList;
    }

    @Override
    public int getCount() {
        return paymentMethodDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        PaymentMethodData PaymentMethodData = paymentMethodDataArrayList.get(position);


        return PaymentMethodData.getCreditCard();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_payment_method_item, null);

        }


        TextView CreditCard = (TextView) view.findViewById(R.id.cardnum_txv);
        TextView Expirydate = (TextView) view.findViewById(R.id.Expirydatetex);
        TextView SecurityCode = (TextView) view.findViewById(R.id.SecurityCodeTex);
        TextView Billingaddress = (TextView) view.findViewById(R.id.BillingaddressTex);

        PaymentMethodData data = paymentMethodDataArrayList.get(position);

        CreditCard.setText(data.getCreditCard());
        Expirydate.setText(data.getExpirydate());
        SecurityCode.setText(data.getSecurityCode());
        Billingaddress.setText(data.getBillingaddress());

        return view;
    }
}
