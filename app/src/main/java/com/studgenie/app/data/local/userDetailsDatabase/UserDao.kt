package com.studgenie.app.data.local.userDetailsDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun addUserData(userData: UserData)

    @Update
    suspend fun updateUserdata(userData: UserData)

    @Query("SELECT * FROM UserData ORDER BY id DESC")
    fun getUserData(): LiveData<List<UserData>>
}