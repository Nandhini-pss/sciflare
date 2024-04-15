package com.example.sciflareapplication.utils

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sciflareapplication.model.UserReq
import com.example.sciflareapplication.model.UserResponse
import com.example.sciflareapplication.room.UserDao
import com.example.sciflareapplication.room.UserEntity
import retrofit2.Response
import okhttp3.ResponseBody
import org.json.JSONObject

class UserRepository(private val apiService: ApiService, private val userDao: UserDao) {

    suspend fun fetchAndStoreAllUsers() {
        try {
            // Fetch users from the API
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                val usersResponse: List<UserResponse> = response.body() ?: emptyList()

                // Convert UserResponse objects to UserEntity objects
                val usersEntities: List<UserEntity> = usersResponse.map { userResponse ->
                    UserEntity(
                        name = userResponse.name,
                        email = userResponse.email,
                        mobile = userResponse.mobile,
                        gender = userResponse.gender,
                        _id = userResponse._id
                    )
                }

                // Insert the list of UserEntity objects into the database
                userDao.insertUsersList(usersEntities)
            } else {
                Log.e("UserRepository", "API request failed with code ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error: ${e.message}")
        }
    }

    suspend fun postDataAndStoreInDatabase(user: UserReq) {
        try {
            // Make network request and get response
            val response = apiService.postData(user)
            if (response.isSuccessful) {
                val responseBody = response.body()?.string() ?: return
                // Parse the response body string to extract _id or any other relevant data
                val userId = JSONObject(responseBody).getString("_id")
                // Store data in local database
                val userEntity = UserEntity(
                    name = user.name,
                    email = user.email,
                    gender = user.gender,
                    mobile = user.mobile,
                    _id = userId
                )
                userDao.insertUser(userEntity)
            } else {
                Log.e("UserRepository", "API request failed with code ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error: ${e.message}")
        }
    }

    fun getUsersFromDatabase(): List<UserEntity> {
        return userDao.getUsers()
    }
}