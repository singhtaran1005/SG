package com.studgenie.app.ui.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AuthToken ::class],version = 1)
abstract class AuthDatabase:RoomDatabase() {
    abstract fun getAuthDao():AuthDao


    companion object{
        @Volatile private var instance:AuthDatabase?=null
        private val Lock = Any()

        operator fun invoke(context: Context) = instance?: synchronized(Lock){
            instance ?: buildDatabase(context).also{
                instance=it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
         context.applicationContext,
            AuthDatabase::class.java,
            "authdatabase"
        ).build()
    }
}