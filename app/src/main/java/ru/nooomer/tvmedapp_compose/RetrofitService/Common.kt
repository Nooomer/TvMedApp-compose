package ru.nooomer.tvmedapp_compose.RetrofitService

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nooomer.tvmedapp_compose.BuildConfig
import ru.nooomer.tvmedapp_compose.api.interfaces.APIAll

class Common(context: Context) {
    val BASE_URL = BuildConfig.API_FULL_URL
    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AddCookiesInterceptor(context))
        .addInterceptor(ReceivedCookiesInterceptor(context))
        .addInterceptor(AddHeaderInterceptor())
        .build()

    inline fun <reified T> initRetrofit(): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
    }

    val retrofitService2: APIAll
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIAll::class.java)
}