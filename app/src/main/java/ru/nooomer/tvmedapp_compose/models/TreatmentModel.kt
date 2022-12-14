package ru.nooomer.tvmedapp_compose.models

data class TreatmentModel(
        var id: Int,
        var patientSurename: String?,
        var doctorSurname: String?,
        val startdate: String?
)
