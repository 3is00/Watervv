package com.myself.waterreminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    registerPlugin(HelperPlugin.class);
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= 26) {
      Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
      AudioAttributes aa = new AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIC).build();
      NotificationChannel ch = new NotificationChannel("water_ring", "تذكير الماية", NotificationManager.IMPORTANCE_HIGH);
      ch.setSound(uri, aa);
      ch.enableVibration(true);
      ch.setVibrationPattern(new long[]{0, 800, 400, 800, 400, 800});
      ch.setLockscreenVisibility(1);
      getSystemService(NotificationManager.class).createNotificationChannel(ch);
    }
  }
}
