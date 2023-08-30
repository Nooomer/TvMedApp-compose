package ru.nooomer.tvmedapp_compose.api.models

class ErrorDto(
    var timestamp: String,
    var status: Int,
    var error: String,
    var message: String,
    var path: String
)