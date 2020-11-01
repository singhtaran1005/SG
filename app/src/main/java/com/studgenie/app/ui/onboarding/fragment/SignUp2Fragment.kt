package com.studgenie.app.ui.onboarding.fragment

//import com.studgenie.app.data.model.DataManager

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.msg91.sendotpandroid.library.internal.SendOTP
import com.msg91.sendotpandroid.library.listners.VerificationListener
import com.msg91.sendotpandroid.library.roots.RetryType
import com.msg91.sendotpandroid.library.roots.SendOTPConfigBuilder
import com.msg91.sendotpandroid.library.roots.SendOTPResponseCode
import com.studgenie.app.R
import com.studgenie.app.data.model.SendNumber
import com.studgenie.app.data.model.SignUpApiResponse
import com.studgenie.app.ui.ApiService.SignUpApi
import com.studgenie.app.ui.Database.AuthDatabase
import com.studgenie.app.ui.Database.AuthToken

import com.studgenie.app.util.InternetConnectivity
import com.studgenie.app.ui.common.OtpEditText
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


@Suppress("DEPRECATION")
class SignUp2Fragment : BaseFragment(), VerificationListener {

    private var authToken:AuthToken? = null

    lateinit var enterOtpEditText: OtpEditText
    lateinit var verifyAndProceedButton:Button
    lateinit var reSendOtpButton:TextView
    lateinit var otpTimer:TextView
    lateinit var toastMessage:TextView
    lateinit var timer: CountDownTimer
    lateinit var backArrow:Button

    var phone: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_sign_up_2_test, container, false)
        if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(requireContext())){

            verifyAndProceedButton = rootView.findViewById(R.id.verify_proceed) as Button
            otpTimer = rootView.findViewById<View>(R.id.timer) as TextView
            reSendOtpButton = rootView.findViewById(R.id.resend_otp_button) as TextView
            enterOtpEditText = rootView.findViewById(R.id.enter_otp) as OtpEditText
            toastMessage = rootView.findViewById(R.id.toast_message_during_signup) as TextView
            backArrow = rootView.findViewById(R.id.back_arrow) as Button

            startTimer()
            reSendOtpButton.setOnClickListener { resendCode() }
            reSendOtpButton.setTextColor(resources.getColor(R.color.transparent_red))
            reSendOtpButton.isClickable = false
            verifyAndProceedButton.isClickable = false
            toastMessage.visibility = View.INVISIBLE


            backArrow.setOnClickListener(View.OnClickListener {
                val signUp1Fragment = SignUp1Fragment()
                fragmentManager!!.beginTransaction().replace(R.id.signup_fragment_container,signUp1Fragment).commit()
            })


            phone = arguments?.getString("phNo").toString()
            init(+91)


            verifyAndProceedButton.setOnClickListener(View.OnClickListener {
                SendOTP.getInstance().getTrigger().verify(enterOtpEditText.getText().toString())
            })


        }else{
//            Toast.makeText(requireContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show()
            toastMessage.visibility = View.VISIBLE
            toastMessage.text = "Check Your Internet Connection"
            toastMessage.setBackgroundResource(R.color.transparent_red)
        }
        return rootView
    }
// for storing into database
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        launch {
            context?.let {
                val mAuthToken = AuthToken("kkkkk")
                if (authToken == null){
                    AuthDatabase(it).getAuthDao().addToken(mAuthToken)
                    val notes = AuthDatabase(it).getAuthDao().getAuthToken()
                    Log.d("Ankan1",notes[0].id.toString() +"\n"+ notes[0].authToken.toString())
                }

//                val mUser = User(33,"taran","abc@xyx.com")
//                val user = AuthDatabase(it).getUser().addUser(mUser)
//
//                val user1 = AuthDatabase(it).getUser().readAllData()
//                Log.d("Ankan5",user1[0].id.toString() + user1[0].email)



            }
        }

    }











    override fun onSendOtpResponse(responseCode: SendOTPResponseCode, message: String) {
            activity!!.runOnUiThread {
                Log.e("VerificationActivity","onSendOtpResponse: " + responseCode.getCode() + "=======" + message)
                if (responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER || responseCode == SendOTPResponseCode.OTP_VERIFIED) {
//                    context?.let {
//                        AlertDialog.Builder(it)
//                            .setTitle("Success!!").setMessage("Verified Successfully !").show()
//                    }
//                    toastMessage.visibility = View.VISIBLE
                    toastMessage.text = "Verified Successfully !"
                    toastMessage.setBackgroundResource(R.color.transparent_blue)
                    timer.cancel();
                    timer.onFinish()

                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.43.217:3000")
//                        .baseUrl("http://sg-backend-dev.ap-south-1.elasticbeanstalk.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val signUpApi = retrofit.create(SignUpApi::class.java)
                    val sendNumber = SendNumber(phone.toString())
                    signUpApi.userSignup(sendNumber).enqueue(object :Callback<SignUpApiResponse>{
                        override fun onResponse(call: Call<SignUpApiResponse>, response: Response<SignUpApiResponse>) {
                            Log.d("Retrofit1", "OnResponse: ${response.body()?.message.toString()} \n"
                                        + "Auth Token: ${response.body()?.auth_token.toString()} \n"
                                        + "Response Code: ${response.code()}\n")


                            if (response.body()?.auth_token.toString() != null){
                                launch {
                                    context?.let {
                                        val mAuthToken = AuthToken(response.body()?.message.toString())
                                        if (authToken == null){
                                            AuthDatabase(it).getAuthDao().addToken(mAuthToken)
                                            val notes = AuthDatabase(it).getAuthDao().getAuthToken()
                                            Log.d("Ankan1",notes[notes.size-1].id.toString() +"\n"+ notes[notes.size-1].authToken.toString())
                                        }
                                    }
                                }
                            }








                        }
                        override fun onFailure(call: Call<SignUpApiResponse>, t: Throwable) {
                            Log.d("Retrofit1", "OnFailure")
                        }
                    })


                    val signUp3Fragment = SignUp3Fragment()
                    val args = Bundle()
                    args.putString("phNo", phone)
                    signUp3Fragment.arguments = args
                    fragmentManager!!.beginTransaction().replace(R.id.signup_fragment_container,signUp3Fragment).commit()

                } else if (responseCode == SendOTPResponseCode.READ_OTP_SUCCESS) {
                    //Auto read otp from sms successfully
                    // you can get otp form message filled
                    if (enterOtpEditText != null) {
                        enterOtpEditText.setText(message)
                        SendOTP.getInstance().getTrigger().verify(message)
                    }
                }else if (responseCode == SendOTPResponseCode.SMS_SUCCESSFUL_SEND_TO_NUMBER || responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_FAILED_SMS_SUCCESSFUL_SEND_TO_NUMBER) {
                    // Otp send to number successfully
//                    Toast.makeText(requireContext(), "OTP send successfully", Toast.LENGTH_SHORT).show()
//                    toastMessage.visibility = View.INVISIBLE

                    enterOtpEditText.addTextChangedListener(object : TextWatcher {
                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                            Log.d("VERIFY","CharSequence = $s Start = $start Before = $before Count = $count")
                            if (start == 5 && before == 0 && count == 1) {
                                verifyAndProceedButton.background =
                                    ContextCompat.getDrawable(requireContext(),R.drawable.button_shadow)
                                verifyAndProceedButton.isClickable = true
                            }
                        }
                        override fun beforeTextChanged(s: CharSequence,start: Int,count: Int,after: Int) {
                        }
                        override fun afterTextChanged(s: Editable) {
                        }
                    })
                    val c: Char = message.get(0)
                    if (c in '0'..'9'){
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.text = "OTP send successfully"
                        toastMessage.setBackgroundResource(R.color.transparent_blue)
                        enterOtpEditText.text?.clear()
                        verifyAndProceedButton.isClickable = false
                        verifyAndProceedButton.setBackgroundResource(R.color.transparent_red)
                    }
                    if (message == "otp_sent_successfully"){
                        toastMessage.visibility = View.VISIBLE
                        enterOtpEditText.text?.clear();
                        toastMessage.text = "OTP resend to your mobile number"
                        toastMessage.setBackgroundResource(R.color.transparent_blue)
                        verifyAndProceedButton.isClickable = false
                        verifyAndProceedButton.setBackgroundResource(R.color.transparent_red)
                    }
                }else {
                    if (message == "otp_not_verified"){
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.text = "Invalid or Incorrect OTP"
                        toastMessage.setBackgroundResource(R.color.transparent_red)
                    }else if (message == "no_request_found" || message == "Number is invalid type" || message == "Invalid_mobile"||message=="Please Enter valid mobile no"){
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.text = "Mobile no not found. Enter a valid no"
                        toastMessage.setBackgroundResource(R.color.transparent_red)
                        timer.cancel()
                        timer.onFinish()
                    }else if (message == "max_limit_reached_for_this_otp_verification"){
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.text = "Max limit reached for this otp verification"
                        toastMessage.setBackgroundResource(R.color.transparent_red)
                        timer.cancel()
                        timer.onFinish()
                    }else if (message == "OTP already verified" || message == "already_verified"){
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.text = "OTP already verified"
                        toastMessage.setBackgroundResource(R.color.transparent_red)
                        timer.cancel()
                        timer.onFinish()

                    }else if(message == "NO INTERNET CONNECTION"){
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.text = "NO INTERNET CONNECTION"
                        toastMessage.setBackgroundResource(R.color.transparent_red)
                        timer.cancel()
                        timer.onFinish()
                    }else {
                        Log.d("Ankan",message)
                    }

//                exception found
//                    Toast.makeText(requireContext(),"Error : " + responseCode.getCode(),Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun init(countryCode: Int) {
        if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(requireContext())) {

            Log.d("PHONE NO", phone.toString())
            Log.d("PHONE COUNTRY CODE", countryCode.toString())

            SendOTPConfigBuilder()
                .setCountryCode(countryCode)
                .setMobileNumber(phone)
                .setVerifyWithoutOtp(true) //direct verification while connect with mobile network
                .setAutoVerification(requireActivity()) //Auto read otp from Sms And Verify
                .setSenderId("ABCDEF")
                .setMessage("##OTP## is Your verification digits.")
                .setOtpLength(OTP_LENGTH)
                .setVerificationCallBack(this).build()
            SendOTP.getInstance().getTrigger().initiate()
        }else{
            toastMessage.visibility = View.VISIBLE
            toastMessage.text = "Check Your Internet Connection"
            toastMessage.setBackgroundResource(R.color.transparent_red)
            timer.cancel()
            timer.onFinish()
            verifyAndProceedButton.isClickable = false
            verifyAndProceedButton.setBackgroundResource(R.color.transparent_red)
        }
    }

    companion object {
        const val OTP_LENGTH = 6
    }

    private fun resendCode() {

        if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(requireContext())) {
            startTimer()
            SendOTP.getInstance().getTrigger().resend(RetryType.TEXT)
            toastMessage.visibility = View.INVISIBLE
        }else{
            toastMessage.visibility = View.VISIBLE
            toastMessage.text = "Check Your Internet Connection"
            toastMessage.setBackgroundResource(R.color.transparent_red)
            timer.cancel()
            timer.onFinish()
            verifyAndProceedButton.isClickable = false
            verifyAndProceedButton.setBackgroundResource(R.color.transparent_red)
        }
    }

    private fun startTimer() {
        if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(requireContext())) {
            reSendOtpButton.isClickable = false
            reSendOtpButton.setTextColor(resources.getColor(R.color.transparent_red))
            timer = object : CountDownTimer(30000, 1000) {
                var secondsLeft = 0
                override fun onTick(ms: Long) {
                    if ((ms.toFloat() / 1000.0f).roundToInt() != secondsLeft) {
                        secondsLeft = (ms.toFloat() / 1000.0f).roundToInt()
                        if(secondsLeft>9){
                            otpTimer.text = "00:$secondsLeft"
                        }else{
                            otpTimer.text = "00:0$secondsLeft"
                        }
                        Log.d("TIMER", secondsLeft.toString())
                    }
                }
                override fun onFinish() {
                    otpTimer.text = "00.00"
                    reSendOtpButton.isClickable = true
                    reSendOtpButton.setTextColor(resources.getColor(R.color.orangePrimary))
                    verifyAndProceedButton.isClickable = false
                    verifyAndProceedButton.setBackgroundResource(R.color.transparent_red)
                }
            }.start()
        }else{
            toastMessage.visibility = View.VISIBLE
            toastMessage.text = "Check Your Internet Connection"
            toastMessage.setBackgroundResource(R.color.transparent_red)
            timer.cancel()
            timer.onFinish()
            verifyAndProceedButton.isClickable = false
            verifyAndProceedButton.setBackgroundResource(R.color.transparent_red)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        SendOTP.getInstance().getTrigger().stop()
    }
}

