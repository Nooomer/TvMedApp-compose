package com.example.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import com.example.tvmedapp_compose.interfaces.PreferenceDataType
import com.example.tvmedapp_compose.interfaces.RetrorfitFun
import com.example.tvmedapp_compose.ssm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class TreatmentModelView: ViewModel(), PreferenceDataType, RetrorfitFun {
    private var result: List<TreatmentModel?>? = null
    val treatmentFlow: Flow<List<TreatmentModel?>?> = flow{
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
        }
        }
    }
}