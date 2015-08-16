package sg.nemysolutions.hapme.utilities;
import com.parse.Parse;
import com.parse.ParseInstallation;

import android.app.Application;

import sg.nemysolutions.hapme.config.AppConfig;

/**
 * Created by yeekeng on 16/8/2015.
 * this will start parse when it runs, it is start by androidManifest.xml
 *  android:name="sg.nemysolutions.hapme.utilities.ParseStarter"
 */
public class ParseStarter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, AppConfig.parseAppId, AppConfig.parseClientKey);
        ParseInstallation.getCurrentInstallation().saveInBackground();    }
}