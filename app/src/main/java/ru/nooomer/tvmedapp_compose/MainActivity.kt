package ru.nooomer.tvmedapp_compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import ru.nooomer.tvmedapp_compose.RetrofitService.*
import ru.nooomer.tvmedapp_compose.interfaces.*
import ru.nooomer.tvmedapp_compose.models.*
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme

private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
lateinit var ssm:SessionManager
private val scope = CoroutineScope(Dispatchers.Main + Job())
private var result: AuthModel? = null
private var result2: List<UserModel?>? = null
private var isFailed: Boolean = false
val phoneErrorSemanticsKey = SemanticsPropertyKey<Boolean>("PhoneErrorSemantics")
var SemanticsPropertyReceiver.phoneErrorSemantics by phoneErrorSemanticsKey
class MainActivity : ComponentActivity(), PreferenceDataType, RetrorfitFun {
    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            TvMedApp_composeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    mContext = LocalContext.current
                    // A surface container using the 'background' color from the theme
                    ssm = SessionManager(this)
                    if (ssm.validation()) {
                        if ((ssm.fetch(TOKEN_LIFETIME)?.toLong()!! < (ssm.fetch(TOKEN_LIFETIME)
                                ?.toLong()
                                ?.plus(60000000000)!!))
                        ) {
                            mContext.startActivity(Intent(mContext, TreatmentActivity::class.java))
                            (mContext as Activity).finish()
                        }
                    }
                    LoginWindow(mContext)
                }
            }
        }
    }
    fun loginClick(loginText: String, passwordText: String, context: Context){
        if(!ssm.validation() and (loginText!="")) {
            scope.launch {
                val def = scope.asyncIO { result = auth(loginText, passwordText) }
                def.await()
                if ((result?.token == null) or (result == null)) {
                    val toast = Toast.makeText(
                        context,
                        "Сломано",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                  isFailed = true
                } else {
                    ssm.save(USER_TOKEN, result?.token)
                    ssm.save(TOKEN_LIFETIME, System.currentTimeMillis().toString())
                    ssm.save(USER_TYPE, result?.user_type)
                    ssm.save(USER_ID, result?.user_id)
                    val toast = Toast.makeText(
                        context,
                        "Токен сохранен",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    val activity = (context as? Activity)
                    context.startActivity(Intent(context, TreatmentActivity::class.java))
                    activity?.finish()
                    println(result?.token)
                    isFailed = false
                }
            }
        }
    }
}
fun checkValidPhone(s: String): Boolean{
    return !Regex(pattern = "89\\d{9}\$").containsMatchIn(input = s)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginWindow(context: Context) {
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val buttonEnable = remember { mutableStateOf(false) }
    val phoneError = remember { mutableStateOf(false) }
    val labelColor = remember { mutableStateOf(Color.Black) }
    val firstLoading = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false )}
    val valueCounter = remember { mutableStateOf(0 )}
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
                    { phone.value = it
                        buttonEnable.value = (password.value!="") and (phone.value!="")
                        valueCounter.value = it.length
                        },
                    textStyle = TextStyle(fontSize = 28.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        errorLabelColor = Color.Red,
                        errorCursorColor = Color.Red,
                    ),
                    label = {
                        Text(
                            stringResource(R.string.phone_number_text),
                            color = labelColor.value
                        )
                    },
                    trailingIcon = {
                        Crossfade(targetState = if (phoneError.value) 1f else 0f, animationSpec = spring(
                            dampingRatio = 2f,
                            stiffness = Spring.StiffnessMedium
                        )) { phoneError ->
                            // note that it's required to use the value passed by Crossfade
                            // instead of your state value
                            if (phoneError == 1f) {
                                Icon(imageVector = Icons.Outlined.Close, contentDescription = null, tint = Color.Red)
                            } else {
                                Icon(imageVector = Icons.Outlined.Phone, contentDescription = null, tint = Color.Black)
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
                        .onFocusEvent {focusState ->
                            when{
                                (!focusState.isFocused and !firstLoading.value) and (valueCounter.value>0) ->{
                                    if(checkValidPhone(phone.value)) {
                                        phoneError.value = true
                                        buttonEnable.value = false
                                        labelColor.value = Color.Red
                                    }
                                    else{
                                        phoneError.value = false
                                        buttonEnable.value = true
                                        labelColor.value = Color.Black
                                    }
                                }
                                else ->{
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
                        buttonEnable.value = (password.value!="") and (phone.value!="")
                    },
                    visualTransformation = {
                        PasswordVisualTransformation().filter(it)
                    },
                    textStyle = TextStyle(fontSize = 28.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    singleLine = true,
                    enabled = !isLoading.value,
                    label = {
                        Text(
                            stringResource(R.string.password_text),
                        color = Color.Black)
                    },
                    shape = MaterialTheme.shapes.small.copy(
                        bottomEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp)
                    ),
                    modifier = Modifier.height(70.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = Color.Black)
                    },
                )

            }
            Button(
                enabled = buttonEnable.value and !phoneError.value,
                modifier = Modifier.width(300.dp).padding(all = 10.dp)
                    .align(Alignment.CenterHorizontally),onClick = {
                    MainActivity().loginClick(phone.value,password.value, context)
                    isLoading.value = true
                }){ if (isLoading.value) {
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

//                    Crossfade(targetState = if (isLoading.value) 1f else 0f, animationSpec = spring(
//                        dampingRatio = 2f,
//                        stiffness = Spring.StiffnessHigh
//                    )) { isLoading ->
//                        if (isLoading == 1f) {
//                            CircularProgressIndicator(
//                                Modifier
//                                    .size(15.dp)
//                                    .align(Alignment.CenterVertically),
//                                strokeWidth = 1.dp
//                            )
//                        } else {
//                            Text(stringResource(R.string.login_button_text))
//                        }


                   // }
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