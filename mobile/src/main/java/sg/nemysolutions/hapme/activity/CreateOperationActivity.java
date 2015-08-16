package sg.nemysolutions.hapme.activity;

/************************************************
 * CreateActivity activity_operation Page

/* This page is to allow Commanders to create_operation
* an activity_operation*/

//Key person: Yeekeng and Ming Sheng
/************************************************/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.nemysolutions.hapme.utilities.Common;
import sg.nemysolutions.hapme.utilities.CustomListView;
import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.entity.Command;
import sg.nemysolutions.hapme.utilities.ParseUtils;

public class CreateOperationActivity extends AppCompatActivity {

    //handles the cmd that is displayed on listview
    private ArrayList<String> commandTextList = new ArrayList<>();
    //handles the command that goes to the parse db
    private ArrayList<Command> commandList = new ArrayList<>();

    private ListView lw_commands;
    private CustomListView arrayAdapter;
    private EditText et_opsName;
    private EditText et_callSign;
    private EditText et_secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_operation);

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        et_secretKey = (EditText) findViewById(R.id.et_secretKey);

        ImageButton bn_addCmd = (ImageButton) findViewById(R.id.bn_addCmd);
        Button bn_createOps = (Button) findViewById(R.id.bn_createOps);

        Common.offAutoKey(this);

        //button that goes to "add cmd" page
        bn_addCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOperationActivity.this, AddCmdActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //button that create operations
        bn_createOps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (et_opsName.getText().toString().equals("") ||
//                        et_callSign.getText().toString().equals("") ||
//                        et_secretKey.getText().toString().equals("")) {
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter all the fields!", Toast.LENGTH_SHORT).show();
                if (et_opsName.getText().toString().equals("")) {
                    et_opsName.setError("Operation name required");
                }
                if (et_callSign.getText().toString().equals("")) {
                    et_callSign.setError("Callsign required");
                }
                if (et_secretKey.getText().toString().equals("")) {
                    et_secretKey.setError("secret key required");

                } else if (et_opsName.getText().toString().equals("default")) {
                    et_opsName.setError("default cannot be used as Ops name!");
//                    Toast.makeText(getApplicationContext(),
//                            "default cannot be used as Ops name!", Toast.LENGTH_SHORT).show();
                } else {
                    ParseUtils.createOperation(CreateOperationActivity.this,
                            et_opsName.getText().toString(),
                            et_callSign.getText().toString(),
                            et_secretKey.getText().toString(),
                            commandList);
                }
            }
        });

        displayCommandList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Command command = (Command) data.getSerializableExtra("command");

                commandList.add(command);
                commandTextList.add(command.getCommandName());

                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    private void displayCommandList() {
        lw_commands = (ListView) findViewById(R.id.lv_addCmd);
        arrayAdapter = new CustomListView(commandTextList, commandList, this);
        lw_commands.setAdapter(arrayAdapter);
    }

}
