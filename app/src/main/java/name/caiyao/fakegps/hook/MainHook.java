package name.caiyao.fakegps.hook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 蔡小木 on 2016/4/17 0017.
 */
public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
        final Context systemContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
        Uri uri = Uri.parse("content://name.caiyao.fakegps.data.AppInfoProvider/app");
        Cursor cursor = systemContext.getContentResolver().query(uri, new String[]{"level"}, "package_name=?", new String[]{loadPackageParam.packageName}, null);
        if (cursor != null && cursor.moveToNext()) {
            int level = cursor.getInt(cursor.getColumnIndex("level"));
            XposedBridge.log("保护位置:" + loadPackageParam.packageName + "," + level);
            HookUtils.HookAndChange(loadPackageParam.classLoader,level);
            cursor.close();
        }
    }
}
