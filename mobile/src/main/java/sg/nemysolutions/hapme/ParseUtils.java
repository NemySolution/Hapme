package sg.nemysolutions.hapme;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Nvcwk on 8/8/2015.
 */
public class ParseUtils {
    public static void registerParse(Context context) {
        // initializing parse library
        ParseObject.registerSubclass(Command.class);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "0RgU1BAusmGZiIkOFDVucZcEtbCHPiJx479CDcKG", "GhyTkHlqG22YziVd7fbP8YNTYK6wbmrcwF99yM5G");


    }
}
