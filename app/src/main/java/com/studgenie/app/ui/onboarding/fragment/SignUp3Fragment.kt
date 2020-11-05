package com.studgenie.app.ui.onboarding.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.studgenie.app.R
import com.studgenie.app.data.model.SendUserDetails
import com.studgenie.app.data.remote.UserDetailsApiResponse
import com.studgenie.app.data.remote.SignUpApi
import com.studgenie.app.data.local.tokenDatabase.AuthViewModel
import com.studgenie.app.ui.main.activity.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.studgenie.app.data.local.userDetailsDatabase.UserData
import com.studgenie.app.data.local.userDetailsDatabase.UserViewModel
import com.studgenie.app.util.Config
import kotlinx.android.synthetic.main.fragment_sign_up_3.view.*


class SignUp3Fragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel
    var isTokenEmpty = 1
    var isUserEmpty:Int = 1

    lateinit var enterNameString: String
    lateinit var enterEmailString: String
    lateinit var enterPasswordString: String
    lateinit var enterConfirmPasswordString: String
    lateinit var authToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        val rootView = inflater.inflate(R.layout.fragment_sign_up_3, container, false)
        val submitButton = rootView.findViewById<Button>(R.id.submit_button)
        val skipButton = rootView.findViewById<Button>(R.id.skip_button)
        val toastMessage = rootView.findViewById<TextView>(R.id.toast_message_3rd_signup_fragment)
        toastMessage.visibility = View.INVISIBLE

        enterNameString = rootView.enter_name.text.toString().trim()
        enterEmailString = rootView.enter_email.text.toString().trim()
        enterPasswordString = rootView.enter_password.text.toString().trim()
        enterConfirmPasswordString = rootView.confirm_password.text.toString().trim()

        skipButton.setOnClickListener {
            val i = Intent(activity, HomeActivity::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)

            activity?.finish()
        }


            authViewModel.readAllData?.observe(viewLifecycleOwner, Observer{ auth->
                if (auth.isEmpty()){
                    isTokenEmpty = 1
                    Log.d("Coroutine","List is empty")
                }else{
                    isTokenEmpty = 0
                    Log.d("Coroutine",auth[0].id.toString()+auth[0].authToken)
                    authToken = auth[0].authToken
                }
        })

        userViewModel.readAllData?.observe(viewLifecycleOwner, Observer{ user->
            if (user.isEmpty()){
                isUserEmpty = 1
                Log.d("Coroutine1","List is empty")
            }else{
                isUserEmpty = 0
                Log.d("Coroutine1",user[0].id.toString()+user[0].number)
            }
        })




        submitButton.setOnClickListener {
            enterNameString = rootView.enter_name.text.toString().trim()
            enterEmailString = rootView.enter_email.text.toString().trim()
            enterPasswordString = rootView.enter_password.text.toString().trim()
            enterConfirmPasswordString = rootView.confirm_password.text.toString().trim()

            if (!enterNameString.isEmpty() && !enterEmailString.isEmpty() && !enterPasswordString.isEmpty()){
                if (enterPasswordString.equals(enterConfirmPasswordString)){
                    toastMessage.visibility = View.INVISIBLE


                    val retrofit = Retrofit.Builder()
//                .baseUrl("http://192.168.43.217:3000")
//                .baseUrl("http://sg-backend-dev.ap-south-1.elasticbeanstalk.com")
                        .baseUrl(Config.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val createDetailsApi = retrofit.create(SignUpApi::class.java)
                    if (authToken != null){
                val sendDetails = SendUserDetails(enterNameString,enterEmailString,enterPasswordString,authToken)
//                        val sendDetails = SendUserDetails("aaaaaa","aa@bb","12345","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJudW1iZXIiOiI3OTkyOTgyMDM4IiwiaWF0IjoxNjAzODA5MTI2fQ.XyVd0uiTLCDFRHu3dwu4SsyUEes35Vzxb-QjMiROYV0")
                        createDetailsApi.userDetails(sendDetails).enqueue(object :
                            Callback<List<UserDetailsApiResponse>> {
                            override fun onResponse(
                                call: Call<List<UserDetailsApiResponse>>,
                                response: Response<List<UserDetailsApiResponse>>
                            ) {
                                Log.d(
                                    "Retrofit3", "OnResponse: ${response.body()?.get(0)?.number} \n"
                                            + "Response Code: ${response.code()}\n"
                                )
                                val mUserData = UserData(
                                    response.body()?.get(0)?.number.toString(),
                                    response.body()?.get(0)?.first_name.toString(),
                                    response.body()?.get(0)?.last_name.toString(),
                                    response.body()?.get(0)?.dob.toString(),
                                    response.body()?.get(0)?.picture_url.toString(),
                                    response.body()?.get(0)?.account_status.toString(),
                                    response.body()?.get(0)?.max_devices!!.toInt(),
                                    response.body()?.get(0)?.user_name.toString(),
                                    response.body()?.get(0)?.student_id!!.toInt(),
                                    response.body()?.get(0)?.institute_id.toString(),
                                    response.body()?.get(0)?.email.toString()
                                )

                                if (isUserEmpty == 1) {
                                    userViewModel.addUserData(mUserData)
                                    Log.d("Coroutine1", "Successfully added!")

                                    val i = Intent(activity, HomeActivity::class.java)
                                    startActivity(i)
                                    (activity as Activity?)!!.overridePendingTransition(0, 0)

                                    userViewModel.readAllData?.observe(viewLifecycleOwner, Observer { user ->
                                            Log.d("Coroutine1", user[0].id.toString() + user[0].number.toString() + user[0].firstName.toString() + user[0].lastName.toString() + user[0].dob.toString() + user[0].pictureUrl.toString() + user[0].accountStatus.toString() + user[0].maxDevices.toString() + user[0].userName.toString() + user[0].studentId.toString() + user[0].instituteId.toString() + user[0].email.toString())
                                            Toast.makeText(requireContext(),user[0].id.toString() + user[0].number.toString() + user[0].firstName.toString() + user[0].lastName.toString() + user[0].dob.toString() + user[0].pictureUrl.toString() + user[0].accountStatus.toString() + user[0].maxDevices.toString() + user[0].userName.toString() + user[0].studentId.toString() + user[0].instituteId.toString() + user[0].email.toString() , Toast.LENGTH_SHORT).show()
                                        })
                                    activity?.finish()
                                } else {
                                    userViewModel.updateUserdata(mUserData)
                                    Log.d("Coroutine1", "Successfully updated!")

                                    val i = Intent(activity, HomeActivity::class.java)
                                    startActivity(i)
                                    (activity as Activity?)!!.overridePendingTransition(0, 0)
                                    userViewModel.readAllData?.observe(viewLifecycleOwner, Observer { user ->
                                        Log.d("Coroutine1", user[0].id.toString() + user[0].number.toString() + user[0].firstName.toString() + user[0].lastName.toString() + user[0].dob.toString() + user[0].pictureUrl.toString() + user[0].accountStatus.toString() + user[0].maxDevices.toString() + user[0].userName.toString() + user[0].studentId.toString() + user[0].instituteId.toString() + user[0].email.toString())
                                        Toast.makeText(requireContext(),user[0].id.toString() + user[0].number.toString() + user[0].firstName.toString() + user[0].lastName.toString() + user[0].dob.toString() + user[0].pictureUrl.toString() + user[0].accountStatus.toString() + user[0].maxDevices.toString() + user[0].userName.toString() + user[0].studentId.toString() + user[0].instituteId.toString() + user[0].email.toString() , Toast.LENGTH_SHORT).show()
                                    })

                                    activity?.finish()
                                }
                            }

                            override fun onFailure(
                                call: Call<List<UserDetailsApiResponse>>,
                                t: Throwable
                            ) {
                                Log.d(
                                    "Retrofit3", "OnFailure \n"
                                            + "Response Code:" + t.message
                                            + "\n" + t.cause
                                            + "\n" + t.printStackTrace().toString()
                                )
                            }
                        })
                    }else{
                        Log.d("Coroutine","Auth token is empty")
                    }
                }else{
                    toastMessage.visibility = View.VISIBLE
                    toastMessage.text = "Password Not Matched"
                    toastMessage.setBackgroundResource(R.color.transparent_red)
                }
            }else{
                    toastMessage.visibility = View.VISIBLE
                    toastMessage.text = "Please enter all details"
                    toastMessage.setBackgroundResource(R.color.transparent_red)
            }
        }
//        mAuthViewModel.readAllData?.observe(viewLifecycleOwner, Observer{auth->
//            Log.d("Coroutine",auth[0].id.toString()+auth[0].authToken)
//        })

//        mUserViewModel.readAllData?.observe(viewLifecycleOwner, Observer{user->
//            Log.d("Coroutine1",user[0].id.toString()+user[0].number.toString()+user[0].firstName.toString()+user[0].lastName.toString()+user[0].dob.toString()+user[0].pictureUrl.toString()+user[0].accountStatus.toString()+user[0].maxDevices.toString()+user[0].userName.toString()+user[0].studentId.toString()+user[0].instituteId.toString()+user[0].email.toString())
//
//        })
        return rootView
    }
}