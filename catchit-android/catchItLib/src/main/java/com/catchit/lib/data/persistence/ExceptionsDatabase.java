package com.catchit.lib.data.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.catchit.lib.models.CatchItException;

/**
 * ExceptionsDatabase Room Database
 *
 * Room queries are thread safe and blocking, meaning no collision can happen if two queries run the same time
 *  also, we're working with database only from our AppExecutors.diskIO thread pool in a serial manner
 */
@Database(entities = {CatchItException.class}, version = 1, exportSchema = false)
public abstract class ExceptionsDatabase extends RoomDatabase {

    private final static String DB_FILE_NAME = "exceptions-db";

    public abstract ExceptionsDao exceptionsDao();

    public static class Builder {

        private Context mApplicationContext = null;

        public ExceptionsDatabase.Builder setApplicationContext(Context context) {
            mApplicationContext = context;
            return this;
        }

        public ExceptionsDatabase build() {

            if (mApplicationContext == null) {
                throw new IllegalArgumentException("You must provide Application Context in ExceptionsDatabase");
            }

            return Room.databaseBuilder(
                    mApplicationContext,
                    ExceptionsDatabase.class,
                    DB_FILE_NAME).build();
        }
    }
}
