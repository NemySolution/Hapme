package sg.nemysolutions.hapme.utilities;

import android.content.Context;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.List;

import sg.nemysolutions.hapme.activity.OperationActivity;
import sg.nemysolutions.hapme.config.AppConfig;

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

        if (!ParseInstallation.getCurrentInstallation().getList("channels").isEmpty()) {
            subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        }

        return subscribedChannels;
    }
}
