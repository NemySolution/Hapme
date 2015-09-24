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

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.utilities.ParseUtils;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Check with Parse whether this user is in any channels
        if (!ParseUtils.getChannels().isEmpty()) {
            Intent intent = new Intent(HomeActivity.this, OperationActivity.class);
            startActivity(intent);
            finish();
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
