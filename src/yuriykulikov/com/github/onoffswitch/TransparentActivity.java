package yuriykulikov.com.github.onoffswitch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.androidutils.logger.Logger;

public class TransparentActivity extends Activity {
    Logger log;

    @Override
    protected void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	final Window win = getWindow();
	win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
	// Turn on the screen unless we are being launched from the
	// AlarmAlert
	// subclass as a result of the screen turning off.
	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
		| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
	// finish();
    }
}
