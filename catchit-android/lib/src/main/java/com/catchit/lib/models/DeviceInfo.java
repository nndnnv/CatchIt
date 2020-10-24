package com.catchit.lib.models;

import android.content.res.Resources;
import android.os.Build;

/**
 * Data model of device info
 */
public class DeviceInfo {

    public String manufacturer;
    public String model;
    public String brand;
    public int sdkVersion;
    public int screenWidth;
    public int screenHeight;
    public int densityDpi;

    private DeviceInfo() {
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        brand = Build.BRAND;
        sdkVersion = Build.VERSION.SDK_INT; // Android API_Version
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
