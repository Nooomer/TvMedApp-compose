package ru.nooomer.tvmedapp_compose.api.models

import java.util.UUID

data class ChatDto(
    var id: UUID,
    var createdDate: String,
    var updatedDate: String?,
    var message: MutableSet<MessageDto>?,
)