package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.NotificationListData;

import java.util.ArrayList;

/**
 * Created by HP on 11/27/2017.
 */
public class NotificationListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<NotificationListData> notificationListDataArrayList;
    NotificationListData notificationListData;
    private String TAG = "NotificationListAdapter";

    public NotificationListAdapter(Context context, ArrayList<NotificationListData> notificationListDataArrayList) {
        this.context = context;
        this.notificationListDataArrayList = notificationListDataArrayList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

            return notificationListDataArrayList.size();

    }

    @Override
    public NotificationListData getItem(int position) {

        NotificationListData notificationListData = notificationListDataArrayList.get(position);

            return notificationListData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_item_notificationlist,parent, false);

            viewholder = new ViewHolder();

            viewholder.checkBox_notification_item = (CheckBox) convertView.findViewById(R.id.checkBox_notification_item);

            viewholder.lblNotifnMsg = (TextView) convertView.findViewById(R.id.lblNotifnMsg);
            viewholder.lblNotifnMsgDate = (TextView) convertView.findViewById(R.id.lblNotifnMsgDate);
            viewholder.lblNotifnMsgTime = (TextView) convertView.findViewById(R.id.lblNotifnMsgTime);

            viewholder.imagvwNotfn = (ImageView) convertView.findViewById(R.id.imagvwNotfn);

            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }


        viewholder.lblNotifnMsg.setText(notificationListDataArrayList.get(position).getNotificationDescription());
        viewholder.lblNotifnMsgDate.setText(notificationListDataArrayList.get(position).getLastModifiedDate());
        viewholder.lblNotifnMsgTime.setText(notificationListDataArrayList.get(position).getLastModifiedDate());
        //Picasso.with(context).load(notificationListData.getCuisineImage()).into(viewholder.imagvwNotfn);

        return convertView;
    }

    public static class ViewHolder {
       public static CheckBox checkBox_notification_item;
        TextView lblNotifnMsg, lblNotifnMsgDate, lblNotifnMsgTime;
        ImageView imagvwNotfn;

    }


}
