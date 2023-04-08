package ru.nooomer.tvmedapp_compose.RetrofitService

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType


/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor, PreferenceDataType {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetch(USER_TOKEN)?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }

}