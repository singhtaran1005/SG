package com.studgenie.app.ui.onboarding.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.studgenie.app.R
import com.studgenie.app.data.local.tokenDatabase.AuthViewModel
import com.studgenie.app.ui.main.activity.HomeActivity
import androidx.lifecycle.Observer


class SplashActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    var isTokenEmpty = 1

    private val SPLASH_DISPLAY_LENGTH = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("Splash","Inside Splash Activity")

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        authViewModel.readAllData?.observe(this, Observer{ auth->
            if (auth.isEmpty()){
                isTokenEmpty=1
                Log.d("Splash1","List is empty")
            }else{
                isTokenEmpty=0
                Log.d("Splash1",auth[0].id.toString()+auth[0].authToken)
            }
        })

//        userViewModel.readAllData?.observe(this, Observer{ user->
//            if (user.isEmpty()){
//                isUserEmpty=1
//                Log.d("Splash2","List is empty")
//            }else{
//                isUserEmpty=0
//                Log.d("Splash2",user[0].id.toString()+user[0].number)
//            }
//        })


















        Handler().postDelayed(Runnable { /* Create an Intent that will start the Menu-Activity. */

           if(isTokenEmpty == 1){
               val signupActivity = Intent(this, SignUpActivity::class.java)
               this.startActivity(signupActivity)
               this.finish()
           }else{
               val homeActivity = Intent(this, HomeActivity::class.java)
               this.startActivity(homeActivity)
               this.finish()
           }





//            val signupActivity = Intent(this, HomeActivity::class.java)
//            this.startActivity(signupActivity)
//            this.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

}