package yuriykulikov.com.github.onoffswitch;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.github.androidutils.logger.Logger;

public class OnOffService extends Service {
    private boolean current;
    private AudioManager mAudioManager;
    private RemoteControlClient mRemoteControlClient;
    private PowerManager mPowerManager;
    private WakeLock mWakeLock;

    @Override
    public void onCreate() {
	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	ComponentName myEventReceiver = new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName());
	mAudioManager.registerMediaButtonEventReceiver(myEventReceiver);

	// build the PendingIntent for the remote control client
	Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
	mediaButtonIntent.setComponent(myEventReceiver);
	PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);
	// create and register the remote control client
	mRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
	mAudioManager.registerRemoteControlClient(mRemoteControlClient);

	int flags = RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS | RemoteControlClient.FLAG_KEY_MEDIA_NEXT
		| RemoteControlClient.FLAG_KEY_MEDIA_PLAY | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
		| RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_STOP;
	mRemoteControlClient.setTransportControlFlags(flags);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	Logger.getDefaultLogger().d(intent.toString());
	if (current) {
	    lockScreen();
	} else {
	    unlockScreen();
	}
	current = !current;
	return START_STICKY;
    }

    private void unlockScreen() {
	Logger.getDefaultLogger().d("disabling keyguard");
	// Intent intent = new Intent(this, TransparentActivity.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// startActivity(intent);
	mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
	mWakeLock = mPowerManager.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
	if (mWakeLock.isHeld()) {
	    mWakeLock.release();
	}
	mWakeLock.acquire();
	KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("Tag");
	keyguardLock.disableKeyguard();
    }

    private void lockScreen() {
	if (mWakeLock.isHeld()) {
	    mWakeLock.release();
	}
	DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
	mDPM.lockNow();
    }

    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }
}
