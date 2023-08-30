package ru.nooomer.tvmedapp_compose.api.models

import java.util.UUID

data class TreatmentDto(
        var id: UUID,
        var createdDate: String,
        var updatedDate: String?,
        var chat: ChatDto?,
        var patient: UserDto,
        var doctor: UserDto?,
        var conclusion: ConclusionDto?,
        var symptom: MutableSet<SymptomDto>,
)
