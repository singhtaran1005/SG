package com.studgenie.app.ui.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AuthDao{
    @Insert
    suspend fun addToken(authToken: AuthToken)

    @Query("SELECT * FROM AuthToken ORDER BY id DESC")
    suspend fun getAuthToken():List<AuthToken>
}