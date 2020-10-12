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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.msg91.sendotpandroid.library.internal.SendOTP
import com.msg91.sendotpandroid.library.listners.VerificationListener
import com.msg91.sendotpandroid.library.roots.RetryType
import com.msg91.sendotpandroid.library.roots.SendOTPConfigBuilder
import com.msg91.sendotpandroid.library.roots.SendOTPResponseCode
import com.studgenie.app.R
import com.studgenie.app.util.InternetConnectivity
import com.studgenie.app.ui.common.OtpEditText


@Suppress("DEPRECATION")
class SignUp2Fragment : Fragment(), VerificationListener {

    lateinit var enterOtpEditText: OtpEditText
    lateinit var verifyAndProceedButton:Button
    lateinit var reSendOtpButton:TextView
    lateinit var otpTimer:TextView
    lateinit var toastMessage:TextView
    lateinit var timer: CountDownTimer

    var phone: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_sign_up_2_test, container, false)
        if (InternetConnectivity.isConnected(requireContext()) == true && InternetConnectivity.isConnectedFast(requireContext()) == true){
            verifyAndProceedButton = rootView.findViewById(R.id.verify_proceed) as Button
            otpTimer = rootView.findViewById<View>(R.id.timer) as TextView
            reSendOtpButton = rootView.findViewById(R.id.resend_otp_button) as TextView
            enterOtpEditText = rootView.findViewById(R.id.enter_otp) as OtpEditText
            toastMessage = rootView.findViewById(R.id.toast_message_during_signup) as TextView

            startTimer()
            reSendOtpButton.setOnClickListener { ResendCode() }
            reSendOtpButton.setTextColor(getResources().getColor(R.color.toast_message))
            reSendOtpButton.isClickable = false
            verifyAndProceedButton.isClickable = false


            phone = arguments?.getString("phNo").toString()
            init(91)


            verifyAndProceedButton.setOnClickListener(View.OnClickListener {
                SendOTP.getInstance().getTrigger().verify(enterOtpEditText.getText().toString())
            })

            enterOtpEditText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    Log.d(
                        "VERIFY",
                        "CharSequence = $s Start = $start Before = $before Count = $count"
                    )
                    if (start == 5 && before == 0 && count == 1) {

                        verifyAndProceedButton.setBackground(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.button_shadow
                            )
                        )
                        verifyAndProceedButton.isClickable = true
                    }
                    // TODO Auto-generated method stub
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // TODO Auto-generated method stub
                }

                override fun afterTextChanged(s: Editable) {
                    // TODO Auto-generated method stub
                }
            })
        }else{
            Toast.makeText(requireContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show()
        }


        return rootView
    }

    override fun onSendOtpResponse(responseCode: SendOTPResponseCode, message: String) {

        if (InternetConnectivity.isConnected(requireContext()) == true && InternetConnectivity.isConnectedFast(requireContext()) == true){
            activity!!.runOnUiThread {
                Log.e(
                    "VerificationActivity",
                    "onSendOtpResponse: " + responseCode.getCode() + "=======" + message
                )
                toast(message)

                if (responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER || responseCode == SendOTPResponseCode.OTP_VERIFIED) {
//                    context?.let {
//                        AlertDialog.Builder(it)
//                            .setTitle("Success!!").setMessage("Verified Successfully !").show()
//                    }

                    toastMessage.visibility = View.GONE
//                    if(timer != null) {
                        timer.cancel();
                        timer.onFinish()
//                    }
//                Toast.makeText(requireContext(), "Verified successfully", Toast.LENGTH_SHORT)
//                    .show()
                    val signUp3Fragment = SignUp3Fragment()
                    val args = Bundle()
                    args.putString("phNo", phone)
                    signUp3Fragment.setArguments(args)
                    getFragmentManager()!!.beginTransaction().replace(
                        R.id.signup_fragment_container,
                        signUp3Fragment
                    ).commit()

                } else if (responseCode == SendOTPResponseCode.READ_OTP_SUCCESS) {
                    //Auto read otp from sms successfully
                    // you can get otp form message filled
//                    Toast.makeText(requireContext(), "message = $message", Toast.LENGTH_SHORT).show()
//                    if (enterOtpEditText != null) {
                        enterOtpEditText.setText(message)
                        SendOTP.getInstance().getTrigger().verify(message)
//                    }
                }else if (responseCode == SendOTPResponseCode.SMS_SUCCESSFUL_SEND_TO_NUMBER || responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_FAILED_SMS_SUCCESSFUL_SEND_TO_NUMBER) {
                    // Otp send to number successfully
//                    Toast.makeText(requireContext(), "OTP send successfully", Toast.LENGTH_SHORT).show()
                    toastMessage.visibility = View.INVISIBLE

                }else if (responseCode != SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER && responseCode != SendOTPResponseCode.OTP_VERIFIED){
                    if (message == "otp_not_verified"){
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.setText("Invalid or Incorrect OTP")
                        toastMessage.setBackgroundResource(R.color.toast_message)
                    }else if (message == "no_request_found"){
                        toastMessage.visibility = View.INVISIBLE
//                        if(timer != null) {
                            timer.cancel();
                            timer.onFinish()
//                        }

                    }else{
                        toastMessage.visibility = View.INVISIBLE
//                        if(timer != null) {
                            timer.cancel();
                            timer.onFinish()
//                        }
                    }
                } else {

//                exception found
//                    Toast.makeText(requireContext(),"Error : " + responseCode.getCode(),Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(requireContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show()
        }

    }

    private fun toast(message: String) {
        val c: Char = message.get(0)
        if (c >= '0' && c <= '9'){
            Toast.makeText(requireContext(),"OTP send successfully",Toast.LENGTH_LONG).show()
        }else if (message == "no_request_found" || message == "Number is invalid type" || message == "Invalid_mobile"){
            Toast.makeText(requireContext(),"Mobile no not found. Enter a valid no",Toast.LENGTH_LONG).show()
        }else if (message == "otp_sent_successfully"){
            Toast.makeText(requireContext(),"Otp resent successfully",Toast.LENGTH_LONG).show()
        }else if (message == "otp_not_verified"){
            Toast.makeText(requireContext(),"OTP not verified",Toast.LENGTH_LONG).show()
        }else if (message == "max_limit_reached_for_this_otp_verification"){
            Toast.makeText(requireContext(),"Max limit reached for this otp verification",Toast.LENGTH_LONG).show()
        }else if (message == "otp_verified"){
            Toast.makeText(requireContext(),"Otp verified Successfully",Toast.LENGTH_LONG).show()
        }else if (message == "READ_OTP_TIMEOUT"){
            Toast.makeText(requireContext(),"READ OTP TIMEOUT",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(requireContext(), "$message", Toast.LENGTH_LONG).show()
        }
    }



























    private fun init(countryCode: Int) {
        if (InternetConnectivity.isConnected(requireContext()) == true && InternetConnectivity.isConnectedFast(requireContext()) == true) {

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
            Toast.makeText(requireContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val OTP_LENGTH = 6
    }

    fun ResendCode() {

        if (InternetConnectivity.isConnected(requireContext()) == true && InternetConnectivity.isConnectedFast(requireContext()) == true ) {
            startTimer()
            SendOTP.getInstance().getTrigger().resend(RetryType.TEXT)
            toastMessage.visibility = View.VISIBLE
            enterOtpEditText.getText()?.clear();
            toastMessage.setText("OTP resend to your mobile number")
            toastMessage.setBackgroundResource(R.color.resend_message)
            verifyAndProceedButton.isClickable = false
            verifyAndProceedButton.setBackgroundResource(R.color.toast_message)
        }else{
            Toast.makeText(requireContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun startTimer() {

        if (InternetConnectivity.isConnected(requireContext()) == true && InternetConnectivity.isConnectedFast(requireContext()) == true) {
            reSendOtpButton.isClickable = false
            reSendOtpButton.setTextColor(getResources().getColor(R.color.toast_message))
            timer = object : CountDownTimer(30000, 1000) {
                var secondsLeft = 0
                override fun onTick(ms: Long) {
                    if (Math.round(ms.toFloat() / 1000.0f) != secondsLeft) {
                        secondsLeft = Math.round(ms.toFloat() / 1000.0f)
                        otpTimer.setText("00:$secondsLeft")
                        Log.d("TIMER", secondsLeft.toString())
                    }
                }

                override fun onFinish() {
                    otpTimer.setText("00.0")
                    reSendOtpButton.isClickable = true
                    reSendOtpButton.setTextColor(getResources().getColor(R.color.orangePrimary))
                    verifyAndProceedButton.isClickable = false
//                verifyAndProceedButton.setBackgroundResource(R.color.toast_message)

                }

            }.start()
        }else{
            Toast.makeText(requireContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        SendOTP.getInstance().getTrigger().stop()
    }


}

