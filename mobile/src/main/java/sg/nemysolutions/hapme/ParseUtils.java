package sg.nemysolutions.hapme;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class ParseUtils {
    public static void registerParse(Context context) {
        // initializing parse library
        //ParseObject.registerSubclass(Command.class);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "0RgU1BAusmGZiIkOFDVucZcEtbCHPiJx479CDcKG", "GhyTkHlqG22YziVd7fbP8YNTYK6wbmrcwF99yM5G");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
