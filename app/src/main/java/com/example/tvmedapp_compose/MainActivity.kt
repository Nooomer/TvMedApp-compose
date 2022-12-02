package com.example.tvmedapp_compose

import android.os.Bundle
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
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tvmedapp_compose.ui.theme.TvMedApp_composeTheme
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvMedApp_composeTheme {
                // A surface container using the 'background' color from the theme
                    Greeting()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {
    val phone = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    )
    {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(all = 10.dp)
        )
        {
            TextField(
                phone.value,
                { phone.value = it },
                textStyle = TextStyle(fontSize = 28.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.LightGray
                ),
                singleLine = true,
                shape = MaterialTheme.shapes.small.copy(
                    bottomEnd = CornerSize(10.dp),
                    bottomStart = CornerSize(10.dp)
                ),
                modifier = Modifier.height(40.dp)
            )

        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(all = 10.dp)
        )
        {
            TextField(
                phone.value,
                { phone.value = it },
                textStyle = TextStyle(fontSize = 28.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.LightGray
                ),
                singleLine = true,
                shape = MaterialTheme.shapes.small.copy(
                    bottomEnd = CornerSize(10.dp),
                    bottomStart = CornerSize(10.dp)
                ),
                modifier = Modifier.height(40.dp)
            )

        }
        Button(
            modifier = Modifier.width(300.dp).padding(all = 10.dp)
                .align(Alignment.CenterHorizontally),onClick = {

            }){
            Text("Войти")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TvMedApp_composeTheme {
        Greeting()
    }
}