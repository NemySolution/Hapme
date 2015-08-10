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
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.List;

public class OperationActivity extends AppCompatActivity {

    EditText et_opsName;
    EditText et_callSign;
    Button bn_broadcast;
    Button bn_endOps;
    ParseObject currentOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation);

        Intent intent = getIntent();
        final String opsId = intent.getStringExtra("opsId");

        final String deviceId = ParseInstallation.getCurrentInstallation().getString("installationId");

        ParsePush.subscribeInBackground(opsId);

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        bn_broadcast = (Button) findViewById(R.id.bn_broadcast);
        bn_endOps = (Button) findViewById(R.id.bn_endOps);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
        query.getInBackground(opsId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    currentOps = object;
                    et_opsName.setText(object.getString("opsName"));
                    et_callSign.setText(object.getString("callSign"));

                } else {
                    Log.e("ERROR", "Cannot retrieve operation!!");
                    finish();
                }
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
                if(currentOps.getString("deviceId").equals(deviceId)) {
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

    }

}
