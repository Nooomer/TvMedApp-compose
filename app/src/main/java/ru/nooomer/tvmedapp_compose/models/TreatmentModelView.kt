package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nooomer.tvmedapp_compose.api.API
import ru.nooomer.tvmedapp_compose.api.interfaces.RetrofitFun
import ru.nooomer.tvmedapp_compose.api.models.ErrorDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType

class TreatmentModelView: ViewModel(), PreferenceDataType, RetrofitFun {
    val treatmentFlow: Flow<List<TreatmentDto>?> = flow{
            var result = API.getTreatmentForUser()
        if (result?.javaClass== ErrorDto::class.java){

        }
        else{
            emit(result as List<TreatmentDto>)
        }

    }
}