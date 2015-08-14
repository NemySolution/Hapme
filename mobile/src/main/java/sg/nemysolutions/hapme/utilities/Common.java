package sg.nemysolutions.hapme.utilities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Common {

    public static void offAutoKey(AppCompatActivity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
