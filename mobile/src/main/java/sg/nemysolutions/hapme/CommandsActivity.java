package sg.nemysolutions.hapme;

/**************** CommandsActivity Page ******************/
/* This page is to allow users to broadcast a
* command.(backup page to broadcast the command*/

//Key person: Nicholas
/*************************************************/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommandsActivity extends AppCompatActivity {
    List<String> commandTextList  = new ArrayList<String>();
    List<Command> commandList = new ArrayList<Command>();
    ListView lw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commands);

        lw = (ListView) findViewById(R.id.lv_commands);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Command");
        query.whereEqualTo("opsName", "opDick");
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

        lw.setOnItemClickListener(onItemClickListener);
    }

    private void setList() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commandTextList);
        lw.setAdapter(arrayAdapter);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            Command command = commandList.get(position);

            ParsePush push = new ParsePush();
            push.setChannel(command.getOpsName());
            push.setMessage(command.getCommandID());
            push.sendInBackground();

            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), "Command: " + command.getCommandName() + " SENT!", duration);
            toast.show();
        }
    };
}
