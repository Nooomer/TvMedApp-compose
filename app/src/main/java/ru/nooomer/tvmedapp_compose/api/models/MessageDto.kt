package ru.nooomer.tvmedapp_compose.api.models

import java.util.UUID

data class MessageDto(
    var id: UUID,
    var createdDate: String,
    var updatedDate: String?,
    var messageText: String,
    var from: UUID,
    var to: UUID,
)
