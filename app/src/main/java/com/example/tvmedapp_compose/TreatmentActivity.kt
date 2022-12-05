package com.example.tvmedapp_compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.tvmedapp_compose.interfaces.*
import com.example.tvmedapp_compose.models.*
import com.example.tvmedapp_compose.ui.theme.TvMedApp_composeTheme

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

    override fun onRestart() {
        super.onRestart()
    }

    /*fun load(){
        when (ssm.fetch(USER_TYPE)) {
            "doctor" ->
            {
                scope.launch {
                    val def = scope.asyncIO {
                        result =
                            get(
                                "all",
                                "treatment","Bearer "+ssm.fetch(USER_TOKEN)
                            ) as List<TreatmentModel?>?
                    }
                    def.await()
                    try {
                        viewSize = result!!.size
                        for (i in 0 until viewSize) {
                            data.add(
                                mutableListOf<String?>(
                                    result!![i]?.patientSurename,
                                    result!![i]?.doctorSurname,
                                    result!![i]?.startdate
                                )
                            )

                        }
                    }
                    catch (e: NullPointerException){
                        val toast = Toast.makeText(
                            applicationContext,
                            "Данные не загружены",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
            "patient"->{
                scope.launch {
                    val def = scope.asyncIO {
                        result =
                            get(
                                "filtered",
                                "treatment", "Bearer " + ssm.fetch(USER_TOKEN), ssm.fetch(USER_ID)!!
                            ) as List<TreatmentModel?>?
                    }
                    def.await()
                }
                    try {
                        viewSize = result!!.size
                    }
                    catch (e: NullPointerException){
                        val toast = Toast.makeText(
                            applicationContext,
                            "Данные не загружены",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                    for (i in 0 until viewSize) {
                        data.add(
                            mutableListOf<String?>(
                                result!![i]?.patientSurename,
                                result!![i]?.doctorSurname,
                                result!![i]?.startdate
                            )
                        )
                    }
            }
        }
    }
}
*/
@OptIn(ExperimentalFoundationApi::class
)
@Composable
fun Greeting2(context: Context) {
        var list2 by remember { mutableStateOf<List<TreatmentModel?>?>(listOf(TreatmentModel("Загрузка...","Загрузка...","Загрузка...")))}
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
                LinearProgressIndicator()
            }
            itemsIndexed(items = list2!!) { pos, _ ->
                ListItem(list2!![pos]?.doctorSurname, list2!![pos]?.patientSurename, list2!![pos]?.startdate)
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
fun ListItem(doctorSurename: String?, patientSurename: String?, dateOfStart: String?, modifier: Modifier = Modifier) {
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
                if (doctorSurename != null) {
                    Text(
                        modifier = Modifier
                            .padding(start = 6.dp),
                        text = doctorSurename,
                        fontSize = 24.sp
                    )
                }
                if (patientSurename != null) {
                    Text(
                        modifier = Modifier
                            .padding(start = 6.dp),
                        text = patientSurename,
                        fontSize = 24.sp
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End
            ) {
                if (dateOfStart != null) {
                    Text(
                        modifier = Modifier
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