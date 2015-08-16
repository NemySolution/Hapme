package sg.nemysolutions.hapme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.utilities.ParseUtils;

/**************** MainActivity ******************/
/* A "login" page for all the users (GC & members)
* for members to join_ops an activity_operation*/

//Key person: Yee Keng & Ming Sheng
/*********************************************/

public class JoinOpsActivity extends AppCompatActivity {

    private EditText et_opsName;
    private EditText et_callSign;
    private EditText et_secretKey;
    private Button bn_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_ops);

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        et_secretKey = (EditText) findViewById(R.id.et_secretKey);

        bn_join = (Button) findViewById(R.id.bn_join);

        bn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_opsName.getText().toString().equals("") || et_secretKey.getText().toString().equals("")) {
                    Toast.makeText(JoinOpsActivity.this, "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!", Toast.LENGTH_LONG).show();
                } else {
                    // initiate joining of activity_operation
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
                    query.whereEqualTo("opsName", et_opsName.getText().toString());
                    query.whereEqualTo("secretKey", et_secretKey.getText().toString());

                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                installation.put("opsId", object.getObjectId());
                                installation.put("opsName", object.getString("opsName"));
                                installation.put("callSign",et_callSign.getText().toString());
                                installation.saveInBackground();

                                ParseUtils.joinOperation(et_callSign.getText().toString());

                                Intent intent = new Intent(JoinOpsActivity.this, OperationActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("ERROR", "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!");
                                Toast.makeText(JoinOpsActivity.this, "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }

}