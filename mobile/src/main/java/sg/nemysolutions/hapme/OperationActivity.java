package sg.nemysolutions.hapme;

/**************** OperationActivity Page ******************/
/* This page is to allow users to view operation
* details and to assure them that they are in
* an operation.
*
* For Ground Commander, this page
* will let him able to MANAGE operation details,
* commands and also broadcast it. Viewing of location
* of members are in consideration*/

//Key person: Yeekeng and Ming Sheng
/*************************************************/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import sg.nemysolutions.hapme.entity.Command;

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
    String callSign;
    List<String> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation);

        Intent intent = getIntent();
        opsId = intent.getStringExtra("opsId");
        callSign = intent.getStringExtra("callSign");

        final String deviceId = ParseInstallation.getCurrentInstallation().getString("installationId");

        ParsePush.subscribeInBackground(opsId);

        lw_addMember = (ListView) findViewById(R.id.lw_addMember);
        bn_refresh = (Button) findViewById(R.id.bn_refresh);
        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        bn_broadcast = (Button) findViewById(R.id.bn_broadcast);
        bn_endOps = (Button) findViewById(R.id.bn_endOps);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
        query.getInBackground(opsId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                currentOps = object;
                // update the members
                if (object.getList("members") != null) {
                    members = object.getList("members");
                }
                members.add(et_callSign.getText().toString());
                object.put("members", members);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            et_opsName.setText(currentOps.getString("opsName"));
                            et_callSign.setText(currentOps.getString("callSign"));
                        } else {
                            Log.e("ERROR", "Cannot retrieve operation!!");
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
                intent.putExtra("opsId", opsId);
                startActivity(intent);
            }
        });

        bn_endOps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if is the commander that is ending this operation, delete the commands first, followed by the operation.
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
                }
                ParsePush.unsubscribeInBackground(opsId);
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
                membersList.add(0, object.getString("callSign") + " (Commander)");
                setList();
            }
        });
    }

    private void setList() {
        if(membersList != null) {
            if(membersList.size() != 0) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, membersList);
                lw_addMember.setAdapter(arrayAdapter);
            }
        }
    }
}
