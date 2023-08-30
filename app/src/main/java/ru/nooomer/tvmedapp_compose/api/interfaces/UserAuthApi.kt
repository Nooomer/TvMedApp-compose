package ru.nooomer.tvmedapp_compose.api.interfaces

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.nooomer.tvmedapp_compose.api.models.AuthDto
import ru.nooomer.tvmedapp_compose.api.models.LoginData

interface UserAuthApi {
    @POST("user/login")
    fun login(
        @Body body: LoginData?
    ): Call<AuthDto>

    @GET("user/login")
    fun checkLogin(): Call<Boolean>

    @GET("user/logout")
    fun logout():Call<Void>
}