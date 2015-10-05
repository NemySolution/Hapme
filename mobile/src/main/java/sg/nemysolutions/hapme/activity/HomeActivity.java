package sg.nemysolutions.hapme.activity;

/**************** HomeActivity Page ******************/
/* This page is to allow users to select whether
* they want to join_ops or create_operation activity_operation.
* on page load, check if the user is already
* in an activity_operation. */

//Key person: Yeekeng and Ming Sheng
/*********************************************/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.utilities.ParseUtils;

public class HomeActivity extends AppCompatActivity {

    RelativeLayout layout_load;
    LinearLayout layout_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        layout_load = (RelativeLayout) findViewById(R.id.layout_load);
        layout_home = (LinearLayout) findViewById(R.id.layout_home);

        // Check with Parse whether this user is in any channels
        if (!ParseUtils.getChannels().isEmpty()) {
            String channel = ParseUtils.getChannels().get(0);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
            query.whereEqualTo("opsName", channel);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> results, ParseException e) {
                    if (results.size() > 0) {
                        Intent intent = new Intent(HomeActivity.this, OperationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ParseUtils.removeChannel();
                        layout_load.setVisibility(View.GONE);
                        layout_home.setVisibility(View.VISIBLE);
                    }
                }
            });

        } else {
            layout_load.setVisibility(View.GONE);
            layout_home.setVisibility(View.VISIBLE);
        }

        Button bn_create = (Button) findViewById(R.id.bn_create);
        Button bn_join = (Button) findViewById(R.id.bn_join);

        bn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CreateOperationActivity.class);
                startActivity(intent);
            }
        });

        bn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, JoinOpsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_exit:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
