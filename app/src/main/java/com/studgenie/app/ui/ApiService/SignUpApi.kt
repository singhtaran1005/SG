package com.studgenie.app.ui.ApiService

import com.studgenie.app.data.model.SendNumber
import com.studgenie.app.data.model.SendUserDetails
import com.studgenie.app.data.model.UserDetailsApiResponse
import com.studgenie.app.data.model.SignUpApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("/students/signup")
    fun userSignup( @Body number:SendNumber): Call<SignUpApiResponse>

    @POST("/students/createdetails")
    fun userDetails( @Body details: SendUserDetails): Call<UserDetailsApiResponse>
}