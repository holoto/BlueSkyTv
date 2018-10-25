package com.example.holoto.blueskytv.UrlDownload;

import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownService
{
    @Streaming
    @GET("{filename}")
    Call<ResponseBody> Download(@Path("filename") String filename);
//    Call<String> Download(@Path("filename") String filename);
}
