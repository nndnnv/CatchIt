package com.catchit.lib.network.client;

import com.catchit.lib.network.request.SyncExceptionRequestBody;
import com.catchit.lib.network.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExceptionsAPI {

    @POST("api/exceptions")
    Call<ApiResponse> sendExceptions(@Body SyncExceptionRequestBody synRequestBody);
}
