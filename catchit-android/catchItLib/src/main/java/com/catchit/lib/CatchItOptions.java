package com.catchit.lib;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * CatchItOptions class holds information for initialising CatchIt library with custom or default values, such as server address and sync intervals
 */
public class CatchItOptions {

    private CatchItOptions(String serverAddress, int syncInterval, TimeUnit timeUnitInterval) {
        this.serverAddress = serverAddress;
        this.syncInterval = syncInterval;
        this.syncIntervalTimeUnit = timeUnitInterval;
    }

    protected final int syncInterval;
    protected final TimeUnit syncIntervalTimeUnit;
    protected final String serverAddress;

    public static class Builder {

        private final List<TimeUnit> SUPPORTED_TIME_UNITS = Arrays.asList(TimeUnit.SECONDS, TimeUnit.MINUTES);
        private String mServerAddress = "http://localhost";
        private int mSyncInterval = 1;
        private TimeUnit mSyncIntervalTimeUnit = TimeUnit.MINUTES;

        public CatchItOptions.Builder setSyncInterval(int syncInterval, TimeUnit syncIntervalTimeUnit) {
            if (syncInterval <= 0) {
                throw new IllegalArgumentException("Sync interval must be greater than zero");
            }

            if(syncIntervalTimeUnit == null) {
                throw new IllegalArgumentException("You must provide TimeUnit in CatchItOptions");
            }

            if(!SUPPORTED_TIME_UNITS.contains(syncIntervalTimeUnit)) {
                throw new IllegalArgumentException("CatchIt supports only TimeUnit.MINUTES and TimeUnit.SECONDS as for now");
            }

            mSyncInterval = syncInterval;
            mSyncIntervalTimeUnit = syncIntervalTimeUnit;

            return this;
        }

        public CatchItOptions.Builder setServerAddress(String serverAddress) {
            if(serverAddress == null) {
                throw new IllegalArgumentException("You must provide serverAddress in CatchItOptions");
            }
            mServerAddress = serverAddress;
            return this;
        }

        public CatchItOptions build() {

            if(mSyncInterval > 0) {
                if (mSyncIntervalTimeUnit == null) {
                    throw new IllegalArgumentException("You must provide TimeUnit in case you provide custom sync interval");
                }
            }

            return new CatchItOptions(mServerAddress, mSyncInterval, mSyncIntervalTimeUnit);
        }
    }
}
