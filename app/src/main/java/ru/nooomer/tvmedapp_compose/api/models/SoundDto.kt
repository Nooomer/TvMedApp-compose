package ru.nooomer.tvmedapp_compose.api.models

import java.util.UUID

data class SoundDto(
    var id: UUID,
    var createdDate: String,
    var updatedDate: String?,
    var soundLink: String,
)
