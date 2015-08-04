package nemysolutions.sg.hapme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Hub;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    /*
        Myo Section Start
     */
    private TextView lockView;
    private TextView messageView;
    /*
        Myo Section End
     */

    private DeviceListener mListener = new AbstractDeviceListener() {
        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            messageView.setText("Myo Connected!");
            messageView.setTextColor(Color.CYAN);
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            messageView.setText("Myo Disconnected!");
            messageView.setTextColor(Color.RED);
        }

        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            messageView.setText(myo.getArm() == Arm.LEFT ? R.string.arm_left : R.string.arm_right);
        }

        // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            messageView.setText("Unsync");
        }

        // onUnlock() is called whenever a synced Myo has been unlocked. Under the standard locking
        // policy, that means poses will now be delivered to the listener.
        @Override
        public void onUnlock(Myo myo, long timestamp) {
            lockView.setText(R.string.unlocked);
        }

        // onLock() is called whenever a synced Myo has been locked. Under the standard locking
        // policy, that means poses will no longer be delivered to the listener.
        @Override
        public void onLock(Myo myo, long timestamp) {
            lockView.setText(R.string.locked);
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
            switch (pose) {
                case UNKNOWN:
                    messageView.setText(getString(R.string.pose_unknown));
                    break;
                case REST:
                    messageView.setText(getString(R.string.pose_rest));
                    break;
                case DOUBLE_TAP:
                    messageView.setText(getString(R.string.pose_doubletap));
                    break;
                case FIST:
                    messageView.setText(getString(R.string.pose_fist));
                    break;
                case WAVE_IN:
                    messageView.setText(getString(R.string.pose_wavein));
                    break;
                case WAVE_OUT:
                    messageView.setText(getString(R.string.pose_waveout));
                    break;
                case FINGERS_SPREAD:
                    messageView.setText(getString(R.string.pose_fingersspread));
                    break;
            }

            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
                // hold the poses without the Myo becoming locked.
                myo.unlock(Myo.UnlockType.HOLD);

                // Notify the Myo that the pose has resulted in an action, in this case changing
                // the text on the screen. The Myo will vibrate.
                myo.notifyUserAction();
            } else {
                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
                // stay unlocked while poses are being performed, but lock after inactivity.
                myo.unlock(Myo.UnlockType.TIMED);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "0RgU1BAusmGZiIkOFDVucZcEtbCHPiJx479CDcKG", "GhyTkHlqG22YziVd7fbP8YNTYK6wbmrcwF99yM5G");

        /*
            Myo Section Start
         */
        // two text views to view lock state and pose command
        lockView = (TextView) findViewById(R.id.lock_state);
        messageView = (TextView) findViewById(R.id.message);
        messageView.setText("Please connect to a Myo Device");
        messageView.setTextColor(Color.RED);

        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this)) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);
        // Disable usage data being sent to Thalmic
        hub.setSendUsageData(false);
        // Using this policy means Myo will be locked until the user performs the unlock pose. This is the default policy.
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
        /*
            Myo Section End
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.myo_settings) {
            /*
                Myo Section Start
             */
            // Launch the ScanActivity to scan for Myos to connect to.
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
            /*
                Myo Section End
             */
        }

        return super.onOptionsItemSelected(item);
    }
}