package com.catchit.lib;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.catchit.lib.data.ExceptionsRepo;
import com.catchit.lib.models.AppInfo;
import com.catchit.lib.models.CatchItException;
import com.catchit.lib.models.DeviceInfo;
import com.catchit.lib.sync.notifier.SyncNotifier;
import com.catchit.lib.sync.worker.SyncExceptionsWorker;
import com.catchit.lib.uncaught.UncaughtExceptionHandlerClient;
import com.catchit.lib.utils.AppExecutors;

import java.util.concurrent.TimeUnit;

/**
 * CatchIt is a Android Library to monitor exceptions and sync with dedicated node-js server.
 *
 * Support caught and uncaught exceptions
 * Unit & Instrumented tests
 */
public class CatchIt {

    /**
     * Prevent from container app to use this class in unintended manner
     * @throws IllegalAccessException exception
     */
    private CatchIt() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    private static volatile CatchIt sInstance;

     /**
     * Begins handling of any uncaught exceptions. This method should only be called once,
     * in the {@link android.app.Application} class.
      *
      * @param context must be application context
      * @param apiKey API key obtain from CatchIt dashboard
     */
    public static void start(Context context, String apiKey) {
        if (sInstance == null) {
            synchronized(CatchIt.class) {
                if (sInstance == null) {
                    sInstance = new CatchIt(context, apiKey);
                }
            }
        }
    }

    public static void logException(Exception exception) {
        sInstance.saveCaughtException(exception);
    }

    private final UncaughtExceptionHandlerClient mUncaughtExceptionHandlerClient;
    private final SyncNotifier mSyncNotifier;
    private final AppInfo mAppInfo;
    private final DeviceInfo mDeviceInfo;
    private final ExceptionsRepo mExceptionsRepo;
    private final AppExecutors mAppExecutors;

    private CatchIt(Context applicationContext, String apiKey) {

        if (!(applicationContext instanceof Application)) {
            throw new IllegalArgumentException("CatchIt must be initialised with valid application context");
        }

        if (apiKey == null) {
            throw new IllegalArgumentException("CatchIt must be initialised with valid API Key");
        }

        mAppExecutors = new AppExecutors();

        mExceptionsRepo = new ExceptionsRepo.Builder()
                .setApplicationContext(applicationContext)
                .setExecutors(mAppExecutors)
                .build();

        mUncaughtExceptionHandlerClient = new UncaughtExceptionHandlerClient.Builder()
                .setExceptionListener(this::saveUncaughtException).build();

        mAppInfo = new AppInfo.Builder()
                .setApplicationContext(applicationContext).build();

        mDeviceInfo = new DeviceInfo.Builder().build();

        mSyncNotifier = new SyncNotifier.Builder()
                .setInterval(15)
                .setIntervalTimeUnit(TimeUnit.SECONDS)
                .setListener(() ->
                        mAppExecutors.executeOnDiskIO(
                            new SyncExceptionsWorker.Builder().
                                    setRepo(mExceptionsRepo).
                                    setAppInfo(mAppInfo).
                                    setDeviceInfo(mDeviceInfo).build() ))
                .build();

        LifecycleObserver lifecycleObserver = new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            private void onAppBackgrounded() {
                mSyncNotifier.stop();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            private void onAppForeground() {
                mSyncNotifier.start();
            }
        };
        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);
    }

    private void saveCaughtException(Throwable e) {
        mExceptionsRepo.saveException(
                new CatchItException.Builder()
                        .setThrowable(e).build());
    }

    /**
     * We're shutting down DiskIO Executor in order to prevent any new read/write operations
     *
     * I'm aware this is not 100% solution, more info in see AppExecutors
     *
     * Saving uncaught exception and exiting process on completion
     */

    private void saveUncaughtException(Throwable e) {
        mAppExecutors.shutdownDiskIO();

        mExceptionsRepo.saveUncaughtException(
                new CatchItException.Builder()
                        .setThrowable(e).build(), () -> System.exit(1));
    }
}
