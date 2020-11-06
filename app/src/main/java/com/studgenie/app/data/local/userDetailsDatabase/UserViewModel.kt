package com.studgenie.app.data.local.userDetailsDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class UserViewModel {
//}

class UserViewModel(application: Application): AndroidViewModel(application) {
    var readAllData: LiveData<List<UserData>>?=null
    private var repository: UserRepository?=null

    init {
        val userDao = UserDatabase.getDatabase(
            application
        ).getUserDataDao()
        repository = UserRepository(userDao)
        readAllData = repository!!.readAllData
    }

    fun addUserData(userData:UserData){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.addUserData(userData)
        }
    }

    fun updateUserdata(userData:UserData){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.updateUserdata(userData)
        }
    }

    fun update(username:String,email:String,pid:Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.update(username, email, pid)
        }
    }
}