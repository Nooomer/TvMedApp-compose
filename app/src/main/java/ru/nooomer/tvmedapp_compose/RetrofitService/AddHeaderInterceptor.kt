package ru.nooomer.tvmedapp_compose.RetrofitService

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AddHeaderInterceptor : Interceptor {
	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val builder = chain.request().newBuilder()
		builder.addHeader("apptype", APP_TYPE)
		return chain.proceed(builder.build())
	}

	companion object {
		const val APP_TYPE = "Mobile"
	}
}