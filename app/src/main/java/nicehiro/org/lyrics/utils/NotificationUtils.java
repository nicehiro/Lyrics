package nicehiro.org.lyrics.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import nicehiro.org.lyrics.service.MusicNotificationListenerService;

public class NotificationUtils {

  private static final String TAG = "NOTIFICATIONS";

  private final static String ENABLE_NOTIFICATION_LISTENER = "enabled_notification_listeners";

  public static boolean isNotificationListenerEnable(Context context) {
    String pkgName = context.getPackageName();
    final String flat = Settings.Secure.getString(context.getContentResolver(), ENABLE_NOTIFICATION_LISTENER);
    if (!TextUtils.isEmpty(flat)) {
      final String[] names = flat.split(":");
      for (int i=0; i<names.length; i++) {
        final ComponentName componentName = ComponentName.unflattenFromString(names[i]);
        if (componentName != null) {
          if (TextUtils.equals(componentName.getPackageName(), pkgName)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static boolean gotoNotificationSetting(Context context) {
    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
    return true;
  }

  public static void toggleNotificationListenerService(Context context) {
    Log.d(TAG, "toggle notification listener service...");
    PackageManager packageManager = context.getPackageManager();
    packageManager.setComponentEnabledSetting(
      new ComponentName(context, MusicNotificationListenerService.class),
      PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    packageManager.setComponentEnabledSetting(
      new ComponentName(context, MusicNotificationListenerService.class),
      PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
  }
}
