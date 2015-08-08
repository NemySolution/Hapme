package sg.nemysolutions.hapme;

/**************** CreateActivity operation Page ******************/
/* This page is to allow Commanders to create
* an operation*/

//Key person: Yeekeng and Ming Sheng
/************************************************/

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParsePush;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    List<String> commandTextList = new ArrayList<String>();
    List<Command> commandList = new ArrayList<Command>();
    ListView lw_commands;
    ArrayAdapter<String> arrayAdapter;
    ListView listView_addCmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        lw_commands = (ListView) findViewById(R.id.listView_addCmd);
        listView_addCmd = (ListView) findViewById(R.id.listView_addCmd);

        //declare button
        Button bn_addCmd = (Button) findViewById(R.id.bn_addCmd);

        //add command
        bn_addCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, AddActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        listView_addCmd.setOnItemClickListener(onItemClickListener);

        Button bn_createOps = (Button) findViewById(R.id.btn_createOps);

        //create ops
        bn_createOps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_opsName = (EditText) findViewById(R.id.editText_opsName);
                EditText editText_cmdCallsign = (EditText) findViewById(R.id.editText_opsName);
                EditText editText_secretKey = (EditText) findViewById(R.id.editText_secretKey);

                ParseObject operation = new ParseObject("Operation");
                operation.put("opsName", editText_opsName.getText().toString());
                operation.put("callSign", editText_cmdCallsign.getText().toString());
                operation.put("secretKey", editText_secretKey.getText().toString());

                operation.saveInBackground();

                ParseObject command;

                for (Command c : commandList) {
                    command = new ParseObject("Command");
                    command.put("opsName", editText_opsName.getText().toString());
                    command.put("commandName", c.getCommandName());
                    command.put("vibrationSeq", c.getVibrationSeq());
                    command.put("gestureSeq", c.getGestureSeq());
                    command.saveInBackground();
                }

                finish();
            }
        });
    }//end of oncreate()

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            AlertDialog.Builder adb=new AlertDialog.Builder(getApplicationContext());
            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete " + position);
            final int positionToRemove = position;
            adb.setNegativeButton("Cancel", null);
            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    commandList.remove(positionToRemove);
                    commandTextList.remove(positionToRemove);
                    arrayAdapter.notifyDataSetChanged();
                }});
            adb.show();
        }
    };

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
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commandTextList);
       arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commandTextList);
        lw_commands.setAdapter(arrayAdapter);
    }








}
