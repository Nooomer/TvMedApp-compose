package com.example.tvmedapp_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tvmedapp_compose.ui.theme.TvMedApp_composeTheme

class TreatmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvMedApp_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("Android")

                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                Text(
                    text = "Sticky Header",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                        .background(Color.Gray),
                    textAlign = TextAlign.Center
                )
            }

            item {
                ListItem(text = "First item")
            }

            itemsIndexed(items = (1..10).toList()) { pos, _ ->
                ListItem("Item $pos")
            }

            item {
                ListItem(text = "Last Item")
            }
        }
    Column(
        modifier = Modifier.padding(all = 10.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
    ){
        ExtendedFloatingActionButton(
            onClick = {

            }
        ){

        }
    }
}
@Composable
fun ListItem(text: String, modifier: Modifier = Modifier) {
    Surface(
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(80.dp)
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 6.dp),
                    text = text,
                    fontSize = 24.sp
                )
                Text(
                    modifier = Modifier
                        .padding(start = 6.dp),
                    text = text,
                    fontSize = 24.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 6.dp),
                    text = text,
                    fontSize = 24.sp
                )
                Text(
                    modifier = Modifier
                        .padding(end = 6.dp),
                    text = text,
                    fontSize = 24.sp
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TvMedApp_composeTheme {
        Greeting2("Android")
    }
}