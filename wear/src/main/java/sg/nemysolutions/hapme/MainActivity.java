package sg.nemysolutions.hapme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends Activity {

    HashMap<String, long[]> hashMap;
    private TextView mTextView;
    private TextView tv_senderCallSign;
    private RelativeLayout layout;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // wake the screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        hashMap = new HashMap<>();
        hashMap.put("LongAlert", new long[] {0, 3000, 500, 3000, 500, 3000, 500, 3000});
        hashMap.put("MediumAlert", new long[] {0, 500, 250, 500, 250, 500, 250, 500});
        hashMap.put("ShortAlert", new long[] {0, 200, 100, 200, 100, 200, 100});
        hashMap.put("Heartbeat", new long[] {0, 200, 100, 200, 1000, 200, 100, 200, 1000, 200, 100, 200, 1000, 200, 100, 200});
        hashMap.put("Shave", new long[] {0,100,200,100,100,100,100,100,200,100,500,100,225,100});
        hashMap.put("Triangle", new long[] {0,150,50,75,50,75,50,150,50,75,50,75,50,300,1000, 150,50,75,50,75,50,150,50,75,50,75,50,300});
        hashMap.put("SOS", new long[]{0, 200, 200, 200, 200, 200, 500, 500, 200, 500, 200, 500, 500, 200, 200, 200, 200, 200, 1000, 200, 200, 200, 200, 200, 500, 500, 200, 500, 200, 500, 500, 200, 200, 200, 200, 200});
        hashMap.put("FinalFantasy", new long[] {0,50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600});
        hashMap.put("StarWars", new long[] {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500});
        hashMap.put("Special", new long[] {0, 250, 200, 250, 150, 150, 75, 150, 75, 150});
        hashMap.put("Location", new long[] {0, 500});
        hashMap.put("Backup", new long[] {0, 10000});

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                layout = (RelativeLayout) stub.findViewById(R.id.layout);
                mTextView = (TextView) stub.findViewById(R.id.text);
                tv_senderCallSign = (TextView) stub.findViewById(R.id.senderCallSign);

                Intent intent = getIntent();
                msg = intent.getStringExtra("msg");

                Vibrator vibrator2 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                final int indexInPatternToRepeat2 = -1;
                vibrator2.vibrate(new long[] {0, 10000}, indexInPatternToRepeat2);

                if (msg != null) {
                    String[] slicedMsg = msg.split(",", 4);
                    String bg_color = slicedMsg[3];
                    // Change the font color if background has the same contrast
                    if (bg_color.equals("cyan") || bg_color.equals("yellow")) {
                        tv_senderCallSign.setTextColor(Color.parseColor("black"));
                        mTextView.setTextColor(Color.parseColor("black"));
                    }
                    tv_senderCallSign.setText(slicedMsg[0]);
                    mTextView.setText(slicedMsg[1]);
                    layout.setBackgroundColor(Color.parseColor(slicedMsg[3]));
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    final int indexInPatternToRepeat = -1;
                    vibrator.vibrate(hashMap.get(slicedMsg[2]), indexInPatternToRepeat);
                }
            }
        });

    }
    /*********** ONCREATE ENDS **************/

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MING SHENG", "WATCH RESTARTED");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        msg = data.getStringExtra("msg");
        Log.e("MING SHENG", "WATCH RESUMED" + msg);
        if (msg != null) {
            String[] slicedMsg = msg.split(",", 4);
            String bg_color = slicedMsg[3];
            // Change the font color if background has the same contrast
            if (bg_color.equals("cyan") || bg_color.equals("yellow")) {
                tv_senderCallSign.setTextColor(Color.parseColor("black"));
                mTextView.setTextColor(Color.parseColor("black"));
            }
            tv_senderCallSign.setText(slicedMsg[0]);
            mTextView.setText(slicedMsg[1]);
            layout.setBackgroundColor(Color.parseColor(slicedMsg[3]));
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            final int indexInPatternToRepeat = -1;
            vibrator.vibrate(hashMap.get(slicedMsg[2]), indexInPatternToRepeat);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MING SHENG", "WATCH RESUMED" + msg);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MING SHENG", "WATCH STARTED");
        Log.e("MING SHENG", "WATCH STARTED" + msg);
    }

}
