package sg.nemysolutions.hapme.activity;

/**************** HomeActivity Page ******************/
/* This page is to allow users to select whether
* they want to join or activity_createoperation operation.
* on page load, check if the user is already
* in an operation. */

//Key person: Yeekeng and Ming Sheng
/*********************************************/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sg.nemysolutions.hapme.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

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
                Intent intent = new Intent(HomeActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

    }

}
