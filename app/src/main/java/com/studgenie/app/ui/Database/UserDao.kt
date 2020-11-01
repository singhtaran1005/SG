//package com.studgenie.app.ui.Database
//
//import androidx.lifecycle.LiveData
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//
//interface UserDao {
//    @Insert
//    suspend fun addUser(user :User)
//
//    @Query("SELECT * FROM `user-data` ORDER BY id ASC")
//    suspend fun readAllData():List<User>
//}