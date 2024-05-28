package ru.nooomer.tvmedapp_compose.RetrofitService

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nooomer.tvmedapp_compose.api.interfaces.APIAll

class Common(context: Context) {
	var client: OkHttpClient = OkHttpClient.Builder()
		.addInterceptor(AddCookiesInterceptor(context))
		.addInterceptor(ReceivedCookiesInterceptor(context))
		.addInterceptor(AddHeaderInterceptor())
		.build()

	inline fun <reified T> initRetrofit(baseUrl: String): T {
		return Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(client)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(T::class.java)
	}

	val retrofitService2: APIAll
		get() = Retrofit.Builder()
			.baseUrl("123")
			.client(client)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(APIAll::class.java)
}