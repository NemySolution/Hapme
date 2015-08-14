package sg.nemysolutions.hapme.utilities;

import android.content.Context;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseInstallation;

import java.util.List;

import sg.nemysolutions.hapme.activity.OperationActivity;

public class ParseUtils {
    public static void registerParse(Context context) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "0RgU1BAusmGZiIkOFDVucZcEtbCHPiJx479CDcKG", "GhyTkHlqG22YziVd7fbP8YNTYK6wbmrcwF99yM5G");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public List<String> getChannels() {
        // Check with Parse whether this user is in any channels
        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");

        return subscribedChannels;
    }
}
