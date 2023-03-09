package ru.nooomer.tvmedapp_compose

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.nooomer.tvmedapp_compose.interfaces.*
import ru.nooomer.tvmedapp_compose.models.*
import ru.nooomer.tvmedapp_compose.ui.theme.*

private var result: List<TreatmentModel?>? = null
private var result2: List<UserModel?>? = null
private var viewSize: Int = 0
var data = mutableListOf<MutableList<String?>>()
class TreatmentActivity : ComponentActivity(), PreferenceDataType, RetrorfitFun {
    private val cardsViewModel by viewModels<CardsViewModel>()
    private lateinit var mContext: Context
    private val treatmentFlow = TreatmentModelView().treatmentFlow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //load()
        setContent {
            TvMedApp_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) { // the key define when the block is relaunched // Your coroutine code her
                    mContext = LocalContext.current
                    CardsScreen(cardsViewModel)
                    LogoutButton()
                    AddNewTreatmentButton()
                    //Greeting2(mContext)
                }
            }
        }
    }
    @Composable
    private fun AddNewTreatmentButton() {
        Column(
            modifier = Modifier.padding(all = 10.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
        ) {
            ExtendedFloatingActionButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }

    @Composable
    private fun LogoutButton() {
        Column(
            modifier = Modifier.padding(all = 10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom,
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    ssm.clearSession()
                    val activity = (this@TreatmentActivity as? Activity)
                    activity?.startActivity(
                        Intent(
                            this@TreatmentActivity,
                            MainActivity::class.java
                        )
                    )
                    activity?.finish()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun CardsScreen(viewModel: CardsViewModel) {
        val cards by viewModel.cards.collectAsStateWithLifecycle()
        val expandedCardIds by viewModel.expandedCardIdsList.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val bgColour = remember {
            Color(ContextCompat.getColor(context, R.color.white))
        }

        Scaffold(backgroundColor = bgColour) { paddingValues ->
            LazyColumn(Modifier.padding(paddingValues)) {
                items(cards!!, TreatmentModel::id) { card ->
                    ExpandableCard(
                        card = card,
                        onCardArrowClick = { viewModel.onCardArrowClicked(card.id) },
                        onCardMessageClick = { card.id },
                        expanded = expandedCardIds!!.contains(card.id),
                    )
                }
            }
        }
    }
    @SuppressLint("UnusedTransitionTargetStateParameter")
    @Composable
    fun ExpandableCard(
        card: TreatmentModel,
        onCardArrowClick: () -> Unit,
        onCardMessageClick: () -> Unit,
        expanded: Boolean,
    ) {
        val transitionState = remember {
            MutableTransitionState(expanded).apply {
                targetState = !expanded
            }
        }
        val transition = updateTransition(transitionState, label = "transition")
        val cardBgColor by transition.animateColor({
            tween(durationMillis = 450)
        }, label = "bgColorTransition") {
            if (expanded) cardExpandedBackgroundColor else cardCollapsedBackgroundColor
        }
        val cardPaddingHorizontal by transition.animateDp({
            tween(durationMillis = 450)
        }, label = "paddingTransition") {
            if (expanded) 24.dp else 24.dp
        }
        val cardElevation by transition.animateDp({
            tween(durationMillis = 450)
        }, label = "elevationTransition") {
            if (expanded) 24.dp else 4.dp
        }
        val cardRoundedCorners by transition.animateDp({
            tween(
                durationMillis = 450,
                easing = FastOutSlowInEasing
            )
        }, label = "cornersTransition") {
            if (expanded) 0.dp else 16.dp
        }
        val arrowRotationDegree by transition.animateFloat({
            tween(durationMillis = 450)
        }, label = "rotationDegreeTransition") {
            if (expanded) 0f else 180f
        }
        val context = LocalContext.current
        val contentColour = remember {
            Color(ContextCompat.getColor(context, R.color.black))
        }

        Card(
            backgroundColor = cardBgColor,
            contentColor = contentColour,
            elevation = cardElevation,
            shape = RoundedCornerShape(cardRoundedCorners),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = cardPaddingHorizontal,
                    vertical = 8.dp
                )
        ) {
            Column {
                Box {
                    CardTitle(title = "Обращение №${card.id}")
                    CardArrow(
                        degrees = arrowRotationDegree,
                        onClick = onCardArrowClick
                    )
                    ChatButton(Modifier.align(Alignment.CenterEnd), card.id)
                }
                ExpandableContent(visible = expanded, card)
            }
        }
    }

    @Composable
    private fun ChatButton(modifier: Modifier, cardId: Int) {
            IconButton(
                modifier = modifier,
                onClick = {
                    val activity = (mContext as? Activity)
                    mContext.startActivity(Intent(mContext, ChatActivity::class.java))
                    activity?.finish()
                },
                content = {
                    Icon(Icons.Filled.Message, "message")
                }
            )
    }

    @Composable
    fun CardArrow(
        degrees: Float,
        onClick: () -> Unit
    ) {
        IconButton(
            onClick = onClick,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_expand_less_24),
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier.rotate(degrees),
                )
            }
        )
    }

    @Composable
    fun CardTitle(title: String) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }

    @Composable
    fun ExpandableContent(
        visible: Boolean = true,
        card: TreatmentModel
    ) {
        val enterTransition = remember {
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(450)
            ) + fadeIn(
                initialAlpha = 0.3f,
                animationSpec = tween(450)
            )
        }
        val exitTransition = remember {
            shrinkVertically(
                // Expand from the top.
                shrinkTowards = Alignment.Top,
                animationSpec = tween(450)
            ) + fadeOut(
                // Fade in with the initial alpha of 0.3f.
                animationSpec = tween(450)
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.Top) {
                //Spacer(modifier = Modifier.heightIn(100.dp))
//                Row(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (card.doctorSurname != null) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 6.dp),
                                text = "Врач: ${card.doctorSurname!!}",
                                fontSize = 24.sp
                            )
                        }
                        if (card.patientSurename != null) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 6.dp),
                                text = "Пациент: ${card.patientSurename!!}",
                                fontSize = 24.sp
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (card.startdate != null) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 6.dp),
                                text = "Дата создания: ${card.startdate}",
                                fontSize = 24.sp
                            )
                        }
                    }
                //}
            }
        }
    }
    @Composable
    fun Alert(): Boolean{
        val tmp  = remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                tmp.value = false
            },
            title = {
                Text(text = "Title")
            },
            text = {
                Text(text = "Turned on by default")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        tmp.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        tmp.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
        return tmp.value
}

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Greeting2(context: Context) {
        var list2 by remember {
            mutableStateOf<List<TreatmentModel?>?>(
                (listOf(
                    TreatmentModel(
                        1,
                        "Загрузка...",
                        "Загрузка...",
                        "Загрузка..."
                    )
                ))
            )
        }
        val isLoading = remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            treatmentFlow.collect {
                list2 = it
            }
        }
        val openDialog = remember { mutableStateOf(false) }

        if (openDialog.value) {
            openDialog.value = Alert()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openDialog.value = true },
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
                if (isLoading.value) {

                }
                Crossfade(
                    targetState = if (isLoading.value) 0f else 1f, animationSpec = spring(
                        dampingRatio = 2f,
                        stiffness = Spring.StiffnessMedium
                    )
                ) { loader ->
                    // note that it's required to use the value passed by Crossfade
                    // instead of your state value
                    if (loader == 1f) {
                        //isLoading.value = false
                    } else {
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                    }
                }
            }
            if (list2 == null) {

            } else {
                itemsIndexed(items = list2!!) { pos, _ ->
                    ListItem(
                        list2!![pos]?.doctorSurname,
                        list2!![pos]?.patientSurename,
                        list2!![pos]?.startdate,
                        Modifier.animateItemPlacement(
                            animationSpec = spring(
                                dampingRatio = 2f,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    )
                    if ((pos == list2!!.lastIndex) and (list2!!.lastIndex != 0)) {
                        isLoading.value = false
                    }
                }
            }
        }
        Column(
            modifier = Modifier.padding(all = 10.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    ssm.clearSession()
                    val activity = (context as? Activity)
                    context.startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                }
            ) {

            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    @ExperimentalFoundationApi
    fun ListItem(doctorSurename: String?, patientSurename: String?, dateOfStart: String?, modifier: Modifier = Modifier) {
        Surface(
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .height(80.dp)
                .padding(start = 10.dp, end = 10.dp),
        ) {
                Column(
                    modifier = modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (doctorSurename != null) {
                        Text(
                            modifier = modifier
                                .padding(start = 6.dp),
                            text = doctorSurename,
                            fontSize = 24.sp
                        )
                    }
                    if (patientSurename != null) {
                        Text(
                            modifier = modifier
                                .padding(start = 6.dp),
                            text = patientSurename,
                            fontSize = 24.sp
                        )
                    }
                }
            }
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (dateOfStart != null) {
                    Text(
                        modifier = modifier.fillMaxWidth()
                            .padding(end = 6.dp),
                        text = dateOfStart,
                        fontSize = 24.sp
                    )
                }
            }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        TvMedApp_composeTheme {
            Greeting2(LocalContext.current)
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun ExpandleCard() {
        TvMedApp_composeTheme {
            ExpandableContent(true, TreatmentModel(0,"Ампилогов","Ампилогов","12-12-2022 14:12:12 07"))
        }
    }
}