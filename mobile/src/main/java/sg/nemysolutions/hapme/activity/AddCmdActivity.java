package sg.nemysolutions.hapme.activity;

/**************** AddCmdActivity command Page ******************/
/* This page is to allow users to add a command,
* attach a myo gesture and vibration sequence
* to it. Please ensure that they are able to name
* the commands also.*/

//Key person: Esmond*/
/********************************************/

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.entity.Command;


public class AddCmdActivity extends AppCompatActivity {

    TextView lockView;
    TextView messageView;
    TextView commandView;
    Button bn_sync;
    Button bn_waveIn;
    Button bn_waveOut;
    Button bn_fingerSpread;
    Button bn_clear;
    Button bn_done;
    EditText et_gesture1;
    EditText et_gesture2;
    EditText et_gesture3;
    EditText et_commandName;

    private LinkedList<Pose> capturedPoseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cmd);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        lockView = (TextView) findViewById(R.id.lock_state);
        commandView = (TextView) findViewById(R.id.tv_command);
        messageView = (TextView) findViewById(R.id.message);

        bn_sync = (Button) findViewById(R.id.bn_sync);
        bn_waveIn = (Button) findViewById(R.id.bn_waveIn);
        bn_waveOut = (Button) findViewById(R.id.bn_waveOut);
        bn_fingerSpread = (Button) findViewById(R.id.bn_fingerSpread);
        bn_clear = (Button) findViewById(R.id.bn_clear);
        bn_done = (Button) findViewById(R.id.bn_done);

        et_gesture1 = (EditText) findViewById(R.id.et_gesture1);
        et_gesture2 = (EditText) findViewById(R.id.et_gesture2);
        et_gesture3 = (EditText) findViewById(R.id.et_gesture3);

        et_commandName = (EditText) findViewById(R.id.et_commandName);

        bn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the ScanActivity to scan for Myos to connect to
                Intent intent = new Intent(getBaseContext(), ScanActivity.class);
                startActivity(intent);
            }
        });

        bn_waveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPose("WAVE_IN");
            }
        });

        bn_waveOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPose("WAVE_OUT");
            }
        });

        bn_fingerSpread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPose("FINGERS_SPREAD");
            }
        });

        bn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_gesture1.setText("");
                et_gesture2.setText("");
                et_gesture3.setText("");
            }
        });

        bn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doneValue = et_commandName.getText().toString();
                if(doneValue.isEmpty()){
                    et_commandName.setError("Command name required");
                }
                else{
                    List<String> gestureList = Arrays.asList(et_gesture1.getText().toString(), et_gesture2.getText().toString(), et_gesture3.getText().toString());

                    //set Command object
                    Command cmd = new Command();
                    cmd.setCommandName(et_commandName.getText().toString());
                    cmd.setGestureSeq(gestureList);
                    cmd.setVibrationSeq(gestureList); // dummy list. Ming Sheng will change when he is done with watch

                    Intent intent = new Intent();
                    intent.putExtra("command", cmd);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

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

        // Initialise pose list to capture pose
        capturedPoseList = new LinkedList<>();
    }//end of onCreate

    private void setPose(String pose) {
        if (et_gesture1.getText().toString().equals("")) {
            et_gesture1.setText(pose);
        } else if (et_gesture2.getText().toString().equals("")) {
            et_gesture2.setText(pose);
        } else if (et_gesture3.getText().toString().equals("")) {
            et_gesture3.setText(pose);
        } else {
            Log.e("Error", "All 3 gestures are filled.");
        }
    }

    // this method reads the captured pose and return numeric command
    private int getCommand(LinkedList<Pose> poseLinkedList) {
        lockView.setText(poseLinkedList.toString());
        int command = 0;
        for (int i = 0; i < poseLinkedList.size(); i++) {

            switch (poseLinkedList.get(i)) {
                case WAVE_IN:
                    command += 1;
                    break;
                case WAVE_OUT:
                    command += 2;
                    break;
                case FINGERS_SPREAD:
                    command += 3;
                    break;
            }
        }
        poseLinkedList.clear();
        return command;
    }

    private DeviceListener mListener = new AbstractDeviceListener() {
        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            messageView.setText("Myo Connected!");
            messageView.setTextColor(Color.BLUE);
            bn_sync.setEnabled(false);
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            messageView.setText("Myo Disconnected! Please connect to a Myo Device");
            messageView.setTextColor(Color.RED);
            messageView.setTextColor(Color.RED);
            bn_sync.setEnabled(true);
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
                    commandView.setText(String.format("Command %d", getCommand(capturedPoseList)));
                    break;
                case FIST:
                    messageView.setText(getString(R.string.pose_fist));
                    commandView.setText("Pose not captured !");
                    break;
                case WAVE_IN:
                    messageView.setText(getString(R.string.pose_wavein));
                    capturedPoseList.offer(pose);
                    commandView.setText("Captured pose: " + getString(R.string.pose_wavein));
                    break;
                case WAVE_OUT:
                    messageView.setText(getString(R.string.pose_waveout));
                    capturedPoseList.offer(pose);
                    commandView.setText("Captured pose: " + getString(R.string.pose_waveout));
                    break;
                case FINGERS_SPREAD:
                    messageView.setText(getString(R.string.pose_fingersspread));
                    capturedPoseList.offer(pose);
                    commandView.setText("Captured pose: " + getString(R.string.pose_fingersspread));
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


}
