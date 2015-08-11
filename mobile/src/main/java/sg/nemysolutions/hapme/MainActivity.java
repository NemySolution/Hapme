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
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.ParseInstallation;

import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Parse
        ParseUtils.registerParse(MainActivity.this);

        // Check with Parse whether this user is in any channels
        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        if (subscribedChannels != null) {
            if (!subscribedChannels.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, OperationActivity.class);
                intent.putExtra("opsName", subscribedChannels.get(0));
                startActivity(intent);
                finish();
            }
        }

        Button bn3 = (Button) findViewById(R.id.button3);
        Button bn4 = (Button) findViewById(R.id.button4);
        Button bn6 = (Button) findViewById(R.id.button6);
        Button bn7 = (Button) findViewById(R.id.button7);

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
        bn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

//        mContext = getApplicationContext();
        bn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ver = getVersion();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(ver).setTitle("version");
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
//                try {
//                    create(mContext);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        });

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

    private String getVersion(){
        try {
            PackageManager packageManager=getPackageManager();
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        }
        catch (  PackageManager.NameNotFoundException e) {
            Log.e("versionerrorrrrrrr","Error while fetching app version", e);

            return "?";
        }
    }

}