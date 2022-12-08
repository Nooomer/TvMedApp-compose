package ru.nooomer.tvmedapp_compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import ru.nooomer.tvmedapp_compose.interfaces.*
import ru.nooomer.tvmedapp_compose.models.*
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme

private var result: List<TreatmentModel?>? = null
private var result2: List<UserModel?>? = null
private var viewSize: Int = 0
var data = mutableListOf<MutableList<String?>>()
class TreatmentActivity : ComponentActivity(), PreferenceDataType, RetrorfitFun {
    private lateinit var mContext: Context
    val treatmentFlow = TreatmentModelView().treatmentFlow
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
                    Greeting2(mContext)
                }
            }
        }
    }
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting2(context: Context) {
        var list2 by remember { mutableStateOf<List<TreatmentModel?>?>((listOf(TreatmentModel("Загрузка...","Загрузка...","Загрузка..."))))}
        val isLoading = remember { mutableStateOf(true) }
        LaunchedEffect(Unit) {
            treatmentFlow.collect {
                list2 = it
            }
        }
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
                if(isLoading.value) {

                }
                    Crossfade(targetState = if (isLoading.value) 0f else 1f, animationSpec = spring(
                        dampingRatio = 2f,
                        stiffness = Spring.StiffnessMedium
                    )) { loader ->
                        // note that it's required to use the value passed by Crossfade
                        // instead of your state value
                        if (loader == 1f) {
                            //isLoading.value = false
                        }
                        else{
                            LinearProgressIndicator(Modifier.fillMaxWidth())
                        }
                    }
                }
            if(list2 == null){

            }
            else {
                itemsIndexed(items = list2!!) { pos, _ ->
                    ListItem(
                        list2!![pos]?.doctorSurname,
                        list2!![pos]?.patientSurename,
                        list2!![pos]?.startdate,
                        Modifier.animateItemPlacement(animationSpec = spring(
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
    ){
        ExtendedFloatingActionButton(
            onClick = {
                ssm.clearSession()
                val activity = (context as? Activity)
                context.startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            }
        ){

        }
    }
}
@Composable
@ExperimentalFoundationApi
fun ListItem(doctorSurename: String?, patientSurename: String?, dateOfStart: String?, modifier: Modifier = Modifier) {
    Surface(
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(80.dp).padding(start = 10.dp, end = 10.dp),
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start
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
            Column(
                modifier = modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End
            ) {
                if (dateOfStart != null) {
                    Text(
                        modifier = modifier
                            .padding(end = 6.dp),
                        text = dateOfStart,
                        fontSize = 24.sp
                    )
                }
            }
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
}