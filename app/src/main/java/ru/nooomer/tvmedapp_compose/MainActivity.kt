package ru.nooomer.tvmedapp_compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.nooomer.tvmedapp_compose.RetrifitService.SessionManager
import ru.nooomer.tvmedapp_compose.interfaces.*
import ru.nooomer.tvmedapp_compose.models.*
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme

private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
lateinit var ssm:SessionManager
private val scope = CoroutineScope(Dispatchers.Main + Job())
private var result: AuthModel? = null
private var result2: List<UserModel?>? = null
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
                    Greeting(mContext)
                }
            }
        }
    }
    fun login_click(login_text: String, password_text: String, context: Context){
        if(!ssm.validation() and (login_text!="")) {
            scope.launch {
                val def = scope.asyncIO { result = auth(login_text, password_text) }
                def.await()
                if ((result?.token == null) or (result == null)) {
                    val toast = Toast.makeText(
                        context,
                        "Сломано",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
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
fun Greeting(context: Context) {
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var button_enable = remember { mutableStateOf(false) }
    var phone_error = remember { mutableStateOf(false) }
    var label_color = remember { mutableStateOf(Color.Black) }
    var first_loading = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false )}
    val value_counter = remember { mutableStateOf(0 )}
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
                        button_enable.value = (password.value!="") and (phone.value!="")
                        value_counter.value = it.length
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
                            color = label_color.value
                        )
                    },
                    trailingIcon = {
                        Crossfade(targetState = if (phone_error.value) 1f else 0f, animationSpec = spring(
                            dampingRatio = 2f,
                            stiffness = Spring.StiffnessMedium
                        )) { phone_error ->
                            // note that it's required to use the value passed by Crossfade
                            // instead of your state value
                            if (phone_error == 1f) {
                                Icon(imageVector = Icons.Outlined.Close, contentDescription = null, tint = Color.Red)
                            } else {
                                Icon(imageVector = Icons.Outlined.Phone, contentDescription = null, tint = Color.Black)
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.small.copy(
                        bottomEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp)
                    ),
                    modifier = Modifier.height(70.dp)
                        .onFocusEvent {focusState ->
                            when{
                                (!focusState.isFocused and !first_loading.value) and (value_counter.value>0) ->{
                                    if(checkValidPhone(phone.value)) {
                                        phone_error.value = true
                                        label_color.value = Color.Red
                                    }
                                    else{
                                        phone_error.value = false
                                        label_color.value = Color.Black
                                    }
                                }
                                else ->{
                                    first_loading.value = false
                                    phone_error.value = false
                                    label_color.value = Color.Black
                                }
                            }
                                        },
                    isError = phone_error.value
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
                        button_enable.value = (password.value!="") and (phone.value!="")
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
                enabled = button_enable.value and !phone_error.value,
                modifier = Modifier.width(300.dp).padding(all = 10.dp)
                    .align(Alignment.CenterHorizontally),onClick = {
                    MainActivity().login_click(phone.value,password.value, context)
                    isLoading.value = true
                }){
                    Crossfade(targetState = if (isLoading.value) 1f else 0f, animationSpec = spring(
                        dampingRatio = 2f,
                        stiffness = Spring.StiffnessHigh
                    )) { isLoading ->
                        if (isLoading == 1f) {
                            CircularProgressIndicator(
                                Modifier
                                    .size(15.dp)
                                    .align(Alignment.CenterVertically),
                                strokeWidth = 1.dp
                            )
                        } else {
                            Text(stringResource(R.string.login_button_text))
                        }
                    }
            }
        }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TvMedApp_composeTheme {
        Greeting(LocalContext.current)
    }
}