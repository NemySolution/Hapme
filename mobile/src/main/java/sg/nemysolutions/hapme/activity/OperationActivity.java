package sg.nemysolutions.hapme.activity;

/**************** OperationActivity Page ******************/
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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.entity.Command;

public class OperationActivity extends AppCompatActivity {

    private EditText et_opsName;
    private EditText et_callSign;

    private Button bn_broadcast;
    private Button bn_endOps;
    private Button bn_refresh;
    private Button bn_myo;

    private ListView lw_addMember;

    private ArrayAdapter<String> membersAdapter;
    private ParseInstallation installation;
    private ParseObject currentOps;

    private Handler mHandler;

    private String opsId;
    private String opsName;
    private String callSign;

    private List<String> members = new ArrayList<>();
    private List<Command> commandList = new ArrayList<>();
    private LinkedList<String> capturedPoseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);






        //auto refresh
        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,10000);

        installation = ParseInstallation.getCurrentInstallation();
        final String deviceId = installation.getInstallationId();
        opsId = installation.getString("opsId");
        opsName = installation.getString("opsName");
        callSign = installation.getString("callSign");

        lw_addMember = (ListView) findViewById(R.id.lw_addMember);
        bn_refresh = (Button) findViewById(R.id.bn_refresh);
        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        bn_broadcast = (Button) findViewById(R.id.bn_broadcast);
        bn_endOps = (Button) findViewById(R.id.bn_endOps);

        et_opsName.setText(opsName);
        et_callSign.setText(callSign);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
        query.getInBackground(installation.getString("opsId"), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                currentOps = object;

                setList();

                ParsePush.subscribeInBackground(currentOps.getString("opsName"));

                retrieveOperationMembers();
            }
        });

        bn_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OperationActivity.this, CommandsActivity.class);

                if (currentOps.getString("deviceId").equals(deviceId)) {
                    intent.putExtra("isCommander", "true");
                } else {
                    intent.putExtra("isCommander", "false");
                }

                intent.putExtra("opsName", opsName);
                startActivity(intent);
            }
        });

        bn_endOps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    members.remove(callSign);
                    currentOps.put("members", members);
                    currentOps.saveInBackground();
                }

                ParsePush.unsubscribeInBackground(currentOps.getString("opsName"));
                mHandler.removeCallbacks(m_Runnable);
                finish();
            }
        });

        bn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveOperationMembers();
                mHandler.removeCallbacks(m_Runnable);
            }
        });

        /*
            Myo Device Start
            pre-requisite: User must be a Ground Commander
         */
        // Create Button to check for myo connectivity
        bn_myo = (Button) findViewById(R.id.bn_myo);

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

        bn_myo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the ScanActivity to scan for Myos to connect to
                Intent intent = new Intent(getBaseContext(), ScanActivity.class);
                startActivity(intent);
            }
        });
        /*
            Myo Device End
         */






    }

    private void retrieveOperationMembers() {
        ParseQuery<ParseObject> memberQuery = ParseQuery.getQuery("Operation");
        memberQuery.getInBackground(installation.getString("opsId"), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object.getList("members") != null && object.getList("members").size() != 0) {
                    members = object.getList("members");
                } else {
                    members = new ArrayList<String>();
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

    /******************************
     * runnable function to do the auto refresh
     */
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //retrieve the members in the ops
            retrieveOperationMembers();
//            Toast.makeText(getBaseContext(),"runn",Toast.LENGTH_SHORT).show();
//            Log.e("runnnnnnnnnnnnnn","runnnnnnnnnnnn");
            mHandler.postDelayed(m_Runnable, 10000);

        }

    };

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
            bn_myo.setEnabled(false);
            bn_myo.setText("Connected");

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
            bn_myo.setEnabled(true);
            bn_myo.setText("Disconnected");
        }

        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
//            messageView.setText(myo.getArm() == Arm.LEFT ? R.string.arm_left : R.string.arm_right);
        }

        // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            //messageView.setText("Unsync");
        }

        // onUnlock() is called whenever a synced Myo has been unlocked. Under the standard locking
        // policy, that means poses will now be delivered to the listener.
        @Override
        public void onUnlock(Myo myo, long timestamp) {
            //lockView.setText(R.string.unlocked);
        }

        // onLock() is called whenever a synced Myo has been locked. Under the standard locking
        // policy, that means poses will no longer be delivered to the listener.
        @Override
        public void onLock(Myo myo, long timestamp) {
            //lockView.setText(R.string.locked);
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
            switch (pose) {
                case UNKNOWN:
                    break;
                case REST:
                    break;
                case DOUBLE_TAP:
                     // Compare pose list with command list
                    Boolean commandFound = false;
                    for (Command c: commandList) {
                        if (Arrays.equals(c.getGestureSeq().toArray(), capturedPoseList.toArray())) {
                            ParsePush push = new ParsePush();
                            push.setChannel(c.getOpsName());
                            push.setMessage(c.getCommandName());
                            push.sendInBackground();

                            Toast.makeText(getApplicationContext(), "Command: " + c.getCommandName() + " SENT!", Toast.LENGTH_SHORT).show();
                            commandFound = true;
                            break;
                        }
                    }

                    // feedback to user by Toast
                    if (commandFound == false) {
                        Toast.makeText(getApplicationContext(), "Command not found.", Toast.LENGTH_SHORT).show();
                    }

                    // clear captured list
                    capturedPoseList.clear();
                    break;
                case FIST:
                    Toast.makeText(getApplicationContext(), "FIST", Toast.LENGTH_SHORT).show();
                    break;
                case WAVE_IN:
                    Toast.makeText(getApplicationContext(), "WAVE_IN", Toast.LENGTH_SHORT).show();
                    capturedPoseList.offer("WAVE_IN");
                    break;
                case WAVE_OUT:
                    Toast.makeText(getApplicationContext(), "WAVE_OUT", Toast.LENGTH_SHORT).show();
                    capturedPoseList.offer("WAVE_OUT");
                    break;
                case FINGERS_SPREAD:
                    Toast.makeText(getApplicationContext(), "FINGERS_SPREAD", Toast.LENGTH_SHORT).show();
                    capturedPoseList.offer("FINGERS_SPREAD");
                    break;
            }

            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
                // hold the poses without the Myo becoming locked.
                myo.unlock(Myo.UnlockType.HOLD);

                // Notify the Myo that the pose has resulted in an action, in this case changing
                // the text on the screen. The Myo will vibrate.
                //myo.notifyUserAction();
            } else {
                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
                // stay unlocked while poses are being performed, but lock after inactivity.
                myo.unlock(Myo.UnlockType.TIMED);
            }
        }
    };

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
                    commandList.add(command);
                }
            }
        });
    }

    private ArrayList<String> convertParseListToArrayList(ParseObject parseObject) {
        ArrayList<String> list = new ArrayList<String>();
        for (Object o : parseObject.getList("gestureSeq")) {
            list.add(o.toString());
        }
        return list;
    }
    /*
        Myo Device end
     */





    //actionbar stuff

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_operation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.endOps:

                return true;
//            case R.id.action_compose:
//                composeMessage();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }









}
