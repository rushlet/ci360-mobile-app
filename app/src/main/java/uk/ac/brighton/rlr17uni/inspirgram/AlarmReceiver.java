package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by rushlet on 20/03/2018.
 */

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent received = intent;
        int scenario = received.getIntExtra("NOTIFICATION_ID", 0);


        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        switch (scenario) {
            case 1: {
                builder.setContentTitle("Testing").setContentText("testing 123...");
            }
            case 2: {
                builder.setContentTitle("Challenge waiting").setContentText("Don't forget to upload your photos...");
            }
            case 3: {
                builder.setContentTitle("Last chance!").setContentText("Challenge will expire tomorrow, upload your photos now");
            }
            case 4: {
                builder.setContentTitle("New Challenge Set").setContentText("Open to find out more...");
            }
            default: {
                break;
            }
        }
        Notification notification = builder
                .setSmallIcon(R.drawable.home_inspiration)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}