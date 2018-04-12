package e.userone.notificationbuilder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String messages = "A basic notification usually includes a title, a line of text, and one or more actions the user can perform in response. To provide even more information, you can also create large, expandable notifications by applying one of several notification templates as described on this page.\n" +
            "\n" +
            "To start, build a notification with all the basic content as described in Create a Notification. Then, call setStyle() with a style object and supply information corresponding to each template, as shown below.\n" +
            "\n" +
            "Add a large image";
    private int count = 0;
    private int limitcount = 0;
    private String message = "message";
    private int NOTIFICATION_ID = 237;
    private String KEY_NOTIFICATION_GROUP = "657";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sendPushNotificationToUser();
                samplePushNotification();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                  //  sendBundledNotifications(new RemoteNotification(extras));
                  //  sendMultipleNotifications();
                }


            }
        });
    }

    private void sendMultipleNotifications() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("New Message received");
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle("doUdo");

        // Add your All messages here or use Loop to generate messages

        inboxStyle.addLine("Messgare 1");
        inboxStyle.addLine("Messgare 2");
        inboxStyle.addLine("Messgare n");


        mBuilder.setStyle(inboxStyle);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
          Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pIntent);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setAutoCancel(true);
        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());
    }

/*
    private void sendBundledNotifications() {
        // only run this code if the device is running 23 or better
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<StatusBarNotification> groupedNotifications = new ArrayList<>();

            // step through all the active StatusBarNotifications and
            for (StatusBarNotification sbn : getNotificationManagerService().getActiveNotifications()) {
                // add any previously sent notifications with a group that matches our RemoteNotification
                // and exclude any previously sent stack notifications
                if (remoteNotification.getUserNotificationGroup() != null &&
                        remoteNotification.getUserNotificationGroup().equals(sbn.getNotification().getGroup()) &&
                        sbn.getId() != RemoteNotification.TYPE_STACK) {
                    groupedNotifications.add(sbn);
                }
            }

            // since we assume the most recent notification was delivered just prior to calling this method,
            // we check that previous notifications in the group include at least 2 notifications
            if (groupedNotifications.size() > 1) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                // use convenience methods on our RemoteNotification wrapper to create a title
                builder.setContentTitle(String.format("%s: %s", remoteNotification.getAppName(), remoteNotification.getErrorName()))
                        .setContentText(String.format("%d new activities", groupedNotifications.size()));

                // for every previously sent notification that met our above requirements,
                // add a new line containing its title to the inbox style notification extender
                NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();
                {
                    for (StatusBarNotification activeSbn : groupedNotifications) {
                        String stackNotificationLine = (String) activeSbn.getNotification().extras.get(NotificationCompat.EXTRA_TITLE);
                        if (stackNotificationLine != null) {
                            inbox.addLine(stackNotificationLine);
                        }
                    }

                    // the summary text will appear at the bottom of the expanded stack notification
                    // we just display the same thing from above (don't forget to use string
                    // resource formats!)
                    inbox.setSummaryText(String.format("%d new activities", groupedNotifications.size()));
                }
                builder.setStyle(inbox);

                // make sure that our group is set the same as our most recent RemoteNotification
                // and choose to make it the group summary.
                // when this option is set to true, all previously sent/active notifications
                // in the same group will be hidden in favor of the notifcation we are creating
                builder.setGroup(remoteNotification.getUserNotificationGroup())
                        .setGroupSummary(true);

                // if the user taps the notification, it should disappear after firing its content intent
                // and we set the priority to high to avoid Doze from delaying our notifications
                builder.setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                // create a unique PendingIntent using an integer request code.
                final int requestCode = (int) System.currentTimeMillis() / 1000;
                builder.setContentIntent(PendingIntent.getActivity(this, requestCode, DetailActivity.createIntent(this), PendingIntent.FLAG_ONE_SHOT));

                Notification stackNotification = builder.build();
                stackNotification.defaults = Notification.DEFAULT_ALL;

                // finally, deliver the notification using the group identifier as the Tag
                // and the TYPE_STACK which will cause any previously sent stack notifications
                // for this group to be updated with the contents of this built summary notification
                getNotificationManagerService().notify(remoteNotification.getUserNotificationGroup(), RemoteNotification.TYPE_STACK, stackNotification);
            }
        }

    }
*/

    private NotificationManager getNotificationManagerService() {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return nManager;
    }

    private void samplePushNotification() {
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alerts_empty);
        String msg = "Aravind";
       // if(msg)



        count++;
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
              /*  Notification.Builder builder = new Notification.Builder(this);
                builder.setContentTitle("Pyars");
                builder.setContentText("Notification from Pyar"+count);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setLargeIcon(bitmap);
                builder.setAutoCancel(true);
               // inboxStyle.setBigContentTitle("Enter Pyar Text");
               *//* inboxStyle.addLine("hi pyar users "+count);
                inboxStyle.addLine("hello"+count);*//*
                builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messages));*/

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Aravind")
                .setContentText("how are you?")
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messages))
                .build();
        if (nManager != null) {
            nManager.notify(getResources().getString(R.string.app_name), NOTIFICATION_ID, notification);
            count ++;
        }
        if (count > 4) {
            NOTIFICATION_ID++;
        }


    }

    private void sendPushNotificationToUser() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        PushNotificationPayload pushNotificationPayload = new PushNotificationPayload();
        pushNotificationPayload.setBody(message + count);
        pushNotificationPayload.setAcme_type("2");
        pushNotificationPayload.setTag("Aravind");
        pushNotificationPayload.setIcon("https://pccdn.pyar.com/pcmbr/8260192b/gl/efc9a4c5.jpg");
        pushNotificationPayload.setMessage("gvgjfg" + count);
        if (count == 4) {
            pushNotificationPayload.setTag("Tag" + String.valueOf(limitcount) + count);
            count = 0;
            limitcount++;
        }
        notificationUtils.showNotificationMessage(pushNotificationPayload, intent);
        count++;
    }
}
