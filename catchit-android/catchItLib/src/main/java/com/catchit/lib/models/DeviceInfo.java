package com.catchit.lib.models;

import android.content.res.Resources;
import android.os.Build;

/**
 * Data model of device info
 */
public class DeviceInfo {

    public final String manufacturer;
    public final String model;
    public final String brand;
    public final int sdkVersion;
    public final int screenWidth;
    public final int screenHeight;
    public final int densityDpi;

    private DeviceInfo() {
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        brand = Build.BRAND;
        sdkVersion = Build.VERSION.SDK_INT;
        screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
        densityDpi =  Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static class Builder {
         public DeviceInfo build() {
            return new DeviceInfo();
        }
    }
}
