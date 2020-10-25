package com.catchit.lib.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class to run disk operation on a dedicated thread pool
 * Network operations run in Retrofit custom Dispatcher (based on Executors)
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind http requests and vice versa).
 *
 *
 */
public class AppExecutors {

    private final ExecutorService diskIO;

    public AppExecutors() {
        this.diskIO = Executors.newSingleThreadExecutor();
    }

    private ExecutorService diskIO() {
        return diskIO;
    }

    /**
     * Execute task on DiskIO thread pool
     * @param r incoming task
     */
    public void executeOnDiskIO(Runnable r) {
        diskIO().execute(r);
    }
}
