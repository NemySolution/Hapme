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
    String message;
    String[] slicedMsg;

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
//        super.onPushReceive(context, intent);

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

            message = json.getString("alert");
            slicedMsg = message.split(",", 2);

            /*************** Send watch message in background ***************/
            client = getGoogleApiClient(context);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("MING MOBILE", "RUNNING");
                    client.blockingConnect(1000, TimeUnit.MILLISECONDS);
                    NodeApi.GetConnectedNodesResult result =
                            Wearable.NodeApi.getConnectedNodes(client).await();
                    List<Node> nodes = result.getNodes();
                    Log.e("MING MOBILE", Integer.toString(nodes.size()));

                    for (int i = 0; i < nodes.size(); i++) {
                        Log.e("MING MOBILE", nodes.get(i).getId() + ", " + nodes.get(i).getDisplayName());
                        nodeId = nodes.get(i).getId();
                        Wearable.MessageApi.sendMessage(client, nodeId, message, null);
                    }


                    Log.e("MING MOBILE", "RUNNING");
                    client.disconnect();
                }
            }).start();
            /****************************************************************/

            Toast.makeText(context, "Message: " + slicedMsg[0] + " RECEIVED!", Toast.LENGTH_SHORT).show();
            NotificationCompat.WearableExtender wearableExtender =
                    new NotificationCompat.WearableExtender()
                            .setHintHideIcon(true);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("HAPME")
                    .setContentText(message)
                    .extend(wearableExtender);

            notificationBuilder.setVibrate(hashMap.get(slicedMsg[1]));

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);

            // mId allows you to update the notification later on.
            notificationManager.notify(100, notificationBuilder.build());

        } catch (JSONException e) {
            Log.e("CUSTOM RECEIVER", "Push message json exception: " + e.getMessage());
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
