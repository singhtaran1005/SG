package com.studgenie.app.ui.onboarding.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.studgenie.app.R
import com.studgenie.app.util.InternetConnectivity
import com.studgenie.app.data.model.CountryItem
import com.studgenie.app.ui.onboarding.adapter.CountrySpinnerAdapter

class SignUp1Fragment : Fragment(){
    lateinit var phoneNumberEditText:EditText
    lateinit var continueButton:Button
    lateinit var countryCode:String
    lateinit var toastMessage:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_sign_up_1, container, false)

        phoneNumberEditText = rootView.findViewById(R.id.edit_text_phone)
        continueButton = rootView.findViewById(R.id.textView_continue)
        toastMessage = rootView.findViewById(R.id.toast_message_1st_signup_fragment)

        val spinner = rootView.findViewById<Spinner>(R.id.spinner_countries)
        val adapter = CountrySpinnerAdapter(requireContext(), createUserModelList())
        spinner.adapter = adapter

        Log.d("Connection","isConnected = ${InternetConnectivity.isConnected(requireContext())}  isConnectedFast = ${InternetConnectivity.isConnectedFast(requireContext())}")

        countryCode= spinner.selectedItem.toString()
            continueButton.setOnClickListener(View.OnClickListener {
                if (InternetConnectivity.isConnected(requireContext()) == true && InternetConnectivity.isConnectedFast(requireContext()) == true) {

                    var storePhoneNo = phoneNumberEditText.getText().toString()
                    if (storePhoneNo.matches("".toRegex())) {
//                        Toast.makeText(requireContext(), "Enter your no first", Toast.LENGTH_SHORT).show()
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.setText("Enter your no first")
                        toastMessage.setBackgroundResource(R.color.transparent_red)
                    } else {

//                        init(91)
                        val signUp2Fragment = SignUp2Fragment()
                        val args = Bundle()
                        Log.d("mCountryIso", countryCode)
                        args.putString("phNo", phoneNumberEditText.text.toString())
                        args.putString("isoCode", countryCode)
                        signUp2Fragment.setArguments(args)

                        getFragmentManager()!!.beginTransaction().replace(
                            R.id.signup_fragment_container,
                            signUp2Fragment
                        ).commit()
                    }

                } else {
//                    Toast.makeText(requireContext(),"Check Your Internet Connection",Toast.LENGTH_LONG).show()
                    toastMessage.visibility = View.VISIBLE
                    toastMessage.setText("Check Your Internet Connection")
                    toastMessage.setBackgroundResource(R.color.transparent_red)
                }
            })

        return rootView
    }
    private fun createUserModelList(): ArrayList<CountryItem> {
        val list = ArrayList<CountryItem>()

        list.add(CountryItem("+91", R.drawable.flag_india))
        list.add(CountryItem("+1", R.drawable.flag_usa))
        list.add(CountryItem("+55", R.drawable.flag_brazil))
        list.add(CountryItem("+56", R.drawable.flag_chile))

        return list
    }

}