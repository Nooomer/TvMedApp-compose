package ru.nooomer.tvmedapp_compose.interfaces

import ru.nooomer.tvmedapp_compose.RetrofitService.Common
import ru.nooomer.tvmedapp_compose.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface RetrofitFun {
   suspend fun get(funType: String?, table: String?, token: String?, vararg params: String): List<*>? {
        val mService = Common.retrofitService
        var result: List<UserModel?>?
        var result2: List<TreatmentModel?>?
       var result3: List<MessagesModel?>?

        when (funType) {
            "all" -> {
                when(table){
                    "user" ->{
                        mService.getAllUser(
                            "patient",
                            token)?.enqueue(object : Callback<List<UserModel?>?> {
                                override fun onResponse(
                                    call: Call<List<UserModel?>?>?,
                                    getResponse: Response<List<UserModel?>?>?
                                ) {
                                   result = getResponse?.body()
                                }
                            override fun onFailure(call: Call<List<UserModel?>?>, t: Throwable) {
                                result = null
                            }
                        })
                    }
                    "treatment" ->{
                        val call = mService.getAllTreatment("treatment", token)
                        result2 =  call?.execute()?.body()
                        return result2
                    }
                    "messages" ->{
                        val call = mService.getAllMessages(params[0], token)
                        result3 =  call?.execute()?.body()
                        return result3
                    }
                }

            }
            "filtered" ->{
                when(table){
                    "treatment" ->{
                        val call = mService.getTreatmentFromPatientId("treatment", params[0], token)
                        result2 =  call?.execute()?.body()
                        return result2
                    }
                }
            }
        }
        return null
    }

    fun auth(login: String?, password: String?): AuthModel? {
        val mService = Common.retrofitService
        val ex_call: Response<AuthModel?>?
        var result: AuthModel? = null
        var result2: List<TreatmentModel?>?
        val call = mService.auth("login", LoginData(login, password))
        ex_call =  call?.execute()
        if(ex_call?.code()!=401){
            result = ex_call?.body()
        }
        else{
            return null
        }

        return result
    }
}