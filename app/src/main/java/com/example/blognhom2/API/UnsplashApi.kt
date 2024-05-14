package com.example.blognhom2.API

import com.example.blognhom2.model.Post
import com.example.blognhom2.model.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("search/photos")
    fun searchPhotos(@Query("query") query: String, @Query("client_id") clientId: String): Call<SearchResult>
}