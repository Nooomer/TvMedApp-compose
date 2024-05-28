package ru.nooomer.tvmedapp_compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.nooomer.tvmedapp_compose.RetrofitService.SessionManager
import ru.nooomer.tvmedapp_compose.api.API
import ru.nooomer.tvmedapp_compose.api.models.AuthDto
import ru.nooomer.tvmedapp_compose.di.appModule
import ru.nooomer.tvmedapp_compose.helpers.PasswordVisualTransformation
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme
import ru.nooomer.tvmedapp_compose.ui.theme.textFieldColor
import java.util.Date

private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
private val scope = CoroutineScope(Dispatchers.Main + Job())
lateinit var ssm: SessionManager
private var result: AuthDto? = null
private var isFailed: Boolean = false
val phoneErrorSemanticsKey = SemanticsPropertyKey<Boolean>("PhoneErrorSemantics")
var SemanticsPropertyReceiver.phoneErrorSemantics by phoneErrorSemanticsKey

class MainActivity : ComponentActivity(), PreferenceDataType {
	private lateinit var mContext: Context
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		startKoin {
			androidContext(this@MainActivity)
			modules(appModule)
			androidLogger()
		}
		setContent {
			TvMedApp_composeTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
				) {
					mContext = LocalContext.current
					ssm = SessionManager(this)
					with(ssm) {
						if (validation()) {
							if (checkTimeOut()) {
								with(mContext) {
									startActivity(
										Intent(
											this, TreatmentActivity::class.java
										)
									)
									(this as Activity).finish()
								}
							} else {
								clearSession()
								LoginWindow(mContext)
							}
						}
					}
					LoginWindow(mContext)
				}
			}
		}
	}

	private fun checkTimeOut() =
		(Date().time - (ssm.fetch(SESSION_TIMEOUT) as String).toLong()) < 604800
}

fun loginClick(loginText: String, passwordText: String, context: Context) {
	if (!ssm.validation() and (loginText != "")) {
		scope.launch {
			scope.asyncIO {
				result = API.login(loginText, passwordText) as AuthDto
				Log.w("[REQUEST]", "Request data:${result}")
			}.join()
			when (result) {
				null -> {
					val toast = Toast.makeText(
						context,
						R.string.an_error_occurred_try_again_later,
						Toast.LENGTH_SHORT
					)
					toast.show()
					isFailed = true
				}

				else -> {
					with(ssm) {
						with(result) {
							save(LOGIN_STATE, true)
							save(USER_TYPE, this?.userType)
							save(USER_ID, this?.id)
							save(SESSION_TIMEOUT, Date().time)
						}
					}
					val toast = Toast.makeText(
						context,
						context.getString(R.string.login_ok),
						Toast.LENGTH_SHORT
					)
					toast.show()
					with(context) {
						startActivity(
							Intent(
								this,
								TreatmentActivity::class.java
							)
						)
						(this as Activity).finish()
					}
					isFailed = false
				}
			}
		}
	}
}

fun checkValidPhone(s: String) = !Regex(pattern = "89\\d{9}\$").containsMatchIn(input = s)

@Composable
fun LoginWindow(context: Context) {
	val phone = remember { mutableStateOf("") }
	val password = remember { mutableStateOf("") }
	val buttonEnable = remember { mutableStateOf(false) }
	val phoneError = remember { mutableStateOf(false) }
	val labelColor = remember { mutableStateOf(Color.Black) }
	val firstLoading = remember { mutableStateOf(true) }
	val isLoading = remember { mutableStateOf(false) }
	val valueCounter = remember { mutableStateOf(0) }
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center
	)
	{
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.fillMaxWidth().padding(5.dp)
		)
		{
			TextField(
				phone.value,
				{
					phone.value = it
					buttonEnable.value = (password.value != "") and (phone.value != "")
					valueCounter.value = it.length
				},
				textStyle = MaterialTheme.typography.titleLarge,
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
				colors = textFieldColor!!,
				label = {
					Text(
						stringResource(R.string.phone_number_text),
						color = labelColor.value
					)
				},
				trailingIcon = {
					Crossfade(
						targetState = if (phoneError.value) 1f else 0f, animationSpec = spring(
							dampingRatio = 2f,
							stiffness = Spring.StiffnessMedium
						), label = ""
					) { phoneError ->
						if (phoneError == 1f) {
							Icon(
								imageVector = Icons.Outlined.Close,
								contentDescription = null,
								tint = Color.Red
							)
						} else {
							Icon(
								imageVector = Icons.Outlined.Phone,
								contentDescription = null,
								tint = Color.Black
							)
						}
					}
				},
				singleLine = true,
				enabled = !isLoading.value,
				shape = MaterialTheme.shapes.small.copy(
					bottomEnd = CornerSize(10.dp),
					bottomStart = CornerSize(10.dp)
				),
				modifier = Modifier
					.semantics {
						phoneErrorSemantics = phoneError.value
					}
					.height(70.dp)
					.onFocusEvent { focusState ->
						when {
							(!focusState.isFocused and !firstLoading.value) and (valueCounter.value < 11) -> {
								if (checkValidPhone(phone.value)) {
									phoneError.value = true
									buttonEnable.value = false
									labelColor.value = Color.Red
								} else {
									if ((password.value == "") or (phone.value.length != 11)) {
										phoneError.value = false
										buttonEnable.value = false
										labelColor.value = Color.Black
									} else {
										phoneError.value = false
										buttonEnable.value = true
										labelColor.value = Color.Black
									}
								}
							}

							else -> {
								firstLoading.value = false
								phoneError.value = false
								labelColor.value = Color.Black
							}
						}
					},
				isError = phoneError.value
			)

		}
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.fillMaxWidth().padding(5.dp)
		)
		{
			TextField(
				password.value,
				{
					password.value = it
					buttonEnable.value =
						(password.value != "") and ((phone.value != "") or (!phoneError.value) or (phone.value.length == 11))
				},
				visualTransformation = {
					PasswordVisualTransformation().filter(it)
				},
				textStyle = MaterialTheme.typography.titleLarge,
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
				colors = textFieldColor!!,
				singleLine = true,
				enabled = !isLoading.value,
				label = {
					Text(
						stringResource(R.string.password_text),
						color = Color.Black
					)
				},
				shape = MaterialTheme.shapes.small.copy(
					bottomEnd = CornerSize(10.dp),
					bottomStart = CornerSize(10.dp)
				),
				modifier = Modifier.height(70.dp),
				trailingIcon = {
					Icon(
						imageVector = Icons.Outlined.Lock,
						contentDescription = null,
						tint = Color.Black
					)
				},
			)

		}
		Button(
			enabled = buttonEnable.value and !phoneError.value and ((phone.value != "") or (!phoneError.value) or (phone.value.length == 11)),
			modifier = Modifier
				.width(300.dp)
				.padding(all = 10.dp)
				.align(Alignment.CenterHorizontally),
			onClick = {
				loginClick(phone.value, password.value, context)
				isLoading.value = true
			}) {
			if (isLoading.value) {
				CircularProgressIndicator(
					Modifier
						.size(15.dp)
						.align(Alignment.CenterVertically),
					strokeWidth = 1.dp
				)
				Text(stringResource(R.string.login_button_text))
				isLoading.value = isFailed
			} else {
				isLoading.value = false
				Text(stringResource(R.string.login_button_text))
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun LoginWindowPreview() {
	TvMedApp_composeTheme {
		LoginWindow(LocalContext.current)
	}
}