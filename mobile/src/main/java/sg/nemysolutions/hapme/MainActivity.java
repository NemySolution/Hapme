package sg.nemysolutions.hapme;

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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;

public class MainActivity extends AppCompatActivity {
//    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Start Parse
        ParseUtils.registerParse(MainActivity.this);

        Button bn1 = (Button) findViewById(R.id.button);
        Button bn2 = (Button) findViewById(R.id.button2);
        Button bn3 = (Button) findViewById(R.id.button3);
        Button bn4 = (Button) findViewById(R.id.button4);
        Button bn5 = (Button) findViewById(R.id.button5);
        Button bn6 = (Button) findViewById(R.id.button6);
        Button bn7 = (Button) findViewById(R.id.button7);

        bn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        bn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommandsActivity.class);
                startActivity(intent);
            }
        });
        bn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
        bn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        bn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OperationActivity.class);
                startActivity(intent);
            }
        });
        bn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
//        mContext = getApplicationContext();
//        bn7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    create(mContext);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

//    public static AlertDialog create(Context context) throws PackageManager.NameNotFoundException {
//
//        PackageInfo pInfo=
//                context.getPackageManager()
//                        .getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//
//        String versionInfo=pInfo.versionName;
//        String aboutTitle=String.format("About %s", context.getString(R.string.app_name));
//        String versionString=String.format("Version: %s",versionInfo);
//        final TextView message=new TextView(context);
//
//        message.setPadding(5, 5, 5, 5);
//        message.setText(versionString);
//        Linkify.addLinks(message, Linkify.ALL);
//
//
//        return new AlertDialog.Builder(context)
//                .setTitle(aboutTitle)
//                .setCancelable(true)
//                .setPositiveButton(context.getString(android.R.string.ok), null)
//                .setView(message)
//                .create();
//    }

}