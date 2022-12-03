package com.example.tvmedapp_compose.interfaces

import com.example.tvmedapp_compose.RetrifitService.Common
import com.example.tvmedapp_compose.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface RetrorfitFun {
    fun get(funType: String?, table: String?, token: String?, vararg params: String): List<*>? {
        val mService = Common.retrofitService
        var result: List<UserModel?>?
        var result2: List<TreatmentModel?>?

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