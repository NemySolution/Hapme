package sg.nemysolutions.hapme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.utilities.ParseUtils;

/**************** Join Operaion Activity ******************/
/* A "login" page for all the users (GC & members)
* for members to join_ops an activity_operation*/

//Key person: Yee Keng & Ming Sheng
/*********************************************/

public class JoinOpsActivity extends AppCompatActivity {

    private TextView tv_ops;
    private TextView tv_callSign;
    private TextView tv_secret_key;
    private EditText et_opsName;
    private EditText et_callSign;
    private EditText et_secretKey;
    private TextView tv_load;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_ops);

        tv_ops = (TextView) findViewById(R.id.textView2);
        tv_callSign = (TextView) findViewById(R.id.textView3);
        tv_secret_key = (TextView) findViewById(R.id.textView4);

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        et_secretKey = (EditText) findViewById(R.id.et_secretKey);

        tv_load = (TextView) findViewById(R.id.textView13);
        spinner = (ProgressBar) findViewById(R.id.progressBar);

        final Button bn_join = (Button) findViewById(R.id.bn_join);

        bn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_opsName.getText().toString().equals("") || et_secretKey.getText().toString().equals("")|| et_callSign.getText().toString().equals("")) {
                    Toast.makeText(JoinOpsActivity.this, "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!", Toast.LENGTH_LONG).show();
                } else {
                    et_opsName.setVisibility(View.GONE);
                    et_callSign.setVisibility(View.GONE);
                    et_secretKey.setVisibility(View.GONE);

                    tv_ops.setVisibility(View.GONE);
                    tv_callSign.setVisibility(View.GONE);
                    tv_secret_key.setVisibility(View.GONE);

                    bn_join.setVisibility(View.GONE);

                    spinner.setVisibility(View.VISIBLE);
                    tv_load.setVisibility(View.VISIBLE);
                    // initiate joining of activity_operation
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
                    query.whereEqualTo("opsName", et_opsName.getText().toString());
                    query.whereEqualTo("secretKey", et_secretKey.getText().toString());

                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                if (object.getList("members") != null) {
                                    if (object.getList("members").contains(et_callSign.getText().toString())) {
                                        Toast.makeText(JoinOpsActivity.this, "Please choose another Call Sign!!", Toast.LENGTH_LONG).show();
                                        et_opsName.setVisibility(View.VISIBLE);
                                        et_callSign.setVisibility(View.VISIBLE);
                                        et_secretKey.setVisibility(View.VISIBLE);
                                        tv_ops.setVisibility(View.VISIBLE);
                                        tv_callSign.setVisibility(View.VISIBLE);
                                        tv_secret_key.setVisibility(View.VISIBLE);
                                        bn_join.setVisibility(View.VISIBLE);
                                        spinner.setVisibility(View.GONE);
                                        tv_load.setVisibility(View.GONE);
                                    } else {
                                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                        installation.put("opsId", object.getObjectId());
                                        installation.put("opsName", object.getString("opsName"));
                                        installation.put("callSign", et_callSign.getText().toString());
                                        installation.saveInBackground();

                                        ParseUtils.joinOperation(et_callSign.getText().toString());

                                        // Needed for correct display of menu
                                        Intent intent = new Intent(JoinOpsActivity.this, OperationActivity.class);
                                        intent.putExtra("isMember", "true");
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                    installation.put("opsId", object.getObjectId());
                                    installation.put("opsName", object.getString("opsName"));
                                    installation.put("callSign", et_callSign.getText().toString());
                                    installation.saveInBackground();

                                    ParseUtils.joinOperation(et_callSign.getText().toString());

                                    // Needed for correct display of menu
                                    Intent intent = new Intent(JoinOpsActivity.this, OperationActivity.class);
                                    intent.putExtra("isMember", "true");
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Log.e("ERROR", "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!");
                                Toast.makeText(JoinOpsActivity.this, "Either opsName wrong or secretKey wrong, Cant retrieve Operation!!", Toast.LENGTH_LONG).show();
                                et_opsName.setVisibility(View.VISIBLE);
                                et_callSign.setVisibility(View.VISIBLE);
                                et_secretKey.setVisibility(View.VISIBLE);
                                tv_ops.setVisibility(View.VISIBLE);
                                tv_callSign.setVisibility(View.VISIBLE);
                                tv_secret_key.setVisibility(View.VISIBLE);
                                bn_join.setVisibility(View.VISIBLE);
                                spinner.setVisibility(View.GONE);
                                tv_load.setVisibility(View.GONE);
                            }
                        }
                    });
                }

            }
        });

    }

}