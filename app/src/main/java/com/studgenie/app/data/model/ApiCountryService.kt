package com.studgenie.app.data.model

import retrofit2.Call
import retrofit2.http.GET

interface ApiCountryService {
    @GET("country_code")
    fun fetchAllCountries(): Call<List<CountryItem>>
}