package sg.nemysolutions.hapme;

/**************** CommandsActivity Page ******************/
/* This page is to allow users to broadcast a
* command.(backup page to broadcast the command*/

//Key person: Nicholas
/*************************************************/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import sg.nemysolutions.hapme.entity.Command;

public class CommandsActivity extends AppCompatActivity {
    List<String> commandTextList  = new ArrayList<>();
    List<Command> commandList = new ArrayList<>();
    ListView lw = null;
    String opsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commands);

        Intent intent = getIntent();
        String opsName = intent.getStringExtra("opsName");
        String isCommander = intent.getStringExtra("isCommander");

        lw = (ListView) findViewById(R.id.lv_commands);

        if (isCommander.equals("true")) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Command");
            query.whereEqualTo("opsName", opsName);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> results, ParseException e) {
                    for (ParseObject c : results) {
                        commandTextList.add(c.getString("commandName"));
                        Command command = new Command();
                        command.setOpsName(c.getString("opsName"));
                        command.setCommandName(c.getString("commandName"));
                        command.setCommandID(c.getObjectId());

                        commandList.add(command);
                    }
                    setList();
                }
            });
        } else {
            // members default commands
            Command default1 = new Command();
            default1.setOpsName(opsName);
            default1.setCommandName("Need Backup");
            commandList.add(default1);
            commandTextList.add(default1.getCommandName());

            Command default2 = new Command();
            default2.setOpsName(opsName);
            default2.setCommandName("Send Location");
            commandList.add(default2);
            commandTextList.add(default2.getCommandName());
            setList();
        }

        lw.setOnItemClickListener(onItemClickListener);
    }

    private void setList() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, commandTextList);
        lw.setAdapter(arrayAdapter);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            Command command = commandList.get(position);

            ParsePush push = new ParsePush();
            push.setChannel(command.getOpsName());
            push.setMessage(command.getCommandName());
            push.sendInBackground();

            Toast.makeText(getApplicationContext(), "Command: " + command.getCommandName() + " SENT!", Toast.LENGTH_SHORT).show();
            finish();
        }
    };
}
