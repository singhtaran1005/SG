package com.studgenie.app.ui.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthToken(
    var authToken:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}