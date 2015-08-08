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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        //declare button
        Button bn_addCmd = (Button) findViewById(R.id.bn_addCmd);
        Button bn_addMember = (Button) findViewById(R.id.bn_addMember);

        //add command
        bn_addCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //add member
        bn_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }//end of oncreate()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // For Add member result
        if (requestCode == 0) {
            if(resultCode == RESULT_OK){
                //String result=data.getStringExtra("result");
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

               Command command = (Command) data.getSerializableExtra("command");


            }
        }
    }//onActivityResult
}
