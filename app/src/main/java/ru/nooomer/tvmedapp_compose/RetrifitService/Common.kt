package ru.nooomer.tvmedapp_compose.RetrifitService

import ru.nooomer.tvmedapp_compose.API

object Common {
    private const val BASE_URL = "https://44a8-79-126-115-109.eu.ngrok.io/"
    val retrofitService: API
        get() = RetrofitClient.getClient(BASE_URL).create(API::class.java)
}