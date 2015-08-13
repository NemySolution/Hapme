package sg.nemysolutions.hapme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            String message = json.getString("alert");

            Toast.makeText(context, "Message: " + message + " RECEIVED!", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.e("hello", "Push message json exception: " + e.getMessage());
        }
    }

}