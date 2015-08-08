package sg.nemysolutions.hapme;

/**************** OperationActivity Page ******************/
/* This page is to allow users to view operation
* details and to assure them that they are in
* an operation.
*
* For Ground Commander, this page
* will let him able to MANAGE operation details,
* commands and also broadcast it. Viewing of location
* of members are in consideration*/

//Key person: Yeekeng and Ming Sheng
/*************************************************/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OperationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation);
    }

}
