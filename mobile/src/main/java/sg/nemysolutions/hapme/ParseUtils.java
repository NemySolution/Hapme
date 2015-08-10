package sg.nemysolutions.hapme;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class ParseUtils {
    public static void registerParse(Context context) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "0RgU1BAusmGZiIkOFDVucZcEtbCHPiJx479CDcKG", "GhyTkHlqG22YziVd7fbP8YNTYK6wbmrcwF99yM5G");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
