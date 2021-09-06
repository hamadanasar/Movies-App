package com.example.moviesapp.apis

import com.example.moviesapp.ui.models.NowPlayingResponse
import com.example.moviesapp.ui.models.TopRatedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String?
    ): Response<NowPlayingResponse>

    @GET("top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String?
    ): Response<TopRatedResponse?>?

}