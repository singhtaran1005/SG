package com.studgenie.app.ui.onboarding.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.studgenie.app.R
import com.studgenie.app.util.InternetConnectivity
import com.studgenie.app.data.model.CountryItem
import com.studgenie.app.ui.onboarding.adapter.CountrySpinnerAdapter

class SignUp1Fragment : Fragment() {

    lateinit var mPhoneNumberEditText:EditText
    lateinit var mSmsButton:Button
    lateinit var mCountryIso:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_sign_up_1, container, false)

        mPhoneNumberEditText = rootView.findViewById(R.id.edit_text_phone)
        mSmsButton = rootView.findViewById(R.id.textView_continue)

        val spinner = rootView.findViewById<Spinner>(R.id.spinner_countries)
        val adapter = CountrySpinnerAdapter(requireContext(), createUserModelList())
        spinner.adapter = adapter

        Log.d("Connection","isConnected = ${InternetConnectivity.isConnected(requireContext())}  isConnectedFast = ${InternetConnectivity.isConnectedFast(requireContext())}")

        mCountryIso= spinner.selectedItem.toString()
            mSmsButton.setOnClickListener(View.OnClickListener {
                if (InternetConnectivity.isConnected(requireContext()) == true && InternetConnectivity.isConnectedFast(requireContext()) == true) {

                    var storePhoneNo = mPhoneNumberEditText.getText().toString()
                    if (storePhoneNo.matches("".toRegex())) {
                        Toast.makeText(requireContext(), "Enter your no first", Toast.LENGTH_SHORT).show()
                    } else {
                        val signUp2Fragment = SignUp2Fragment()
                        val args = Bundle()
                        Log.d("mCountryIso", mCountryIso)
                        args.putString("phNo", mPhoneNumberEditText.text.toString())
                        args.putString("isoCode", mCountryIso)
                        signUp2Fragment.setArguments(args)

                        getFragmentManager()!!.beginTransaction().addToBackStack(null).replace(
                            R.id.signup_fragment_container,
                            signUp2Fragment
                        ).commit()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Check Your Internet Connection",
                        Toast.LENGTH_LONG
                    ).show()
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