package yuriykulikov.com.github.onoffswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.github.androidutils.logger.Logger;

public class MediaButtonIntentReceiver extends BroadcastReceiver {
    private static final String TOGGLE = "yuriykulikov.com.github.onoffswitch.OnOffService.MediaButtonIntentReceiver.TOGGLE";
    private static boolean mDown = false;

    @Override
    public void onReceive(Context context, Intent intent) {
	String intentAction = intent.getAction();
	Logger logger = Logger.getDefaultLogger();
	logger.d(intentAction);
	if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
	    KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

	    if (event == null)
		return;

	    int keycode = event.getKeyCode();
	    int action = event.getAction();

	    String command = null;
	    switch (keycode) {
	    case KeyEvent.KEYCODE_MEDIA_STOP:
	    case KeyEvent.KEYCODE_HEADSETHOOK:
	    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
	    case KeyEvent.KEYCODE_MEDIA_NEXT:
	    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
	    case KeyEvent.KEYCODE_MEDIA_PAUSE:
	    case KeyEvent.KEYCODE_MEDIA_PLAY:
		command = "does not matter";
		break;
	    }

	    if (command != null) {
		if (action == KeyEvent.ACTION_UP) {
		    if (event.getRepeatCount() == 0) {
			// only consider the first event in a sequence, not
			// the
			// repeat events,
			// so that we don't trigger in cases where the first
			// event went to
			// a different app (e.g. when the user ends a phone
			// call
			// by
			// long pressing the headset button)

			// The service may or may not be running, but we
			// need to
			// send it
			// a command.
			Logger.getDefaultLogger().d("starting");
			Intent i = new Intent(context, OnOffService.class);
			i.setAction(MediaButtonIntentReceiver.TOGGLE);
			context.startService(i);
		    }
		}
		if (isOrderedBroadcast()) {
		    abortBroadcast();
		}
	    }
	}
    }
}