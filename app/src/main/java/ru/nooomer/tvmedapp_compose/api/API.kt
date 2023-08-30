package ru.nooomer.tvmedapp_compose.api

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nooomer.tvmedapp_compose.RetrofitService.Common
import ru.nooomer.tvmedapp_compose.api.interfaces.SymptomApi
import ru.nooomer.tvmedapp_compose.api.interfaces.TreatmentApi
import ru.nooomer.tvmedapp_compose.api.interfaces.UserAuthApi
import ru.nooomer.tvmedapp_compose.api.models.LoginData
import ru.nooomer.tvmedapp_compose.api.models.SymptomDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto

object API : KoinComponent {
    private val common by inject<Common>()
    private val authService = common.initRetrofit<UserAuthApi>()
    private val treatmentService = common.initRetrofit<TreatmentApi>()
    private val symptomsService = common.initRetrofit<SymptomApi>()
    fun login(login: String, password: String): Any {
        return authService.login(LoginData(login, password)).execute().body()!!
    }

    fun checkLogin(): Boolean {
        return authService.checkLogin().execute().body()!!
    }

    fun getTreatmentForUser(): Any? {
        val result = treatmentService.getTreatment().execute()
        return if(!result.isSuccessful){
            //result.errorBody() as ErrorDto?
            listOf<TreatmentDto>()
        }
        else{
         result.body()
        }
    }

    fun getSymptoms(): List<SymptomDto>? {
        return symptomsService.getSymptoms().execute().body()
    }
}