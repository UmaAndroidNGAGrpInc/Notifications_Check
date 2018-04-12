package e.userone.notificationbuilder;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by userone on 2/15/2018.
 */

public class NotificationUtils extends ContextWrapper {

    private static int NOTIFICATION_ID = 1000;
    private static int PUSH_NOTIFICATION_REQUEST_ID = 100;
    private static String MESSAGES_CHANNEL = "MessageChannel";
    private static String FLERTS_CHANNEL = "FlirtChannel";
    private static int COMMON_NOTIFICATION_ID = 150;
    int notificationId = 0;
    NotificationCompat.InboxStyle inboxStyle;
    private NotificationManager notificationManager;
    private Context mContext;

    public NotificationUtils(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        inboxStyle = new NotificationCompat.InboxStyle();

        NotificationChannel chan1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel(MESSAGES_CHANNEL,
                    MESSAGES_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);

            chan1.setLightColor(Color.RED);
            getManager().createNotificationChannel(chan1);

            NotificationChannel chan2 = new NotificationChannel(FLERTS_CHANNEL,
                    FLERTS_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
            chan2.setLightColor(Color.RED);
            getManager().createNotificationChannel(chan2);
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /**
     * Get the notification manager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public void showNotificationMessage(PushNotificationPayload data, Intent intent) {
        // Check for empty push message
        if (TextUtils.isEmpty(data.getBody()))
            return;


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        PUSH_NOTIFICATION_REQUEST_ID,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        try {
            if (data.getAcme_type().equalsIgnoreCase("2")) {
                if (Singleton.hashMapNotifications.size() > 0) {
                    if (Singleton.hashMapNotifications.containsKey(data.getTag())) {
                        Singleton.hashMapNotifications.get(data.getTag()).add(data);
                    } else {
                        List<PushNotificationPayload> payloadList = new ArrayList<>();
                        data.setNotificationId(notificationId);
                        payloadList.add(data);
                        Singleton.hashMapNotifications.put(data.getTag(), payloadList);
                        notificationId++;
                    }
                } else {
                    List<PushNotificationPayload> payloadList = new ArrayList<>();
                    data.setNotificationId(notificationId);
                    payloadList.add(data);
                    Singleton.hashMapNotifications.put(data.getTag(), payloadList);
                    notificationId++;
                }
                showMessagesNotification(mBuilder, Singleton.hashMapNotifications, intent, resultPendingIntent, System.currentTimeMillis());
            } else
                showCustomizedNotification(mBuilder, notificationManager, data, intent, resultPendingIntent, System.currentTimeMillis());
        } catch (Exception ex) {
            showCustomizedNotification(mBuilder, notificationManager, data, intent, resultPendingIntent, System.currentTimeMillis());
        }
    }

    private void showMessagesNotification(NotificationCompat.Builder mBuilder, final HashMap<String, List<PushNotificationPayload>> payload, Intent intent, PendingIntent pendingIntent, long timeMilliseconds) {
        try {
            String icon ="";
            List<String> messages = new ArrayList<>();
            int notificationId = 0;

            Iterator it = payload.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<PushNotificationPayload>> entry = (Map.Entry<String, List<PushNotificationPayload>>) it.next();
                //List<PushNotificationPayload> list = (List<PushNotificationPayload>) it.next();
                icon = entry.getValue().get(0).getIcon();
                notificationId = entry.getValue().get(0).getNotificationId();
                for (PushNotificationPayload ll : entry.getValue()) {
                    messages.add(ll.getBody());
                }
       /*     for (Map.Entry<String, List<PushNotificationPayload>> entry : payload.entrySet()) {
                String key = entry.getKey();
                List<PushNotificationPayload> list = entry.getValue();
                icon = list.get(0).getIcon();
                notificationId = list.get(0).getNotificationId();
                for(PushNotificationPayload listOfMessages:list)
                    messages.add(listOfMessages.getBody());
            }*/
            }

                showPersonWiseNotfication(mBuilder, messages, icon, pendingIntent, notificationId);

        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage() + "Check");
        }
    }


    private void showPersonWiseNotfication(NotificationCompat.Builder mBuilder, List<String> messages, String icon, PendingIntent pendingIntent, int notificationId) {

        Bitmap bitmap;
        if (!TextUtils.isEmpty(icon) && icon.length() > 3)
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        else
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        String content;
        if (messages.size() == 1)
            content = messages.get(0);
        else
            content = messages.get(messages.size() - 1);

        if (messages.size() > 0) {
            for (int i = 0; (i < 7 && i<messages.size()); i++) {
                inboxStyle.addLine(messages.get(i));
            }
            inboxStyle.setSummaryText(messages.size() - 7 + " more messages");
        }

        mBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Pyar.com")
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setStyle(inboxStyle);
/*

        if (messages.size() == 1) {
            mBuilder.setStyle(bigPictureStyle);
        } else {
            mBuilder.setStyle(inboxStyle);
        }
*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(MESSAGES_CHANNEL);
        }
        if (getManager() != null) {
            getManager().notify(notificationId, mBuilder.build());
        }

    }

    private void showCustomizedNotification(NotificationCompat.Builder mBuilder, final NotificationManager notificationManager, final PushNotificationPayload payload, Intent intent, PendingIntent pendingIntent, long timeMilliseconds) {

        try {
            Bitmap bitmap;
            if (!TextUtils.isEmpty(payload.getIcon()) && payload.getIcon().length() > 3)
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
            else
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);


            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle("Pyar.com");
            bigPictureStyle.setSummaryText(payload.getBody());
            bigPictureStyle.setBigContentTitle("Pyar.com");


            mBuilder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Pyar.com")
                    .setColor(mContext.getResources().getColor(R.color.colorAccent))
                    .setContentText(payload.getBody())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(bitmap)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setPriority(Notification.PRIORITY_DEFAULT);
            mBuilder.setStyle(bigPictureStyle);

            if (notificationManager != null) {
                notificationManager.notify("Pyar.com",COMMON_NOTIFICATION_ID, mBuilder.build());
            }

        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage() + "Check");
        }

    }
/*


    private void showSmallNotification(NotificationCompat.Builder mBuilder, NotificationManager notificationManager, PushNotificationPayload payload, Intent intent, PendingIntent resultPendingIntent, long l) {
        try {
            final NotificationCompat.Builder notification;
            notification = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Pyar.com")
                    .setContentText(payload.getBody())
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .setColor(mContext.getResources().getColor(R.color.red_dark))
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setPriority(Notification.PRIORITY_DEFAULT);
            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, notification.build());
            }

        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage() + "Check");
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, NotificationManager notificationManager, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);


        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setStyle(inboxStyle)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, NotificationManager notificationManager, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(ConstantVariables.NOTIFICATION_ID_BIG_IMAGE, notification);
        }
    }

*/
}

