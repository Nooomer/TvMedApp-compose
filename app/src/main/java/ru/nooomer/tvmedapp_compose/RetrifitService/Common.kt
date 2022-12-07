package ru.nooomer.tvmedapp_compose.RetrifitService

import ru.nooomer.tvmedapp_compose.API

object Common {
    private const val BASE_URL = "https://c8f5-95-105-124-220.eu.ngrok.io/"
    val retrofitService: API
        get() = RetrofitClient.getClient(BASE_URL).create(API::class.java)
}