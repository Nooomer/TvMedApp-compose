package ru.nooomer.tvmedapp_compose.api.models

data class NewTreatmentDto(
    var symptom: MutableSet<SymptomDto>,
)
