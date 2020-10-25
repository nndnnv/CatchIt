package com.catchit.lib.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * CatchItException data model
 */

@Entity
public class CatchItException {

    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * Class canonical name (where Exception raised)
     */
    public String canonicalName;

    /**
     * Exception message
     */
    public String message;

    /**
     * Exception Stacktrace
     */
    public String stackTrace;

    /**
     * Method where Exception raised
     */
    public String methodName;

    /**
     * Line number where Exception raised
     */
    public int lineNumber;

    /**
     * Exception timestamp
     */
    public long timestamp;

    /**
     * Detect if current Exception is on the way to server
     * defaults to false
     */
    public boolean inTransit;

    public CatchItException() {

    }

    public CatchItException(Throwable exception) {
        message = exception.getMessage();
        stackTrace = getStackTrace(exception);
        StackTraceElement lastExceptionElement = exception.getStackTrace()[0];
        canonicalName = lastExceptionElement.getClassName();
        methodName = lastExceptionElement.getMethodName();
        lineNumber = lastExceptionElement.getLineNumber();
        timestamp = System.currentTimeMillis();
        inTransit = false;
    }

    private String getStackTrace(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public static class Builder {

        private Throwable mThrowable = null;

        public CatchItException.Builder setThrowable(Throwable t) {
            mThrowable = t;
            return this;
        }

        public CatchItException build() {
            if (mThrowable == null) {
                throw new IllegalArgumentException("You must provide Throwable in CatchItException");
            }
            return new CatchItException(mThrowable);
        }
    }

}
