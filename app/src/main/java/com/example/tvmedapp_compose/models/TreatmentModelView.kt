package com.example.tvmedapp_compose.models

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.tvmedapp_compose.data
import com.example.tvmedapp_compose.interfaces.PreferenceDataType
import com.example.tvmedapp_compose.interfaces.RetrorfitFun
import com.example.tvmedapp_compose.ssm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*

class TreatmentModelView: ViewModel(), PreferenceDataType, RetrorfitFun {
    private var result: List<TreatmentModel?>? = null
    val treatmentFlow: Flow<List<TreatmentModel?>?> = flow{
        val scope = CoroutineScope(Dispatchers.Main + Job())
        fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
        when (ssm.fetch(USER_TYPE)) {
            "doctor" -> {

                    result =
                        withContext(Dispatchers.IO) {
                        get(
                            "all",
                            "treatment", "Bearer " + ssm.fetch(USER_TOKEN)
                        ) as List<TreatmentModel?>?
                }
                emit(result)
            }
            "patient" -> {
                result =
                    get(
                        "filtered",
                        "treatment", "Bearer " + ssm.fetch(USER_TOKEN), ssm.fetch(USER_ID)!!
                    ) as List<TreatmentModel?>?
            for (i in 0 until result!!.size) {
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