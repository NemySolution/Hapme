package sg.nemysolutions.hapme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MyService extends WearableListenerService {

    String nodeId;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        nodeId = messageEvent.getSourceNodeId();
        Log.e("MING WEAR", messageEvent.getPath());
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.putExtra("msg", messageEvent.getPath());
        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);
    }

}
