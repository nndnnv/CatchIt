package com.catchit.lib.sync.worker;

import com.catchit.lib.data.ExceptionsRepo;
import com.catchit.lib.models.AppInfo;
import com.catchit.lib.models.CatchItException;
import com.catchit.lib.models.DeviceInfo;
import com.catchit.lib.network.request.SyncExceptionRequestBody;

/**
 * Worker Runnable that runs when SyncNotifier notify.
 *
 * Feting all pending exceptions, and sync with server
 *
 * Run on the DiskIO Executor, network operations run on Retrofit own Dispatcher
 */
public class SyncExceptionsWorker implements Runnable {

    private final ExceptionsRepo mRepo;
    private final AppInfo mAppInfo;
    private final DeviceInfo mDeviceInfo;

    private SyncExceptionsWorker(ExceptionsRepo exceptionsRepo, AppInfo appInfo, DeviceInfo deviceInfo)
    {
        mRepo = exceptionsRepo;
        mAppInfo = appInfo;
        mDeviceInfo = deviceInfo;
    }

    @Override
    public void run() {

        CatchItException[] catchItExceptions = mRepo.getAllPendingExceptions();

        if(catchItExceptions.length > 0){
            mRepo.markExceptionsTransit(catchItExceptions);

            mRepo.syncExceptions(new SyncExceptionRequestBody(mAppInfo, mDeviceInfo, catchItExceptions));
        }
    }


    public static class Builder {

        private ExceptionsRepo mRepo;
        private AppInfo mAppInfo;
        private DeviceInfo mDeviceInfo;

        public SyncExceptionsWorker.Builder setRepo(ExceptionsRepo repo) {
            mRepo = repo;
            return this;
        }

        public SyncExceptionsWorker.Builder setAppInfo(AppInfo appInfo) {
            mAppInfo = appInfo;
            return this;
        }

        public SyncExceptionsWorker.Builder setDeviceInfo(DeviceInfo deviceInfo) {
            mDeviceInfo = deviceInfo;
            return this;
        }


        public SyncExceptionsWorker build() {

            if (mRepo == null) {
                throw new IllegalArgumentException("You must provide Repo in SyncExceptionsWorker");
            }

            if (mAppInfo == null) {
                throw new IllegalArgumentException("You must provide AppInfo in SyncExceptionsWorker");
            }

            if (mDeviceInfo == null) {
                throw new IllegalArgumentException("You must provide DeviceInfo in SyncExceptionsWorker");
            }

            return new SyncExceptionsWorker(mRepo, mAppInfo, mDeviceInfo);
        }
    }
}
