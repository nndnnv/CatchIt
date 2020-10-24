package com.catchit.lib.uncaught;

/**
 * Listener to handles an uncaught exception thrown by an Android application.
 */

public interface UncaughtExceptionListener {

    /**
     * Called whenever an uncaught exception is thrown
     *
     * @param e exception
     */
    void onExceptionThrown(Throwable e);
}
