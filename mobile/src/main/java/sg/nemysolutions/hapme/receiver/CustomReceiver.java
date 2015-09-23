package sg.nemysolutions.hapme.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomReceiver extends ParsePushBroadcastReceiver {

    GoogleApiClient client;
    String nodeId;

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }


    @Override
    protected void onPushReceive(Context context, Intent intent) {
//        super.onPushReceive(context, intent);

        client = getGoogleApiClient(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("MING MOBILE", "RUNNING");
                client.blockingConnect(3000, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                Log.e("MING MOBILE", Integer.toString(nodes.size()));
                if (nodes.size() > 0) {
                    Log.e("MING MOBILE", nodes.get(0).getId() + ", " + nodes.get(0).getDisplayName());
                    Log.e("MING MOBILE", nodes.get(1).getId() + ", " + nodes.get(1).getDisplayName());
                    nodeId = nodes.get(1).getId();

                }
                Wearable.MessageApi.sendMessage(client, nodeId, "whatsup", null);
                Log.e("MING MOBILE", "RUNNING");
                client.disconnect();
            }
        }).start();

        HashMap<String, long[]> hashMap = new HashMap<>();
        hashMap.put("longAlert", new long[] {0, 10000});
        hashMap.put("mediumAlert", new long[] {0, 500, 250, 500, 250, 500, 250});
        hashMap.put("shortAlert", new long[] {0, 200, 100, 200, 100, 200, 100});
        hashMap.put("heartbeat", new long[] {0, 200, 100, 200, 1000, 200, 100, 200, 1000, 200, 100, 200, 1000, 200, 100, 200});
        hashMap.put("shave", new long[] {0,100,200,100,100,100,100,100,200,100,500,100,225,100});
        hashMap.put("triangle", new long[] {0,150,50,75,50,75,50,150,50,75,50,75,50,300,1000, 150,50,75,50,75,50,150,50,75,50,75,50,300});
        hashMap.put("sos", new long[]{0, 200, 200, 200, 200, 200, 500, 500, 200, 500, 200, 500, 500, 200, 200, 200, 200, 200, 1000, 200, 200, 200, 200, 200, 500, 500, 200, 500, 200, 500, 500, 200, 200, 200, 200, 200});
        hashMap.put("location", new long[] {0, 500});
        hashMap.put("finalFantasy", new long[] {0,50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600});
        hashMap.put("starWars", new long[] {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500});
        hashMap.put("special", new long[] {0, 250, 200, 250, 150, 150, 75, 150, 75, 150});

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            String message = json.getString("alert");
            String[] splitedMsg = message.split(",", 2);

            Toast.makeText(context, "Message: " + splitedMsg[0] + " RECEIVED!", Toast.LENGTH_SHORT).show();
            NotificationCompat.WearableExtender wearableExtender =
                    new NotificationCompat.WearableExtender()
                            .setHintHideIcon(true);


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("ALERT!")
                    .setContentText(message)
                    .extend(wearableExtender);

            notificationBuilder.setVibrate(hashMap.get(splitedMsg[1]));

//            // Creates an explicit intent for an Activity in your app
//            Intent resultIntent = new Intent(context, OperationActivity.class);
//
//            // The stack builder object will contain an artificial back stack for the
//            // started Activity.
//            // This ensures that navigating backward from the Activity leads out of
//            // your application to the Home screen.
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//            // Adds the back stack for the Intent (but not the Intent itself)
//            stackBuilder.addParentStack(OperationActivity.class);
//            // Adds the Intent that starts the Activity to the top of the stack
//            stackBuilder.addNextIntent(resultIntent);
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(
//                            0,
//                            PendingIntent.FLAG_UPDATE_CURRENT
//                    );
//            mBuilder.setContentIntent(resultPendingIntent);

//            NotificationManagerCompat mNotificationManager =
//                    NotificationManagerCompat.from(context);

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);

//            NotificationManager mNotificationManager =
//                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//            // mId allows you to update the notification later on.
            notificationManager.notify(100, notificationBuilder.build());


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
