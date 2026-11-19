package com.myself.waterreminder;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Helper")
public class HelperPlugin extends Plugin {

  private void go(Intent i) {
    try {
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      getContext().startActivity(i);
    } catch (Exception e) {
    }
  }

  private Uri pkg() {
    return Uri.parse("package:" + getContext().getPackageName());
  }

  @PluginMethod
  public void status(PluginCall call) {
    Context c = getContext();
    JSObject r = new JSObject();
    PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
    r.put("battery", pm.isIgnoringBatteryOptimizations(c.getPackageName()));
    boolean ex = true;
    if (Build.VERSION.SDK_INT >= 31) {
      AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
      ex = am.canScheduleExactAlarms();
    }
    r.put("exact", ex);
    call.resolve(r);
  }

  @PluginMethod
  public void askBattery(PluginCall call) {
    go(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, pkg()));
    call.resolve();
  }

  @PluginMethod
  public void askExact(PluginCall call) {
    if (Build.VERSION.SDK_INT >= 31) {
      go(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, pkg()));
    }
    call.resolve();
  }

  @PluginMethod
  public void appSettings(PluginCall call) {
    go(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, pkg()));
    call.resolve();
  }

  @PluginMethod
  public void channelSettings(PluginCall call) {
    Intent i = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
    i.putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
    i.putExtra(Settings.EXTRA_CHANNEL_ID, "water_ring");
    go(i);
    call.resolve();
  }
}
