package sg.nemysolutions.hapme.utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class Information {

    /**
     * This method is called by MainActivity.java to retrieve application version
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Fetch Version Error", "Error:", e);

            return "Fetch Version Error";
        }
    }
}
