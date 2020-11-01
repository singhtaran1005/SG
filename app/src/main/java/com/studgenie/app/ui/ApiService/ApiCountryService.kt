package com.studgenie.app.ui.ApiService

import com.studgenie.app.data.model.CountryItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiCountryService {
    @GET("country_code")
    fun fetchAllCountries(): Call<List<CountryItem>>
}