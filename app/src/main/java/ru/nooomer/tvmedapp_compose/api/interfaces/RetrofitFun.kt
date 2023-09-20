package ru.nooomer.tvmedapp_compose.api.interfaces

import org.koin.java.KoinJavaComponent
import retrofit2.Response
import ru.nooomer.tvmedapp_compose.RetrofitService.Common
import ru.nooomer.tvmedapp_compose.api.models.AuthDto
import ru.nooomer.tvmedapp_compose.api.models.LoginData
import ru.nooomer.tvmedapp_compose.api.models.MessageDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto

@Deprecated("Old api fun")
interface RetrofitFun {
    suspend fun get(
        funType: String?,
        table: String?,
        token: String?,
        vararg params: String
    ): List<*>? {
        val common by KoinJavaComponent.inject<Common>(Common::class.java)
        val mService = common.retrofitService2
        var result2: List<TreatmentDto?>?
        var result3: List<MessageDto?>?

        when (funType) {
            "all" -> {
                when (table) {
                    "treatment" -> {
                        val call = mService.getAllTreatment("treatment", token)
                        result2 = call?.execute()?.body()
                        return result2
                    }

                    "messages" -> {
                        val call = mService.getAllMessages(params[0], token)
                        result3 = call?.execute()?.body()
                        return result3
                    }
                }

            }

            "filtered" -> {
                when (table) {
                    "treatment" -> {
                        val call = mService.getTreatmentFromPatientId("treatment", params[0], token)
                        result2 = call?.execute()?.body()
                        return result2
                    }
                }
            }
        }
        return null
    }

    fun auth(login: String, password: String): AuthDto? {
        val common by KoinJavaComponent.inject<Common>(Common::class.java)
        val mService = common.retrofitService2
        val ex_call: Response<AuthDto?>?
        var result: AuthDto? = null
        var result2: List<TreatmentDto?>?
        val call = mService.auth("login", LoginData(login, password))
        ex_call = call?.execute()
        if (ex_call?.code() != 401) {
            result = ex_call?.body()
        } else {
            return null
        }

        return result
    }
}