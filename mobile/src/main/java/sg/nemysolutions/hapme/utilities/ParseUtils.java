package sg.nemysolutions.hapme.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.nemysolutions.hapme.activity.OperationActivity;
import sg.nemysolutions.hapme.config.AppConfig;
import sg.nemysolutions.hapme.entity.Command;

public class ParseUtils {
    /**
     *
     * @param context
     */
    //yk: i comment this out to test parse, its working now and can be remove
//    public static void registerParse(Context context) {
//        // Enable Local Datastore.
//        Parse.enableLocalDatastore(context);
//        Parse.initialize(context, AppConfig.parseAppId, AppConfig.parseClientKey);
//        ParseInstallation.getCurrentInstallation().saveInBackground();
//    }

    /**
     *
     * @return a list of channel if not empty else an empty list
     */
    public static List<String> getChannels() {
        // Check with Parse whether this user is in any channels
        List<String> subscribedChannels = new ArrayList<>();

        if (ParseInstallation.getCurrentInstallation().getList("channels") != null) {
            subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        }

        return subscribedChannels;
    }

    /**
     *
     * @param context
     * @param opsName
     * @param callSign
     * @param secretKey
     * @param commandList
     */
    public static void createOperation(final AppCompatActivity context, final String opsName, String callSign, String secretKey, final List<Command> commandList) {

        final ParseObject operation = new ParseObject("Operation");
        operation.put("deviceId", ParseInstallation.getCurrentInstallation().getInstallationId());
        operation.put("opsName", opsName);
        operation.put("callSign", callSign);
        operation.put("secretKey", secretKey);

        operation.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                ParseObject command;

                for (Command c : commandList) {
                    command = new ParseObject("Command");

                    command.put("opsName", opsName);
                    command.put("commandName", c.getCommandName());
                    command.put("vibrationSeq", c.getVibrationSeq());
                    command.put("gestureSeq", c.getGestureSeq());

                    command.saveInBackground();
                }

                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("opsId", operation.getObjectId());
                installation.put("opsName", operation.getString("opsName"));
                installation.put("callSign", operation.getString("callSign"));

                installation.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(context, OperationActivity.class);
                            context.startActivity(intent);
                            context.finish();
                        } else {
                            Toast.makeText(context, "Cannot Create Operation. Try Again!", Toast.LENGTH_SHORT);
                        }
                    }
                });




                /*
                Intent intent = new Intent(CreateOperationActivity.this, OperationActivity.class);
//                            intent.putExtra("opsId", operation.getObjectId());
//                            intent.putExtra("opsName", operation.getString("opsName"));
//                            intent.putExtra("callSign", operation.getString("callSign")); */
            }
        });
    }

    public static void joinOperation(final String callSign) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List<String> members;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
        query.getInBackground(installation.getString("opsId"), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                List<String> members = object.getList("members");

                if (members == null) {
                    members = new ArrayList<String>();
                }

                members.add(callSign);

                object.put("members", members);
                object.saveInBackground();
            }
        });
    }
}
