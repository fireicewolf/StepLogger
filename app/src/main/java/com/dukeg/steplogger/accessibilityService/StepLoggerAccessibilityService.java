package com.dukeg.steplogger.accessibilityService;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;


public class StepLoggerAccessibilityService extends AccessibilityService {
    private static final String TAG = "StepLogger";
    Context context = this;

    @Override
    public void onServiceConnected() {
        Log.i(TAG, "Accessibility service enabled");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String eventTypeName = "";
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventTypeName = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventTypeName = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventTypeName = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventTypeName = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventTypeName = "TYPE_WINDOW_STATE_CHANGED";
                String foregroundAppName = event.getPackageName().toString();
                String foregroundAppActivityName = event.getClassName().toString();
                String foregroundAppLabel = getPackageLabelByPackageName(context, foregroundAppName);
                String foregroundAppActivityLabel = getActivityLabelByClassName(context, foregroundAppName, foregroundAppActivityName);
                Log.i(TAG, "当前前台应用程序: " + foregroundAppLabel + "-" + foregroundAppActivityLabel);
                Log.i(TAG, "Current foreground App and activity: " + foregroundAppName + "/" + foregroundAppActivityName);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventTypeName = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventTypeName = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
        }
        Log.i(TAG, "eventTypeName:" + eventTypeName);
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "Accessibility service disabled");
    }

    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }

    public static String getPackageLabelByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(pm.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getActivityLabelByClassName(Context context, String PackageName, String ActivityName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getActivityInfo(ComponentName.unflattenFromString(PackageName + "/" + ActivityName), 0).loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }
}
