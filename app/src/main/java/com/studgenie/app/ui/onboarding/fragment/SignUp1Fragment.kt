package com.studgenie.app.ui.onboarding.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.studgenie.app.R
import com.studgenie.app.data.remote.ApiCountryService
import com.studgenie.app.util.InternetConnectivity
import com.studgenie.app.data.model.CountryItem
import com.studgenie.app.ui.main.activity.HomeActivity
import com.studgenie.app.ui.onboarding.adapter.CountrySpinnerAdapter
import com.studgenie.app.util.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class SignUp1Fragment : Fragment() {
    lateinit var phoneNumberEditText: EditText
    lateinit var continueButton: Button
    lateinit var countryCode: String
    lateinit var toastMessage: TextView
    lateinit var exploreTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_sign_up_1, container, false)
        phoneNumberEditText = rootView.findViewById(R.id.edit_text_phone)
        continueButton = rootView.findViewById(R.id.textView_continue_button)
        toastMessage = rootView.findViewById(R.id.toast_message_1st_signup_fragment)
        exploreTextView = rootView.findViewById<TextView>(R.id.textView_explore)

        val spinner = rootView.findViewById<Spinner>(R.id.spinner_countries)

        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.43.217:3000")
//            .baseUrl("http://sg-backend-dev.ap-south-1.elasticbeanstalk.com")
            .baseUrl(Config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiCountryService::class.java)
        api.fetchAllCountries().enqueue(object : Callback<List<CountryItem>> {
            override fun onResponse(
                call: Call<List<CountryItem>>,
                response: Response<List<CountryItem>>
            ) {
                val adapter = CountrySpinnerAdapter(
                    requireContext(),
                    response.body() as ArrayList<CountryItem>
                )
                spinner.adapter = adapter
                Log.d("Retrofit1", "OnResponse: ${response.body()!![0].code}")
            }

            override fun onFailure(call: Call<List<CountryItem>>, t: Throwable) {
                Log.d("Retrofit1", "OnFailure")
            }
        })

        continueButton.setOnClickListener(View.OnClickListener {
            if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(
                    requireContext()
                )
            ) {

                val storePhoneNo = phoneNumberEditText.text.toString()
                if (storePhoneNo.matches("".toRegex())) {
                    toastMessage.visibility = View.VISIBLE
                    toastMessage.text = "Enter your number first"
                    toastMessage.setBackgroundResource(R.color.transparent_red)
                } else {
                    val signUp2Fragment = SignUp2Fragment()
                    val args = Bundle()
                    args.putString("phNo", phoneNumberEditText.text.toString())
//                        args.putString("isoCode", "91")
                    signUp2Fragment.arguments = args
                    fragmentManager!!.beginTransaction()
                        .replace(R.id.signup_fragment_container, signUp2Fragment).commit()
                }
            } else {
                toastMessage.visibility = View.VISIBLE
                toastMessage.text = "Check Your Internet Connection"
                toastMessage.setBackgroundResource(R.color.transparent_red)
            }
        })
        exploreTextView.setOnClickListener {
            val i = Intent(activity, HomeActivity::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
            activity?.finish()
        }
        return rootView
    }


}