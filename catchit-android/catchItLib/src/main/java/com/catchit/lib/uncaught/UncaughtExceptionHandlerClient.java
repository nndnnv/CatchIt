package com.catchit.lib.uncaught;

import androidx.annotation.NonNull;

/**
 * UncaughtExceptionHandler to implement Thread.UncaughtExceptionHandler on the JVM
 */
public class UncaughtExceptionHandlerClient implements Thread.UncaughtExceptionHandler {

    private final UncaughtExceptionListener mExceptionHandler;

    private UncaughtExceptionHandlerClient(UncaughtExceptionListener handler) {
        mExceptionHandler = handler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        mExceptionHandler.onExceptionThrown(e);
    }

    /**
     * Remove Default Uncaught Exception Handler
     */
    public void remove(){
        Thread.setDefaultUncaughtExceptionHandler(null);
    }

    public static class Builder {

        private UncaughtExceptionListener mListener;

        public Builder() {
        }

        public Builder setExceptionListener(UncaughtExceptionListener listener) {
            mListener = listener;
            return this;
        }

        public UncaughtExceptionHandlerClient build() {
            if (mListener == null) {
                throw new IllegalArgumentException("You must provide Listener in UncaughtExceptionHandlerClient");
            }
            return new UncaughtExceptionHandlerClient(mListener);
        }
    }
}
