package com.studgenie.app.data.local.tokenDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(application: Application):AndroidViewModel(application) {
    var readAllData: LiveData<List<AuthToken>>?=null
    private var repository: AuthRepository?=null

    init {
        val authDao = AuthDatabase.getDatabase(
            application
        ).getAuthDao()
        repository = AuthRepository(authDao)
        readAllData = repository!!.readAllData
    }

    fun addToken(authToken: AuthToken){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.addToken(authToken)
        }
    }

    fun updateToken(authToken: AuthToken){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.updateToken(authToken)
        }
    }

    fun update(token:String,pid:Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.update(token, pid)
        }
    }
}