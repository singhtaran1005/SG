package com.studgenie.app.ui.onboarding.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.studgenie.app.R
import com.studgenie.app.data.model.SendUserDetails
import com.studgenie.app.data.model.UserDetailsApiResponse
import com.studgenie.app.ui.ApiService.SignUpApi
import com.studgenie.app.ui.onboarding.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_sign_up_3_test.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignUp3Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sign_up_3_test, container, false)
        val submit_button = rootView.findViewById<Button>(R.id.submit_button)
        submit_button.setOnClickListener {
            val i = Intent(activity, HomeActivity::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }


        rootView.skip_button.setOnClickListener {
            val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.43.217:3000")
//                .baseUrl("http://sg-backend-dev.ap-south-1.elasticbeanstalk.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


            val createDetailsApi = retrofit.create(SignUpApi::class.java)
            val sendDetails = SendUserDetails("Ankan","aa@bb","12345","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJudW1iZXIiOiI3OTkyOTgyMDM4IiwiaWF0IjoxNjAzODA5MTI2fQ.XyVd0uiTLCDFRHu3dwu4SsyUEes35Vzxb-QjMiROYV0")
            createDetailsApi.userDetails(sendDetails).enqueue(object: Callback<UserDetailsApiResponse>{
                override fun onResponse(call: Call<UserDetailsApiResponse>, response: Response<UserDetailsApiResponse>) {
                    Log.d("Retrofit2", "OnResponse: ${response.body()?.toString()} \n"
                            + "Response Code: ${response.code()}\n")
                }

                override fun onFailure(call: Call<UserDetailsApiResponse>, t: Throwable) {
                    Log.d("Retrofit2", "OnFailure")
                }
            })
        }

        return rootView
    }
}