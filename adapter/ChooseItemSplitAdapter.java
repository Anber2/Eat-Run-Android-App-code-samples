package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.OrderStatusData;
import com.mawaqaa.eatandrun.fragment.FragmentChooseItemToSplit;

import java.util.ArrayList;

import static com.mawaqaa.eatandrun.R.id.checkBox_item;
import static com.mawaqaa.eatandrun.R.id.textView_qty;
import static java.lang.Integer.parseInt;

/**
 * Created by HP on 11/27/2017.
 */

public class ChooseItemSplitAdapter extends BaseAdapter {
    static int minteger;
    private static LayoutInflater inflater = null;

    Context context;
    ArrayList<OrderStatusData> orderStatusDataArrayList = new ArrayList<OrderStatusData>();
    OrderStatusData orderStatusData;
    private String TAG = "OrderStatusAdapter";
    public static ArrayList<Boolean> status = new ArrayList<Boolean>();
    public static ArrayList<String> QtyStrings = new ArrayList<String>();


    public ChooseItemSplitAdapter(Context context, ArrayList<OrderStatusData> orderStatusDataArrayList) {
        this.context = context;
        this.orderStatusDataArrayList = orderStatusDataArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < orderStatusDataArrayList.size(); i++) {
            status.add(false);
        }


    }

    @Override
    public int getCount() {
        return orderStatusDataArrayList.size();
    }

    @Override
    public OrderStatusData getItem(int position) {
        OrderStatusData orderStatusData = orderStatusDataArrayList.get(position);
        return orderStatusData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return orderStatusDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        orderStatusData = orderStatusDataArrayList.get(position);


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.single_choose_item_split, null);
            vh = new ViewHolder();

            vh.checkBox_item = (CheckBox) convertView.findViewById(checkBox_item);
            vh.imagvworderlist = (ImageView) convertView.findViewById(R.id.imagvworderlist_choose_item);
            vh.lbl_orderlist_itemname = (TextView) convertView.findViewById(R.id.lbl_orderlist_itemname_choose_item);
            vh.lbl_orderlist_itemprice = (TextView) convertView.findViewById(R.id.lbl_orderlist_itemprice_choose_item);
            vh.textView_qty = (TextView) convertView.findViewById(textView_qty);
            vh.buttonPlus = (Button) convertView.findViewById(R.id.btn_food_item_plus);
            vh.buttonMinus = (Button) convertView.findViewById(R.id.btn_food_item_minus);
            vh.textView_qty.setText("" + FragmentChooseItemToSplit.Number[position]);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        QtyStrings.add(position, "" + orderStatusDataArrayList.get(position).getQny());


        vh.lbl_orderlist_itemname.setText(orderStatusData.getMenuItemName());
        vh.lbl_orderlist_itemprice.setText(orderStatusData.getPrice() + " KD");


        vh.checkBox_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    status.set(position, true);
                } else {
                    status.set(position, false);
                }
            }
        });
        vh.checkBox_item.setChecked(status.get(position));

        vh.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num = parseInt(vh.textView_qty.getText().toString());

                if (num < parseInt(orderStatusDataArrayList.get(position).getQny())) {
                    FragmentChooseItemToSplit.Number[position] = FragmentChooseItemToSplit.Number[position] + 1;
                    num++;
                }
                vh.textView_qty.setText("" + FragmentChooseItemToSplit.Number[position]);
                QtyStrings.set(position, "" + num);

            }
        });


        vh.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num = parseInt(vh.textView_qty.getText().toString());

                if (num > 1) {
                    FragmentChooseItemToSplit.Number[position] = FragmentChooseItemToSplit.Number[position] - 1;
                    num--;

                }
                vh.textView_qty.setText("" + FragmentChooseItemToSplit.Number[position]);
                QtyStrings.set(position, "" + num);
                notifyDataSetChanged();

            }
        });


        return convertView;
    }

    class ViewHolder {

        Button buttonPlus, buttonMinus;
        CheckBox checkBox_item;
        ImageView imagvworderlist;
        TextView lbl_orderlist_itemname, lbl_orderlist_itemprice, textView_qty;


    }


}
