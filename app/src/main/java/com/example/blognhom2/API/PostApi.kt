package com.example.blognhom2.API

import com.example.blognhom2.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {
//    recommended posts
    @GET("posts/recommendedPosts")
    fun getRecommendedPosts(): Call<List<PostInfo>>
//    categories
    @GET("posts/categories")
    fun getCategories(): Call<List<Category>>
//    search posts
    @GET("posts/search")
    fun searchPosts(@Query("page") page: Int, @Query("l") letter: String): Call<List<PostInfo>>
    @GET("posts/search/{category}")
    fun searchPostsByCategory(@Path("category") category: String, @Query("page") page: Int, @Query("l") letter: String): Call<List<PostInfo>>
//get posts
    @GET("posts")
    fun getPosts(@Query("page") page: Int): Call<List<PostInfo>>
//get posts by category
    @GET("/posts/category/{category}")
    fun getPostsByCategory(@Path("category") category: String, @Query("page") page: Int): Call<List<PostInfo>>
//get all posts
    @GET("posts/detail/{postID}")
    fun getPostDetail(@Path("postID") postID: Int): Call<PostDetail>
}