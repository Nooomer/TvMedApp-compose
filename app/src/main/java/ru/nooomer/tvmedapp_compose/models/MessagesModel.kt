package ru.nooomer.tvmedapp_compose.models

import java.util.*

data class MessagesModel(
    var id: Int,
    var fromId: Int,
    var toId: Int,
    var messageText: String?,
    var sendTime: String,
    )


