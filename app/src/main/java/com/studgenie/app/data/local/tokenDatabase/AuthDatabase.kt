package com.studgenie.app.data.local.tokenDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AuthToken ::class],version = 1,exportSchema = false)
abstract class AuthDatabase:RoomDatabase() {
    abstract fun getAuthDao(): AuthDao
    companion object{
        @Volatile private var INSTANCE: AuthDatabase?=null
        fun getDatabase(context: Context): AuthDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuthDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}