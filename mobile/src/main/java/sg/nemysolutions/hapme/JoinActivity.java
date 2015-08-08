package sg.nemysolutions.hapme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**************** MainActivity ******************/
/* A "login" page for all the users (GC & members)
* for members to join an operation*/

//Key person: Yee Keng & Ming Sheng
/*********************************************/

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        Button bn_join = (Button) findViewById(R.id.bn_join);
        bn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, OperationActivity.class);
                startActivity(intent);
            }
        });
    }

}
