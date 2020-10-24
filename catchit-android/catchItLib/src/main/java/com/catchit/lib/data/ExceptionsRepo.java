package com.catchit.lib.data;

import android.content.Context;

import com.catchit.lib.data.persistence.ExceptionsDatabase;
import com.catchit.lib.models.CatchItException;
import com.catchit.lib.network.client.ExceptionsAPI;
import com.catchit.lib.network.provider.NetworkProvider;
import com.catchit.lib.network.request.SyncExceptionRequestBody;
import com.catchit.lib.network.response.ApiResponse;
import com.catchit.lib.utils.AppExecutors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Main repository for exception persistence (saving and fetching exceptions)
 * and communicating with ExceptionsAPI client
 */
public class ExceptionsRepo {

    private final AppExecutors mAppExecutors;
    private final ExceptionsAPI mApiClient;
    private final ExceptionsDatabase mDatabase;

    private ExceptionsRepo(Context applicationContext, AppExecutors appExecutors, String serverAddress)
    {
        mAppExecutors = appExecutors;

        // create room database
        mDatabase = new ExceptionsDatabase.Builder()
                .setApplicationContext(applicationContext).build();

        // get retrofit network provider
        NetworkProvider networkProvider = new NetworkProvider.Builder().setServerAddress(serverAddress).build();

        // builder API client using network provider
        mApiClient = networkProvider.getHttpClient().create(ExceptionsAPI.class);
    }

    /**
     * This function may called from the Main Thread (UI) so we need to make sure we're executing it on background thread pool
     * @param catchItException exception to save
     */
    public void saveException(CatchItException catchItException)
    {
        // save exception into local db
        mAppExecutors.executeOnDiskIO(() -> mDatabase.exceptionsDao().insert(catchItException) );
    }

    /**
     * Before saving uncaught exception, we're taking all "in transit" exceptions and updating as "pending"
     * to prevent scenario where we suppose to send them to server but we're unable due to uncaught exception raised
     * @param catchItException uncaught exception to save
     * @param listener listed to notify so we can exit process
     */
    public void saveUncaughtException(CatchItException catchItException, SaveOperationListener listener)
    {
        mAppExecutors.runOnce(() -> {
            // get all exception in transit and update them to transit false, so we'll send again
            CatchItException[] inTransitExceptions = mDatabase.exceptionsDao().getAllTransitExceptions();
            // mark them as "pending" state
            mDatabase.exceptionsDao().updateExceptionsTransitStatus(inTransitExceptions, false);
            // save uncaught exception
            mDatabase.exceptionsDao().insert(catchItException);

            listener.onFinished();
        });
    }

    /**
     * Get all pending exceptions ready for sync with server
     * Since we're only accessing (@methodName getAllPendingExceptions) from SyncExceptionsWorker (running already in DiskIO Executor) we can access it directly
     * @return list of exceptions
     */
    public CatchItException[] getAllPendingExceptions()
    {
        // get all exceptions from local db
        return mDatabase.exceptionsDao().getAllPendingExceptions();
    }

    /**
     * Mark exceptions as in transit prior sending to server
     * Since we're only accessing (@methodName markExceptionsTransit) from SyncExceptionsWorker (running already in DiskIO Executor) we can access it directly
     */
    public void markExceptionsTransit(CatchItException[] exceptions) {
        mDatabase.exceptionsDao().updateExceptionsTransitStatus(exceptions, true);
    }

    /**
     * Delete synced exceptions since we don't need it
     * @param  exceptions exceptions to delete
     */
    private void deleteExceptions(CatchItException[] exceptions) {
        mAppExecutors.executeOnDiskIO(() -> mDatabase.exceptionsDao().delete(exceptions));
    }

    /**
     * Mark exceptions as pending to sync again (due to sync failure)
     * @param exceptions exceptions to update
     */
    private void markExceptionsPending(CatchItException[] exceptions) {
        mAppExecutors.executeOnDiskIO(() -> mDatabase.exceptionsDao().updateExceptionsTransitStatus(exceptions, false));
    }

    /**
     * Sync exceptions with server, we're working with Retrofit Dispatcher here (also build on top of Executors)
     *
     * Improvement: Provide my own custom Executor for Retrofit (Networking) as I want full control on network tasks pooling
     * @param synRequestBody request body
     */
    public void syncExceptions(SyncExceptionRequestBody synRequestBody)  {
        mApiClient.sendExceptions(synRequestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                // on success, delete exception as we don't need them
                deleteExceptions(synRequestBody.exceptions);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // on failure, set the exception as Pending so SyncExceptionsWorker can retry sending on next sync
                markExceptionsPending(synRequestBody.exceptions);
            }
        });
    };

    public interface SaveOperationListener{
        void onFinished();
    }

    public static class Builder {

        private Context mApplicationContext = null;
        private AppExecutors mExecutors = null;
        String mServerAddress;

        public ExceptionsRepo.Builder setServerAddress(String serverAddress) {
            mServerAddress = serverAddress;
            return this;
        }

        public ExceptionsRepo.Builder setApplicationContext(Context context) {
            mApplicationContext = context;
            return this;
        }

        public ExceptionsRepo.Builder setExecutors(AppExecutors executors) {
            mExecutors = executors;
            return this;
        }

        public ExceptionsRepo build() {

            if (mApplicationContext == null) {
                throw new IllegalArgumentException("You must provide Application Context in ExceptionRepo");
            }

            if (mExecutors == null) {
                throw new IllegalArgumentException("You must provide Executors in ExceptionRepo");
            }

            if(mServerAddress == null) {
                throw new IllegalArgumentException("You must provide Server Address in NetworkProvider");
            }

            return new ExceptionsRepo(mApplicationContext, mExecutors, mServerAddress);
        }
    }
}