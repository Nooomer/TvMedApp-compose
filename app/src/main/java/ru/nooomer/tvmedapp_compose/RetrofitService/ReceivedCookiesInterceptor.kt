package ru.nooomer.tvmedapp_compose.RetrofitService

import android.content.Context
import androidx.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ReceivedCookiesInterceptor
	(private val context: Context) : Interceptor {
	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalResponse = chain.proceed(chain.request())
		if (!originalResponse.headers("Set-Cookie").isEmpty()) {
			val cookies = PreferenceManager.getDefaultSharedPreferences(
				context
			).getStringSet("PREF_COOKIES", HashSet()) as HashSet<String>?
			for (header in originalResponse.headers("Set-Cookie")) {
				cookies!!.add(header)
			}
			val memes = PreferenceManager.getDefaultSharedPreferences(
				context
			).edit()
			memes.putStringSet("PREF_COOKIES", cookies).apply()
			memes.commit()
		}
		return originalResponse
	}
}