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

/**************** MainActivity ******************/
/* A "login" page for all the users (GC & members)
* for members to join an activity_operation*/

//Key person: Yee Keng & Ming Sheng
/*********************************************/

public class JoinActivity extends AppCompatActivity {

    EditText et_opsName;
    EditText et_callSign;
    EditText et_secretKey;
    Button bn_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        et_secretKey = (EditText) findViewById(R.id.et_secretKey);

        bn_join = (Button) findViewById(R.id.bn_join);

        bn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_opsName.getText().toString().equals("") || et_secretKey.getText().toString().equals("")) {
                    Toast.makeText(JoinActivity.this, "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!", Toast.LENGTH_LONG).show();
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
                                Intent intent = new Intent(JoinActivity.this, OperationActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("ERROR", "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!");
                                Toast.makeText(JoinActivity.this, "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }

}