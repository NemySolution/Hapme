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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class OperationActivity extends AppCompatActivity {

    EditText et_opsName;
    EditText et_callSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation);

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
        query.whereEqualTo("opsName", "opDick");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject c : results) {
                    et_opsName.setText(c.getString("opsName"));
                    et_callSign.setText(c.getString("callSign"));
                }

            }
        });

        // Query list of Members in the channel
        ParseQuery<ParseObject> membersQuery = ParseQuery.getQuery("Installation");
        query.whereEqualTo("Installation", "opDick");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject c : results) {
                    Log.e("Members", c.toString());
                }

            }
        });

    }

}
