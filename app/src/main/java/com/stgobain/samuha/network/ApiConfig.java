package com.stgobain.samuha.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {
    @Multipart
    @POST("postmemoriesios")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("user_id") RequestBody userID, @Part("event_type") RequestBody eventTypes,@Part("event_contest_id") RequestBody eventId,@Part("file_type") RequestBody fileType);
    @Multipart
    @POST("postsabios")
    Call<ServerResponse> uploadAuditions(@Part MultipartBody.Part file,
                                    @Part("user_id") RequestBody userID, @Part("audition_type") RequestBody auditionType,@Part("dependent_type") RequestBody dependentType,@Part("file_type") RequestBody fileType,@Part("short_description") RequestBody shortDesc);

    @Multipart
    @POST("postmemoriesios")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,
                                       @Part MultipartBody.Part file2);
}
