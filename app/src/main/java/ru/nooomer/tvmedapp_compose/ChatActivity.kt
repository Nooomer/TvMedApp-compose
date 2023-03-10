package ru.nooomer.tvmedapp_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.nooomer.tvmedapp_compose.models.*
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme

class ChatActivity : ComponentActivity() {
    private val messagesViewModel by viewModels<MessagesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvMedApp_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    MessagesScreen(messagesViewModel)
                }
            }
        }
    }
}
@Composable
fun MessagesScreen(viewModel: MessagesViewModel) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val expandedMesssagesIds by viewModel.expandedMessagesIdsList.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val bgColour = remember {
        Color(ContextCompat.getColor(context, R.color.white))
    }

    Scaffold(backgroundColor = bgColour) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            items(messages!!, MessagesModel::id) { message ->

                var arragement: Arrangement.Horizontal =
                    if(message.fromId == ssm.fetch(ssm.USER_ID)?.toInt()){
                    Arrangement.End
                } else{
                    Arrangement.Start
                }
                MessageBubble(Modifier, arragement, message)
            }
        }
    }
}
@Composable
fun MessagesList(modifier: Modifier, message: MessagesModel, arragement: Arrangement.Horizontal) {
            MessageBubble(modifier, arragement, message)
}
@Composable
private fun MessageBubble(
    modifier: Modifier,
    allignment: Arrangement.Horizontal,
    message: MessagesModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = allignment,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (message.messageText != null) {
            Text(
                modifier = Modifier
                    .widthIn(max = 220.dp)
                    .drawBehind {
                        drawRoundRect(
                            Color(0xFFBBAAEE),
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .padding(6.dp),
                text = message.messageText!!,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun Message(type: Boolean){
    /*Column {
        Box {
            CardTitle(title = "Обращение №${card.id}")
            CardArrow(
                degrees = arrowRotationDegree,
                onClick = onCardArrowClick
            )
            ChatButton(Modifier.align(Alignment.CenterEnd), card.id)
        }
        ExpandableContent(visible = expanded, card)
    }*/
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TvMedApp_composeTheme {
        Greeting("Android")
    }
}