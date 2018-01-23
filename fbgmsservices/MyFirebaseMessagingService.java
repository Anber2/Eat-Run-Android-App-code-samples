package com.mawaqaa.eatandrun.fbgmsservices;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;
import com.mawaqaa.eatandrun.activity.EatndRunMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mawaqaa.eatandrun.fragment.FragmenSplitBillEvently.makeJsonGetOrderItems2StringReq;
import static com.mawaqaa.eatandrun.fragment.FragmentChooseItemToSplit.makeJsonGetOrderItems3StringReq;
import static com.mawaqaa.eatandrun.fragment.FragmentOpenBill.btn_Register_bill;
import static com.mawaqaa.eatandrun.fragment.FragmentOpenBill.txt_pressNest;
import static com.mawaqaa.eatandrun.fragment.FragmentOrderStatus.button_Accept_orderBill;
import static com.mawaqaa.eatandrun.fragment.FragmentOrderStatus.makeJsonGetOrderItemsStringReq;

/**
 * Created by HP on 11/27/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    handleNotification(remoteMessage.getNotification().getBody());

                }
            });

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(final String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            //app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


            if (message.equalsIgnoreCase("Order Updated")) {

                btn_Register_bill.setVisibility(View.VISIBLE);
                txt_pressNest.setVisibility(View.VISIBLE);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {

                        Toast.makeText(getApplicationContext(), "Order Has been Updated" + "\n" +
                                "Please Check Your Orders List!", Toast.LENGTH_LONG).show();
                    }
                });
                //button_Accept_orderBill.setVisibility(View.VISIBLE);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.putOpt("OpenOrderNumber", PreferenceUtil.getOpenBillNumber(getApplicationContext()));
                            jsonObject.putOpt("CustomerId", PreferenceUtil.getUserId(getApplicationContext()));

                            if (AppConstants.currentClass == "1") {
                                makeJsonGetOrderItemsStringReq(AppConstants.EatndRun_GETORDERITEMS, jsonObject);
                                button_Accept_orderBill.setVisibility(View.VISIBLE);
                            } else if (AppConstants.currentClass == "2") {
                                makeJsonGetOrderItems2StringReq(AppConstants.EatndRun_GETORDERITEMS, jsonObject);
                            } else if (AppConstants.currentClass == "3") {
                                makeJsonGetOrderItems3StringReq(AppConstants.EatndRun_GetItemsToSplit, jsonObject);
                            }

                        } catch (Exception xx) {
                            xx.toString();
                        }

                    }
                }).start();

            } else {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    }
                });
            }
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String type = data.getString("notificationtype");
            String dataid = data.getString("dataid");
            //Log.e("CHECKOOOO","val"+type+""+dataid);
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), EatndRunMainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}