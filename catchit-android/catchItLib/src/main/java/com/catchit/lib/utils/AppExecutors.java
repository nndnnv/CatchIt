package com.catchit.lib.utils;

import com.catchit.lib.CatchIt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class to run disk operation on a dedicated thread pool
 * Network operations run in Retrofit custom Dispatcher (based on Executors)
 *
 * As this is POC level project and NOT production I made assumptions that allow me to solve some issues in a fast and neat way.
 * On production level project I would extend and dive into Shutting down SAFELY the DiskIO Executor to guarantee that every running task being stopped
 * as the shutdownNow() function is not 100% solution (according to the doc):
 *
 * "There are no guarantees beyond best-effort attempts to stop processing actively executing tasks.
 * For example, typical implementations will cancel via Thread.interrupt, so any task that fails to respond to interrupts may never terminate."
 *
 * So in order to guarantee 100% solution I'll have to create custom executor with "Interruptable" (will handle Thread.interrupt properly)
 * runnable to make sure all active tasks is safely terminated
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind webservice requests).
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
     * In case we shutdown diskIO executor (system about to terminate) we're discarding any incoming tasks
     * @param r incoming task
     */
    public void executeOnDiskIO(Runnable r) {
        if(!diskIO().isShutdown()){
            diskIO().execute(r);
        }
    }

    /**
     * Shutting down diskIO executor (system about to terminate)
     */
    public void shutdownDiskIO() {
        diskIO().shutdownNow();
    }

    /**
     * Run task one time on disposable thread pool
     * @param r task
     */
    public void runOnce(Runnable r) {
        Executors.newSingleThreadExecutor().execute(r);
    }
}
