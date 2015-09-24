package sg.nemysolutions.hapme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // wake the screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        HashMap<String, long[]> hashMap = new HashMap<>();
        hashMap.put("longAlert", new long[] {0, 10000});
        hashMap.put("mediumAlert", new long[] {0, 500, 250, 500, 250, 500, 250});
        hashMap.put("shortAlert", new long[] {0, 200, 100, 200, 100, 200, 100});
        hashMap.put("heartbeat", new long[] {0, 200, 100, 200, 1000, 200, 100, 200, 1000, 200, 100, 200, 1000, 200, 100, 200});
        hashMap.put("shave", new long[] {0,100,200,100,100,100,100,100,200,100,500,100,225,100});
        hashMap.put("triangle", new long[] {0,150,50,75,50,75,50,150,50,75,50,75,50,300,1000, 150,50,75,50,75,50,150,50,75,50,75,50,300});
        hashMap.put("sos", new long[]{0, 200, 200, 200, 200, 200, 500, 500, 200, 500, 200, 500, 500, 200, 200, 200, 200, 200, 1000, 200, 200, 200, 200, 200, 500, 500, 200, 500, 200, 500, 500, 200, 200, 200, 200, 200});
        hashMap.put("location", new long[] {0, 500});
        hashMap.put("finalFantasy", new long[]{0, 50, 100, 50, 100, 50, 100, 400, 100, 300, 100, 350, 50, 200, 100, 100, 50, 600});
        hashMap.put("starWars", new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500});
        hashMap.put("special", new long[]{0, 250, 200, 250, 150, 150, 75, 150, 75, 150});

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        Intent intent = getIntent();
        String[] msg = intent.getStringExtra("msg").split(",");
        mTextView.setText(msg[0]);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final int indexInPatternToRepeat = -1;
        vibrator.vibrate(hashMap.get(msg[1]), indexInPatternToRepeat);

    }

}
