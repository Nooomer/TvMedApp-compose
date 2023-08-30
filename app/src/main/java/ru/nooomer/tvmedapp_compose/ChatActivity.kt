package ru.nooomer.tvmedapp_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import ru.nooomer.tvmedapp_compose.api.models.MessageDto
import ru.nooomer.tvmedapp_compose.models.MessagesViewModel
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme
import ru.nooomer.tvmedapp_compose.ui.theme.textFieldColor

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

    Scaffold { paddingValues ->
        Box(
            Modifier.fillMaxSize()
        ) {
            MessageList(paddingValues, messages)
            MessageButtons(textValue, textChanged, Modifier.align(Alignment.BottomStart))
        }
    }
}
@Composable
private fun MessageButtons(
    textValue: MutableState<String>,
    textChanged: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .background(
                Color.White,
                shape = MaterialTheme.shapes.small.copy(
                    bottomEnd = CornerSize(10.dp),
                    bottomStart = CornerSize(10.dp)
                )
            )
            .fillMaxWidth()
            .padding(start = 10.dp, bottom = 6.dp, end = 10.dp)
    ) {
        TextField(
            textValue.value,
            {
                textValue.value = it
                textChanged.value = true
            },
            Modifier
                //.defaultMinSize(minHeight = 100.dp)
                .height(70.dp)
                //.requiredHeightIn(min = 130.dp, max = 130.dp)
                .padding(start = 10.dp, bottom = 10.dp, top = 3.dp),
            colors = textFieldColor!!,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MessageList(
    paddingValues: PaddingValues,
    messages: List<MessageDto>?
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
        items(messages!!, MessageDto::id) { message ->

            var arragement: Arrangement.Horizontal =
                if (message.from == ssm.fetch(ssm.USER_ID)) {
                    Arrangement.End
                } else {
                    Arrangement.Start
                }
            MessageBubble(arragement, message)
        }
    }
}

@Composable
private fun MessageBubble(
    allignment: Arrangement.Horizontal,
    message: MessageDto
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