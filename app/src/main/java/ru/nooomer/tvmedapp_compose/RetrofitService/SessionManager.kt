package ru.nooomer.tvmedapp_compose.RetrofitService

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import ru.nooomer.tvmedapp_compose.R
import ru.nooomer.tvmedapp_compose.interfaces.TSessionManager

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager(context: Context) : TSessionManager {
	private var prefs: SharedPreferences =
		context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
	val cookiesEditor = PreferenceManager.getDefaultSharedPreferences(
		context
	).edit()
	private val editor: SharedPreferences.Editor = prefs.edit()


	private var userType: String?
		get() = prefs.getString(USER_TYPE, null)
		set(value) {
			editor.putString(USER_TYPE, value)
			editor.apply()
		}
	private var userId: String?
		get() = prefs.getString(USER_ID, null)
		set(value) {
			editor.putString(USER_ID, value)
			editor.apply()
		}
	private var loginState: Boolean
		get() = prefs.getBoolean(LOGIN_STATE, false)
		set(value) {
			editor.putBoolean(LOGIN_STATE, value)
			editor.apply()
		}
	private var sessionTimeOut: String?
		get() = prefs.getString(SESSION_TIMEOUT, null)
		set(value) {
			editor.putString(SESSION_TIMEOUT, value)
			editor.apply()
		}

	private var clickedTreatmentId: String?
		get() = prefs.getString(CLICKED_TREATMENT_ID, null)
		set(value) {
			editor.putString(CLICKED_TREATMENT_ID, value)
			editor.apply()
		}


	init {
		if (fetch(LOGIN_STATE) == null) {
			loginState = false
			userId = ""
			userType = ""
			sessionTimeOut = ""
			clickedTreatmentId = ""
		}
	}

	override fun validation(): Boolean {
		return (fetch(USER_ID) != "" && fetch(USER_ID) != null)
	}

	override fun fetch(param: String): Any? {
		when (param) {
			LOGIN_STATE -> return loginState
			USER_TYPE -> return userType
			USER_ID -> return userId
			SESSION_TIMEOUT -> return sessionTimeOut
			CLICKED_TREATMENT_ID -> return clickedTreatmentId
		}
		return "Not Found params"
	}

	override fun save(param: String, data: Any?) {
		val editor = prefs.edit()
		when (param) {
			LOGIN_STATE -> loginState = data as Boolean
			USER_TYPE -> userType = data.toString()
			USER_ID -> userId = data.toString()
			SESSION_TIMEOUT -> sessionTimeOut = data.toString()
			CLICKED_TREATMENT_ID -> clickedTreatmentId = data.toString()
		}
		editor.apply()
	}

	override fun clearSession() {
		editor.clear()
		cookiesEditor.clear()
		cookiesEditor.apply()
		editor.apply()
	}
}