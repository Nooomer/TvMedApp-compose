package com.example.tvmedapp_compose.RetrifitService

import com.example.tvmedapp_compose.API

object Common {
    private const val BASE_URL = "https://7260-83-149-19-254.eu.ngrok.io/"
    val retrofitService: API
        get() = RetrofitClient.getClient(BASE_URL).create(API::class.java)
}