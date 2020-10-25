package com.catchit.lib.network.request;

import com.catchit.lib.models.AppInfo;
import com.catchit.lib.models.CatchItException;
import com.catchit.lib.models.DeviceInfo;

/**
 * Data model of sending exceptions to backend
 */
public class SyncExceptionRequestBody {

    public final AppInfo appInfo;

    public final DeviceInfo deviceInfo;

    public final CatchItException[] exceptions;

    public SyncExceptionRequestBody(AppInfo appInfo, DeviceInfo deviceInfo, CatchItException[] exceptions) {
        this.appInfo = appInfo;
        this.deviceInfo = deviceInfo;
        this.exceptions = exceptions;
    }
}
