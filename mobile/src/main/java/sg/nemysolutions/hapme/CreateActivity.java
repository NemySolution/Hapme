package sg.nemysolutions.hapme;

/**************** CreateActivity operation Page ******************/
/* This page is to allow Commanders to create
* an operation*/

//Key person: Yeekeng and Ming Sheng
/************************************************/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

import sg.nemysolutions.hapme.entity.Command;

public class CreateActivity extends AppCompatActivity {

    //handles the cmd that is displayed on listview
    ArrayList<String> commandTextList = new ArrayList<>();
    //handles the command that goes to the parse db
    ArrayList<Command> commandList = new ArrayList<>();
    ListView lw_commands;
    CustomListView arrayAdapter;

    EditText et_opsName;
    EditText et_callSign;
    EditText et_secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        this.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        lw_commands = (ListView) findViewById(R.id.listView_addCmd);

        //declare buttons
        Button bn_addCmd = (Button) findViewById(R.id.bn_addCmd);
        Button bn_createOps = (Button) findViewById(R.id.btn_createOps);

        //button that goes to "add cmd" page
        bn_addCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, AddActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        et_secretKey = (EditText) findViewById(R.id.editText_secretKey);

        bn_createOps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_opsName.getText().toString().equals("") || et_callSign.getText().toString().equals("") || et_secretKey.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all the fields!", Toast.LENGTH_SHORT).show();
                } else {
                    String deviceId = ParseInstallation.getCurrentInstallation().getString("installationId");

                    // Save the operation to parse, followed by the commands
                    final ParseObject operation;
                    operation = new ParseObject("Operation");
                    operation.put("deviceId", deviceId);
                    operation.put("opsName", et_opsName.getText().toString());
                    operation.put("callSign", et_callSign.getText().toString());
                    operation.put("secretKey", et_secretKey.getText().toString());
                    operation.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseObject command;
                            for (Command c : commandList) {
                                command = new ParseObject("Command");
                                command.put("opsId", operation.getObjectId());
                                command.put("opsName", et_opsName.getText().toString());
                                command.put("commandName", c.getCommandName());
                                command.put("vibrationSeq", c.getVibrationSeq());
                                command.put("gestureSeq", c.getGestureSeq());
                                command.saveInBackground();
                            }
                            Intent intent = new Intent(CreateActivity.this, OperationActivity.class);
                            intent.putExtra("opsId", operation.getObjectId());
                            intent.putExtra("opsName", operation.getString("opsName"));
                            intent.putExtra("callSign", operation.getString("callSign"));
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if(resultCode == RESULT_OK){
                Command command = (Command) data.getSerializableExtra("command");
                commandList.add(command);
                commandTextList.add(command.getCommandName());
                setCommandTextList();
            }
        }
    }//onActivityResult

    private void setCommandTextList() {
        arrayAdapter = new CustomListView(commandTextList,commandList,this);
        lw_commands.setAdapter(arrayAdapter);
    }

}
