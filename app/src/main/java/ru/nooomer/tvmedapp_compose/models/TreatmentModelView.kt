package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.interfaces.RetrofitFun
import ru.nooomer.tvmedapp_compose.ssm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class TreatmentModelView: ViewModel(), PreferenceDataType, RetrofitFun {
    private var result: List<TreatmentModel>? = null
    val treatmentFlow: Flow<List<TreatmentModel>?> = flow{
        when (ssm.fetch(USER_TYPE)) {
            "doctor" -> {

                    result =
                        withContext(Dispatchers.IO) {
                        get(
                            "all",
                            "treatment", "Bearer " + ssm.fetch(USER_TOKEN)
                        ) as List<TreatmentModel>?
                }
                emit(result)
            }
            "patient" -> {
                result =
                    get(
                        "filtered",
                        "treatment", "Bearer " + ssm.fetch(USER_TOKEN), ssm.fetch(USER_ID)!!
                    ) as List<TreatmentModel>?
        }
        }
    }
}