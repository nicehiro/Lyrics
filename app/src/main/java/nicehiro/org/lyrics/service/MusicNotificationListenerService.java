package nicehiro.org.lyrics.service;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

@SuppressLint("OverrideAbstract")
public class MusicNotificationListenerService extends NotificationListenerService {

  private static String TAG = "MUSIC-NOTIFICATION-LISTENER";

  @Override
  public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    Log.d(TAG, "Notification Changed!");
  }
}
