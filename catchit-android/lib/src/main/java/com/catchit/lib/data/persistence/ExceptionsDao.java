package com.catchit.lib.data.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.catchit.lib.models.CatchItException;

@Dao
public abstract class ExceptionsDao {

    /**
     * Insert CatchItException to DB
     * @param exception exception to insert
     */
    @Insert
    public abstract void insert(CatchItException... exception);

    /**
     * Delete CatchItException from DB
     * @param exception exception to delete
     */
    @Delete
    public abstract void delete(CatchItException... exception);

    /**
     * Fetching all CatchItException that ready to transit
     */
    @Query("Select * FROM CatchItException where inTransit = 0")
    public abstract CatchItException[] getAllPendingExceptions();

    /**
     * Fetching all in Transit CatchItException
     */
    @Query("Select * FROM CatchItException where inTransit = 1")
    public abstract CatchItException[] getAllTransitExceptions();

    /**
     * Update single CatchItException transit state
     */
    @Query("UPDATE CatchItException SET inTransit = :inTransit WHERE id IN (:id)")
    public abstract void updateExceptionTransit(int inTransit, int id);

    /**
     * Transaction to update multiple CatchItException
     */
    @Transaction
    public void updateExceptionsTransitStatus(CatchItException[] exceptions, boolean inTransit) {
        for (CatchItException exception: exceptions) {
            updateExceptionTransit(inTransit ? 1 : 0, exception.id);
        }
    }
}