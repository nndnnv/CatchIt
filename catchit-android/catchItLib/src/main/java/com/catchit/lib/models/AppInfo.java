package com.catchit.lib.models;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Data model of container app info
 */
public class AppInfo {

    public String version;

    public long versionCode;

    private AppInfo(Context context) {

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
            versionCode = pInfo.getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e) {
            version = null;
            versionCode = -1;
        }
    }


    public static class Builder {

        private Context mApplicationContext = null;

        public AppInfo.Builder setApplicationContext(Context context) {
            mApplicationContext = context;
            return this;
        }

        public AppInfo build() {
            if (mApplicationContext == null) {
                throw new IllegalArgumentException("You must provide Application Context in AppInfo");
            }
            return new AppInfo(mApplicationContext);
        }
    }
}
