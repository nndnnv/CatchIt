package com.catchit.lib.data.network.client;

import com.catchit.lib.data.network.request.SyncExceptionRequestBody;
import com.catchit.lib.data.network.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExceptionsAPI {

    @POST("api/exceptions")
    Call<ApiResponse> sendExceptions(@Body SyncExceptionRequestBody synRequestBody);
}
