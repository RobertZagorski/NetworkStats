package pl.rzagorski.networkstatsmanager.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import pl.rzagorski.networkstatsmanager.view.StatsActivity;

/**
 * Created by Robert Zagórski on 2016-09-09.
 */
public class PackageManagerHelper {


    public static boolean isPackage(Context context, CharSequence s) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(s.toString(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static int getPackageUid(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        int uid = -1;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            Log.d(StatsActivity.class.getSimpleName(), packageInfo.packageName);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return uid;
    }
}
