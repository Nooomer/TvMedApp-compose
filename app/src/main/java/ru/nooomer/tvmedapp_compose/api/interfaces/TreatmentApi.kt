package ru.nooomer.tvmedapp_compose.api.interfaces

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.nooomer.tvmedapp_compose.api.models.NewTreatmentDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto

interface TreatmentApi {
    @GET("treatment")
    fun getTreatment(): Call<List<TreatmentDto>>

    @POST("treatment")
    fun addTreatment(@Body treatmentDto: NewTreatmentDto): Call<List<TreatmentDto>>
}