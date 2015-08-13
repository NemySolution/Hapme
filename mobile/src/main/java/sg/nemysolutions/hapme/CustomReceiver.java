package sg.nemysolutions.hapme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            String message = json.getString("alert");

            Toast.makeText(context, "Message: " + message + " RECEIVED!", Toast.LENGTH_SHORT).show();

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
