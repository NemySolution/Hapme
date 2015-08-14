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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import sg.nemysolutions.hapme.R;

public class OperationActivity extends AppCompatActivity {

    EditText et_opsName;
    EditText et_callSign;
    Button bn_broadcast;
    Button bn_endOps;
    ParseObject currentOps;
    ListView lw_addMember;
    List<String> membersList;
    Button bn_refresh;
    String opsId;
    String opsName;
    String callSign;
    List<String> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
        query.getInBackground(opsId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {

                // store the object to update later
                currentOps = object;

                // subscribe
                ParsePush.subscribeInBackground(object.getString("opsName"));

                // get the list of members and update it with current user to parse
                if (object.getList("members") != null) {
                    members = object.getList("members");
                    if (!members.contains(callSign)) {
                        members.add(callSign);
                    }
                } else {
                    members.add(callSign);
                }

                object.put("members", members);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            et_opsName.setText(currentOps.getString("opsName"));
                            et_callSign.setText(currentOps.getString("callSign"));
                        } else {
                            Log.e("ERROR", "Cannot retrieve activity_operation!!");
                            finish();
                        }
                    }
                });

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
                    membersList = currentOps.getList("members");
                    membersList.remove(callSign);
                    currentOps.put("members", membersList);
                    currentOps.saveInBackground();
                }
                ParsePush.unsubscribeInBackground(currentOps.getString("opsName"));
                finish();
            }
        });


        bn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabMembers();
            }
        });

        grabMembers();
    }

    private void grabMembers() {
        ParseQuery<ParseObject> memberQuery = ParseQuery.getQuery("Operation");
        memberQuery.getInBackground(opsId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                membersList = object.getList("members");
                if (membersList == null) {
                    membersList = new ArrayList<>();
                }
//                membersList.set(0, membersList.get(0) +  " (Commander)");
                setList();
            }
        });
    }

    private void setList() {
        if (membersList != null) {
            if (membersList.size() != 0) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, membersList);
                lw_addMember.setAdapter(arrayAdapter);
            }
        }
    }

}
