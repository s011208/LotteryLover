package yhh.bj4.lotterylover;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import yhh.bj4.lotterylover.applicationproxy.ApplicationProxy;
import yhh.bj4.lotterylover.applicationproxy.MainApplicationProxy;
import yhh.bj4.lotterylover.applicationproxy.RemoteComponentsApplicationProxy;

/**
 * Created by User on 2016/6/15.
 */
public class LotteryLoverApplication extends Application {
    private ApplicationProxy mApplicationProxy;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationProxy = createApplicationProxy();
        if (mApplicationProxy != null) {
            mApplicationProxy.onCreate();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mApplicationProxy != null) {
            mApplicationProxy.onTerminate();
        }
    }

    private ApplicationProxy createApplicationProxy() {
        final String processName = getProcessName(this, Process.myPid());
        if (processName == null) {
            return null;
        }
        if (getPackageName().equals(processName)) {
            return new MainApplicationProxy(this);
        } else if (processName.endsWith(":remote_components")) {
            return new RemoteComponentsApplicationProxy(this);
        }
        return null;
    }

    @Nullable
    private static String getProcessName(Context context, int pID) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : am.getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid != pID) continue;
            if (!TextUtils.isEmpty(runningAppProcessInfo.processName) && runningAppProcessInfo.processName.startsWith("yhh.bj4.lotterylover")) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

}
