package ru.nooomer.tvmedapp_compose.api.models

import java.util.UUID

data class NewTreatmentDto(
	var patient: UserDto? = null,
	var symptomIds: MutableSet<UUID>,
)
