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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.entity.Command;
import sg.nemysolutions.hapme.utilities.Common;
import sg.nemysolutions.hapme.utilities.CustomListView;
import sg.nemysolutions.hapme.utilities.ParseUtils;

public class CreateOperationActivity extends AppCompatActivity {

    private static final String CMD_TEXT_LIST = "commandTextList";
    private static final String CMD_LIST = "commandList";
    //handles the cmd that is displayed on listview
    private ArrayList<String> commandTextList = new ArrayList<>();
    //handles the command that goes to the parse db
    private ArrayList<Command> commandList = new ArrayList<>();

    private ListView lw_commands;
    private CustomListView arrayAdapter;

    private TextView tv_ops;
    private TextView tv_callSign;
    private TextView tv_secret_key;
    private TextView tv_command_list;

    private EditText et_opsName;
    private EditText et_callSign;
    private EditText et_secretKey;

    private Button bn_addCmd;
    private Button bn_createOps;

    private TextView tv_load;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_operation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // If we have a saved state then we can restore it now
        if (savedInstanceState != null) {
            commandTextList = savedInstanceState.getStringArrayList(CMD_TEXT_LIST);
            commandList = (ArrayList<Command>)savedInstanceState.get(CMD_LIST);

        }

        tv_ops = (TextView) findViewById(R.id.textView5);
        tv_callSign = (TextView) findViewById(R.id.textView6);
        tv_secret_key = (TextView) findViewById(R.id.textView11);
        tv_command_list = (TextView) findViewById(R.id.textView12);

        et_opsName = (EditText) findViewById(R.id.et_opsName);
        et_callSign = (EditText) findViewById(R.id.et_callSign);
        et_secretKey = (EditText) findViewById(R.id.et_secretKey);

        bn_addCmd = (Button) findViewById(R.id.bn_addCmd);
        bn_createOps = (Button) findViewById(R.id.bn_createOps);

        tv_load = (TextView) findViewById(R.id.textView14);
        spinner = (ProgressBar) findViewById(R.id.progressBar2);

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
                if (et_opsName.getText().toString().equals("")) {
                    et_opsName.setError("Operation name required");
                } else if (et_callSign.getText().toString().equals("")) {
                    et_callSign.setError("Callsign required");
                } else if (et_secretKey.getText().toString().equals("")) {
                    et_secretKey.setError("secret key required");
                } else if (et_opsName.getText().toString().equals("default")) {
                    et_opsName.setError("default cannot be used as Ops name!");
                } else {

                    lw_commands.setVisibility(View.GONE);

                    tv_ops.setVisibility(View.GONE);
                    tv_callSign.setVisibility(View.GONE);
                    tv_secret_key.setVisibility(View.GONE);
                    tv_command_list.setVisibility(View.GONE);

                    et_opsName.setVisibility(View.GONE);
                    et_callSign.setVisibility(View.GONE);
                    et_secretKey.setVisibility(View.GONE);

                    bn_addCmd.setVisibility(View.GONE);
                    bn_createOps.setVisibility(View.GONE);

                    spinner.setVisibility(View.VISIBLE);
                    tv_load.setVisibility(View.VISIBLE);

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

    //saving cmd list state testing
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putSerializable(CMD_TEXT_LIST, commandTextList);
        outState.putSerializable(CMD_LIST, commandList);
    }

}
