package ru.nooomer.tvmedapp_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.nooomer.tvmedapp_compose.models.MessagesModel
import ru.nooomer.tvmedapp_compose.models.MessagesViewModel
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessagesScreen(viewModel: MessagesViewModel) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val expandedMesssagesIds by viewModel.expandedMessagesIdsList.collectAsStateWithLifecycle()
    var textValue = remember { mutableStateOf("") }
    var textChanged = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val bgColour = remember {
        Color(ContextCompat.getColor(context, androidx.appcompat.R.color.material_grey_300))
    }

    Scaffold(backgroundColor = bgColour) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize(),
            //verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Чат обращения",
                    )
                }
                items(messages!!, MessagesModel::id) { message ->

                    var arragement: Arrangement.Horizontal =
                        if (message.fromId == ssm.fetch(ssm.USER_ID)?.toInt()) {
                            Arrangement.End
                        } else {
                            Arrangement.Start
                        }
                    MessageBubble(Modifier, arragement, message)
                }
            }
            TextField(
                textValue.value,
                {
                    textValue.value = it
                    textChanged.value = true
                },
                Modifier
                    //.defaultMinSize(minHeight = 100.dp)
                    .requiredHeightIn(min = 130.dp)
                    .padding(start = 10.dp, bottom = 10.dp)
                )
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
    val bubleColour = MaterialTheme.colorScheme.primary
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
                            color = bubleColour,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .padding(6.dp),
                color = Color.White,
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