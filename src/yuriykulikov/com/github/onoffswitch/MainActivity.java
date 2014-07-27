package yuriykulikov.com.github.onoffswitch;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.github.androidutils.logger.LogcatLogWriterWithLines;
import com.github.androidutils.logger.Logger;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Logger.getDefaultLogger().addLogWriter(LogcatLogWriterWithLines.getInstance());
	startService(new Intent(this, OnOffService.class));

	// Launch the activity to have the user enable our admin.
	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(this, MyDeviceAdminReceiver.class));
	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "to lock the screen");
	startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }
}
