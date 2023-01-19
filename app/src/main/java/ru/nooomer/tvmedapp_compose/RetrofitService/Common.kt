package ru.nooomer.tvmedapp_compose.RetrofitService

import ru.nooomer.tvmedapp_compose.API

object Common {
    private const val BASE_URL = "http://185.87.48.154:8090/"
    val retrofitService: API
        get() = RetrofitClient.getClient(BASE_URL).create(API::class.java)
}