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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommandsActivity extends AppCompatActivity {
    List<String> commandList  = new ArrayList<String>();
    ListView lw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commands);

        lw = (ListView) findViewById(R.id.lv_commands);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Command");
        query.whereEqualTo("opsName", "123");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject a : results) {
                    commandList.add(a.getString("commandName"));
                }

                setList();
            }
        });

        lw.setOnItemClickListener(onItemClickListener);
    }

    private void setList() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commandList);
        lw.setAdapter(arrayAdapter);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {

        }
    };
}
