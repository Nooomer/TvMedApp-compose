package com.example.tvmedapp_compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tvmedapp_compose.RetrifitService.SessionManager
import com.example.tvmedapp_compose.interfaces.*
import com.example.tvmedapp_compose.models.*
import com.example.tvmedapp_compose.ui.theme.TvMedApp_composeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
                mContext = LocalContext.current
                // A surface container using the 'background' color from the theme
                ssm = SessionManager(this)
                if(ssm.validation()) {
                    if ((ssm.fetch(TOKEN_LIFETIME)?.toLong()!! < (ssm.fetch(TOKEN_LIFETIME)?.toLong()
                            ?.plus(60000000000)!!))
                    ) {
                        mContext.startActivity(Intent(mContext, TreatmentActivity::class.java))
                        finish()
                    }
                }
                    Greeting(mContext)
            }
        }
    }
    fun login_click(login_text: String, password_text: String){
        if(!ssm.validation() and (login_text!="")) {
            scope.launch {
                val def = scope.asyncIO { result = auth(login_text, password_text) }
                def.await()
                if ((result?.token == null) or (result == null)) {
                    val toast = Toast.makeText(
                        mContext,
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
                        mContext,
                        "Токен сохранен",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    mContext.startActivity(Intent(mContext, TreatmentActivity::class.java))
                    finish()
                    println(result?.token)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(context: Context) {
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        )
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            )
            {
                TextField(
                    phone.value,
                    { phone.value = it },
                    textStyle = TextStyle(fontSize = 28.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    label = {
                        Text(
                            stringResource(R.string.phone_number_text),
                            color = Color.Black
                        )
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.small.copy(
                        bottomEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp)
                    ),
                    modifier = Modifier.height(70.dp)
                )

            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().padding(all = 10.dp)
            )
            {
                TextField(
                    password.value,
                    { password.value = it },
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
                    modifier = Modifier.height(70.dp)
                )

            }
            Button(
                modifier = Modifier.width(300.dp).padding(all = 10.dp)
                    .align(Alignment.CenterHorizontally),onClick = {
                        MainActivity().login_click(phone.value,password.value)
                }){
                Text(stringResource(R.string.login_button_text))
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