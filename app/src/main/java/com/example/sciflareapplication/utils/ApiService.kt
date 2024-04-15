package com.example.sciflareapplication.utils

import com.example.sciflareapplication.model.UserReq
import com.example.sciflareapplication.model.UserResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("Nandhini")
    suspend fun postData(@Body user: UserReq): Response<ResponseBody>

    @GET("Nandhini") // Modify the endpoint based on your API
    suspend fun getAllUsers(): Response<List<UserResponse>>
}