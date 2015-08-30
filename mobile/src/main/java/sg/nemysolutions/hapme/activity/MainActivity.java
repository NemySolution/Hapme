package sg.nemysolutions.hapme.activity;

/**************** MainActivity ******************/
/* Temporary page for us to easily access our
* development page. In the end we will integrate
* together when everybody is done with their
* own parts.
*
* Do contact Ming Sheng if you want to add in
* pages to develop. */

//Key person: Ming Sheng
/*********************************************/

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import sg.nemysolutions.hapme.R;
import sg.nemysolutions.hapme.utilities.Information;
import sg.nemysolutions.hapme.utilities.ParseUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialise Parse
//        ParseUtils.registerParse(this);

        // Check with Parse whether this user is in any channels
        if (!ParseUtils.getChannels().isEmpty()) {
            Intent intent = new Intent(MainActivity.this, OperationActivity.class);
            startActivity(intent);
            finish();
        }

        Button bn_homePage = (Button) findViewById(R.id.bn_homePage);
        Button bn_createOperation = (Button) findViewById(R.id.bn_createOperation);
        Button bn_joinOperation = (Button) findViewById(R.id.bn_joinOperation);
        Button bn_about = (Button) findViewById(R.id.bn_about);

        bn_createOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateOperationActivity.class);
                startActivity(intent);
            }
        });

        bn_homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        bn_joinOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinOpsActivity.class);
                startActivity(intent);
            }
        });

        bn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ver = Information.getVersion(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(ver).setTitle("Version");

                // 3. Get the AlertDialog from create_operation()
                AlertDialog dialog = builder.create();
                dialog.show();
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
//            case R.id.action_compose:
//                composeMessage();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}