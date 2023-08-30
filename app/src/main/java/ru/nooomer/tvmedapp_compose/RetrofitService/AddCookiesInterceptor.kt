package ru.nooomer.tvmedapp_compose.RetrofitService

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AddCookiesInterceptor(
    private val context: Context
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences = PreferenceManager.getDefaultSharedPreferences(
            context
        ).getStringSet(PREF_COOKIES, HashSet()) as HashSet<String>?
        for (cookie in preferences!!) {
            builder.addHeader("Cookie", cookie)
        }
        Log.println(Log.WARN, "koin", context.toString())
        return chain.proceed(builder.build())
    }

    companion object {
        const val PREF_COOKIES = "PREF_COOKIES"
    }
}