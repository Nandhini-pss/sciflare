package com.example.sciflareapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sciflareapplication.model.UserReq
import com.example.sciflareapplication.room.UserEntity
import com.example.sciflareapplication.utils.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userData = MutableLiveData<List<UserEntity>>()
    val userData: LiveData<List<UserEntity>> get() = _userData

    init {
        // Call the function to fetch and store all users when initializing the ViewModel
        fetchAndStoreAllUsers()
    }

    fun fetchAndStoreAllUsers() {
        viewModelScope.launch {
            userRepository.fetchAndStoreAllUsers()
        }
    }

    fun postDataAndStoreInDatabase(user: UserReq) {
        viewModelScope.launch {
            try {
                // Post data to API and store the response in the database
                userRepository.postDataAndStoreInDatabase(user)
            } catch (e: Exception) {
                // Handle error
                Log.e("UserViewModel", "Error: ${e.message}")
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _userData.value = userRepository.getUsersFromDatabase()
        }
    }
}