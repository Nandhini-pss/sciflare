package com.example.sciflareapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sciflareapplication.model.UserResponse


@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers():List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsersList(users: List<UserEntity>)

    @Query("DELETE FROM users WHERE _id= :id")
    fun deleteById(id:String)

    @Query("SELECT * FROM users where _id=:id")
    fun getParticularUserDetails(id:String):UserEntity

    @Query("UPDATE users SET name= :name1, email= :email1,mobile=:mobile1,gender=:gender1 WHERE _id =:id1")
    fun updateUserData(
        name1:String,
        email1:String,
        mobile1:String,
        gender1:String,
        id1:String)
}