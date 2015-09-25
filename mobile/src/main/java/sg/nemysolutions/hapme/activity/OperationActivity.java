package sg.nemysolutions.hapme.activity;

/*************************************************
 * OperationActivity Page

/* This page is to allow users to view activity_operation
* details and to assure them that they are in
* an activity_operation.
*
* For Ground Commander, this page
* will let him able to MANAGE activity_operation details,
* commands and also broadcast it. Viewing of location
* of members are in consideration*/

//Key person: Yeekeng and Ming Sheng
/*************************************************/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SendCallback;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.entity.Command;

public class OperationActivity extends AppCompatActivity {

    private EditText et_opsName;
    private EditText et_callSign;
    private TextView tv_myo;
    private TextView tv_commandList;
    private ListView lw_addMember;
    private ArrayAdapter<String> membersAdapter;
    private ParseInstallation installation;
    private ParseObject currentOps;
    private Handler mHandler;
    private String opsId;
    private String opsName;
    private String callSign;
    private String deviceId;
    private String isMember = "true";
    private List<String> members = new ArrayList<>();
    private List<Command> commandList = new ArrayList<>();
    private LinkedList<String> capturedPoseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        // Auto Refresh
        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable, 5000);

        installation = ParseInstallation.getCurrentInstallation();
        deviceId = installation.getInstallationId();
        opsId = installation.getString("opsId");
        opsName = installation.getString("opsName");
        callSign = installation.getString("callSign");

        Intent intent = getIntent();
        isMember = intent.getStringExtra("isMember");

        lw_addMember = (ListView) findViewById(R.id.lw_addMember);
        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        tv_myo = (TextView) findViewById(R.id.tv_myo);
        tv_commandList = (TextView) findViewById(R.id.tv_commandList);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
        query.getInBackground(installation.getString("opsId"), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                currentOps = object;
                if (currentOps.getString("deviceId").equals(deviceId)) {
                    isMember = "false";
                }
                et_opsName.setText(opsName);
                et_callSign.setText(currentOps.getString("callSign"));
                setList();
                ParsePush.subscribeInBackground(currentOps.getString("opsName"));
                retrieveOperationMembers();
            }
        });

        /*
            Myo Device Start
            pre-requisite: User must be a Ground Commander
         */
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
        /*
            Myo Device End
         */

    }
    /********************* END OF ONCREATE ***********************/

    private void retrieveOperationMembers() {
        ParseQuery<ParseObject> memberQuery = ParseQuery.getQuery("Operation");
        memberQuery.getInBackground(installation.getString("opsId"), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {

                if (object.getList("members") != null) {
                    Log.e("MEMBERS callSign", callSign);
                    members = object.getList("members");
                    for(int i = 0; i < members.size(); i++) {
                        if (members.get(i).equals(callSign)) {
                            Log.e("MEMBERS", members.get(i));
                            members.set(i, callSign + " (You)");
                        }
                    }
                } else {
                    Log.e("MEMBERS null", "is null");
                    members = new ArrayList<>();
                }

                if (!members.contains(currentOps.getString("callSign") + " (Commander)")) {
                    members.add(0, currentOps.getString("callSign") + " (Commander)");
                }

                setList();
            }
        });
    }

    private void setList() {
        membersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, members);
        lw_addMember.setAdapter(membersAdapter);
    }

    /************** AUTO REFRESH MENU ****************/
    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            //retrieve the members in the ops
            retrieveOperationMembers();
            mHandler.postDelayed(m_Runnable, 5000);
        }
    };

//    public void refresh() {
//        retrieveOperationMembers();
//        mHandler.removeCallbacks(m_Runnable);
//    }
    /************** AUTO REFRESH END ****************/


    /*
        Myo Device start
        pre-requisite: User must be a Ground Commander
     */
    private DeviceListener mListener = new AbstractDeviceListener() {
        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            Toast.makeText(getApplicationContext(), "Myo Connected!", Toast.LENGTH_SHORT).show();

            // Disable myo button on page
//            bn_myo.setEnabled(false);

//            bn_myo.setText("Connected");
            tv_myo.setText("Myo connected");


            // Clear command list
            commandList.clear();

            // retrieve command list from parse
            getCommandListFromParse();
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            Toast.makeText(getApplicationContext(), "Myo Disconnected! Please connect to a Myo Device", Toast.LENGTH_SHORT).show();
            // Enable myo button on page
//            bn_myo.setEnabled(true);
//            bn_myo.setText("Disconnected");

            tv_myo.setText("Myo not connected");
            tv_commandList.setText("");
        }

        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
//            messageView.setText(myo.getArm() == Arm.LEFT ? R.string.arm_left : R.string.arm_right);
            tv_myo.setText("Myo Connected and Sync-ed");
            myo.lock();
            tv_commandList.setText("Myo Locked. Fist to Unlock.");
            capturedPoseList.clear();
        }

        // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            //messageView.setText("Unsync");
            tv_myo.setText("Myo Connected but Unsync-ed");
            tv_commandList.setText("");
            capturedPoseList.clear();
            myo.lock();
        }

        // onUnlock() is called whenever a synced Myo has been unlocked. Under the standard locking
        // policy, that means poses will now be delivered to the listener.
        @Override
        public void onUnlock(Myo myo, long timestamp) {
            //lockView.setText(R.string.unlocked);
            //tv_myo.setText("Myo connected, Unlocked");
        }

        // onLock() is called whenever a synced Myo has been locked. Under the standard locking
        // policy, that means poses will no longer be delivered to the listener.
        @Override
        public void onLock(Myo myo, long timestamp) {
            //lockView.setText(R.string.locked);
            //tv_myo.setText("Myo connected, Locked");
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
            switch (pose) {
                case UNKNOWN:
                    Log.e("ESMOND MYO MESSAGE", "unknown");
                    break;
                case REST:
                    Log.e("ESMOND MYO MESSAGE", "rest");
                    break;
                case DOUBLE_TAP:
                    Log.e("ESMOND MYO MESSAGE", "double tap");
                    // Compare pose list with command list
                    Boolean commandFound = false;
                    for (Command c : commandList) {
                        if (Arrays.equals(c.getGestureSeq().toArray(), capturedPoseList.toArray())) {
                            ParsePush push = new ParsePush();
                            push.setChannel(c.getOpsName());
                            push.setMessage(c.getCommandName() + "," + c.getVibrationSeq() + "," + c.getColor());
                            Log.e("ESMOND MYO MESSAGE", c.getCommandName() + "," + c.getVibrationSeq()+ "," + c.getColor());

                            Toast.makeText(getApplicationContext(), "Sending command: " + c.getCommandName(), Toast.LENGTH_SHORT).show();

                            push.sendInBackground(new SendCallback() {
                                public void done(ParseException e) {
                                    Toast.makeText(getApplicationContext(), "Command SENT!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            commandFound = true;
                            break;
                        }
                    }

                    // feedback to user by Toast
                    if (!commandFound) {
                        Toast.makeText(getApplicationContext(), "Command not found.", Toast.LENGTH_SHORT).show();
                    }
                    // clear captured list
                    capturedPoseList.clear();
                    tv_commandList.setText("Standby for Gestures");
                    break;
                case FIST:
                    Log.e("ESMOND MYO MESSAGE", "fist");
                    if (myo.isUnlocked()) {
                        myo.lock();
                        tv_commandList.setText("Myo Locked. Fist to Unlock.");
                    } else {
                        myo.unlock(Myo.UnlockType.HOLD);
                        tv_commandList.setText("Standby for Gestures");
                    }
                    capturedPoseList.clear();
                    break;
                case WAVE_IN:
                    //Toast.makeText(getApplicationContext(), "WAVE_IN", Toast.LENGTH_SHORT).show();
                    if (capturedPoseList.size() == 3) {
                        capturedPoseList.clear();
                    }
                    capturedPoseList.offer("WAVE_IN");
                    tv_commandList.setText(printCommandList());
                    break;
                case WAVE_OUT:
                    //Toast.makeText(getApplicationContext(), "WAVE_OUT", Toast.LENGTH_SHORT).show();
                    if (capturedPoseList.size() == 3) {
                        capturedPoseList.clear();
                    }
                    capturedPoseList.offer("WAVE_OUT");
                    tv_commandList.setText(printCommandList());
                    break;
                case FINGERS_SPREAD:
                    Log.e("ESMOND MYO MESSAGE", "fingers spread");
                    //Toast.makeText(getApplicationContext(), "FINGERS_SPREAD", Toast.LENGTH_SHORT).show();
                    if (capturedPoseList.size() == 3) {
                        capturedPoseList.clear();
                    }
                    capturedPoseList.offer("FINGERS_SPREAD");
                    tv_commandList.setText(printCommandList());
                    break;
            }

//            if (pose == Pose.FIST) {
//                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
//                // hold the poses without the Myo becoming locked.
//                myo.unlock(Myo.UnlockType.HOLD);
//                tv_commandList.setText("Perform Gestures to Send Commands");
//
//                // Notify the Myo that the pose has resulted in an action, in this case changing
//                // the text on the screen. The Myo will vibrate.
//                //myo.notifyUserAction();
//            } else {
//                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
//                // stay unlocked while poses are being performed, but lock after inactivity.
//                myo.unlock(Myo.UnlockType.TIMED);
//            }
        }
    };

    private String printCommandList() {
        return capturedPoseList.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();
    }

    private void getCommandListFromParse() {
        // Retrieve Command list from parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Command");
        query.whereEqualTo("opsName", opsName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject c : results) {
                    Command command = new Command();
                    command.setOpsName(c.getString("opsName"));
                    command.setCommandName(c.getString("commandName"));
                    command.setCommandID(c.getObjectId());
                    command.setGestureSeq(convertParseListToArrayList(c));
                    command.setVibrationSeq(c.getString("vibrationSeq"));
                    command.setColor(c.getString("color"));
                    commandList.add(command);
                }
            }
        });
    }

    private ArrayList<String> convertParseListToArrayList(ParseObject parseObject) {
        ArrayList<String> list = new ArrayList<>();
        for (Object o : parseObject.getList("gestureSeq")) {
            list.add(o.toString());
        }
        return list;
    }
    /*
        Myo Device end
     */

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (isMember == "false") {
            inflater.inflate(R.menu.menu_operation, menu);
        } else {
            inflater.inflate(R.menu.menu_operation_member, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_operation_member, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.myo:
                myo();
                return true;
            case R.id.editCmd:
                return true;
            case R.id.broadCastCmd:
                broadcast();
                return true;
            case R.id.viewMemLocation:
                return true;
            case R.id.endOps:
                endOps();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Please exit from the menu.", Toast.LENGTH_SHORT).show();
    }

    /******************* MENU FUNCTIONS *********************/
    public void broadcast() {
        Intent intent = new Intent(OperationActivity.this, CommandsActivity.class);

        if (currentOps.getString("deviceId").equals(deviceId)) {
            intent.putExtra("isCommander", "true");
        } else {
            intent.putExtra("isCommander", "false");
        }

        intent.putExtra("opsName", opsName);
        intent.putExtra("callSign", callSign);
        startActivity(intent);
    }

    public void endOps() {
        // if is the commander that is ending this activity_operation, delete the commands first, followed by the activity_operation.
        if (currentOps.getString("deviceId").equals(deviceId)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Command");
            query.whereEqualTo("opsId", opsId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> results, ParseException e) {
                    for (ParseObject c : results) {
                        c.deleteInBackground();
                    }
                    currentOps.deleteInBackground();
                }
            });
        } else {
            members = currentOps.getList("members");
            members.remove(callSign + " (You)");
            currentOps.put("members", members);
            currentOps.saveInBackground();
        }

        ParsePush.unsubscribeInBackground(currentOps.getString("opsName"));
        mHandler.removeCallbacks(m_Runnable);
        finish();
    }

    public void myo() {
        // Launch the ScanActivity to scan for Myos to connect to
        Intent intent = new Intent(getBaseContext(), ScanActivity.class);
        startActivity(intent);
    }
    /******************* END MENU FUNCTIONS *********************/

}
