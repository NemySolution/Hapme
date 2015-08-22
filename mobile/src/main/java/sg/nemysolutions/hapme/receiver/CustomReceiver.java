package sg.nemysolutions.hapme.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.activity.OperationActivity;

public class CustomReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        //super.onPushReceive(context, intent);

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            String message = json.getString("alert");

            Toast.makeText(context, "Message: " + message + " RECEIVED!", Toast.LENGTH_SHORT).show();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            // [how long to wait before vibrating, vibrate ,sleep , vibrate, sleep ..]
                            .setVibrate(new long[] { 0, 2000, 1000, 2000, 1000, 2000, 3000 })
                            .setContentTitle("ALERT!")
                            .setContentText(message);
            // Creates an explicit intent for an Activity in your app
                        Intent resultIntent = new Intent(context, OperationActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
                        stackBuilder.addParentStack(OperationActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(100, mBuilder.build());

        } catch (JSONException e) {
            Log.e("hello", "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }
}
