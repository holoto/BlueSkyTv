package com.example.holoto.blueskytv.UrlDownload;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface DownloadJson
        {
        @Streaming
        @GET("{filename}")
        Call<mybody> Download(@Path("filename") String filename);
        }