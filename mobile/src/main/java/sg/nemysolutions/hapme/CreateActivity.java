package sg.nemysolutions.hapme;

/**************** CreateActivity operation Page ******************/
/* This page is to allow Commanders to create
* an operation*/

//Key person: Yeekeng and Ming Sheng
/************************************************/

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);


        Button bn_addCmd = (Button) findViewById(R.id.bn_addCmd);
        Button bn_addMember = (Button) findViewById(R.id.bn_addMember);

        bn_addCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        bn_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
