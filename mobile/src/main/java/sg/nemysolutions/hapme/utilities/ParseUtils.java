package sg.nemysolutions.hapme.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import sg.nemysolutions.hapme.activity.OperationActivity;
import sg.nemysolutions.hapme.config.AppConfig;
import sg.nemysolutions.hapme.entity.Command;

public class ParseUtils {
    /**
     *
     * @param context
     */
    public static void registerParse(Context context) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, AppConfig.parseAppId, AppConfig.parseClientKey);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

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

        ParseObject operation = new ParseObject("Operation");
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

                Intent intent = new Intent(context, OperationActivity.class);
                context.startActivity(intent);
                context.finish();

                /*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("opsId", operation.getObjectId());
                installation.put("opsName", operation.getString("opsName"));
                installation.put("callSign", operation.getString("callSign"));
                installation.saveInBackground();
                Intent intent = new Intent(CreateOperationActivity.this, OperationActivity.class);
//                            intent.putExtra("opsId", operation.getObjectId());
//                            intent.putExtra("opsName", operation.getString("opsName"));
//                            intent.putExtra("callSign", operation.getString("callSign")); */
            }
        });
    }
}
