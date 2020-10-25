package com.catchit.lib.sync.notifier;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.TimeUnit;

/**
 * SyncNotifier is providing Event when to sync with backend, see SyncNotifier.Builder to change interval settings
 *
 *
 * There are many options to run periodic tasks in Android, for this scenario I chose to use the 'old' but good Handler and ThreadHandler mechanism with the help of postDelayed method.
 * As it allows me to run in a genuine serial manner (limit IO collisions) with a few lines and simplicity
 *
 *
 * Other alternatives to this solution:
 * WorkManager - has a minimum interval of 15 minutes
 * ScheduledThreadPoolExecutor - good solution but the first task will have to wait 1 minutes before it starts, so I need to subclass and adds complexity
 * AlarmManager - overkill and will produce boilerplate code
 */
public class SyncNotifier {

    private final static String HANDLER_THREAD_NAME = "SyncNotifierThread";

    private final SyncNotifierListener mListener;
    private final int mSyncInterval;
    private final TimeUnit mSyncIntervalTimeUnit;

    HandlerThread mHandlerThread;
    Handler mHandler;
    Runnable mNetworkSyncTask;

    public SyncNotifier(SyncNotifierListener listener, int syncInterval, TimeUnit timeUnitInterval)
    {
        mListener = listener;
        mSyncInterval = syncInterval;
        mSyncIntervalTimeUnit = timeUnitInterval;
    }

    public void start()
    {
        mHandlerThread = new HandlerThread(HANDLER_THREAD_NAME);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        mNetworkSyncTask = new Runnable() {
            @Override
            public void run() {

                mListener.onSyncNeeded();

                mHandler.postDelayed(this, mSyncIntervalTimeUnit.toMillis(mSyncInterval));
            }
        };

        mHandler.post(mNetworkSyncTask);
    }

    public void stop() {
        mHandlerThread.quitSafely();
        mHandlerThread = null;
        mHandler.removeCallbacks(mNetworkSyncTask);
        mHandler = null;
        mNetworkSyncTask = null;
    }

    public static class Builder {

        private SyncNotifierListener mListener;
        private int mSyncInterval;
        private TimeUnit mSyncIntervalTimeUnit;

        public SyncNotifier.Builder setListener(SyncNotifierListener listener) {
            mListener = listener;
            return this;
        }

        public SyncNotifier.Builder setInterval(int syncInterval) {
            if (syncInterval <= 0) {
                throw new IllegalArgumentException("Sync interval must be greater than zero");
            }

            mSyncInterval = syncInterval;

            return this;
        }

        public SyncNotifier.Builder setIntervalTimeUnit(TimeUnit syncIntervalTimeUnit) {
            if(syncIntervalTimeUnit == null) {
                throw new IllegalArgumentException("You must provide TimeUnit in SyncNotifier");
            }

            mSyncIntervalTimeUnit = syncIntervalTimeUnit;
            return this;
        }

        public SyncNotifier build() {

            if (mListener == null) {
                throw new IllegalArgumentException("You must provide Listener in SyncNotifier");
            }

            if(mSyncInterval > 0) {
                if (mSyncIntervalTimeUnit == null) {
                    throw new IllegalArgumentException("You must provide TimeUnit in case you provide custom sync interval");
                }
            }

            return new SyncNotifier(mListener, mSyncInterval, mSyncIntervalTimeUnit);
        }
    }
}
