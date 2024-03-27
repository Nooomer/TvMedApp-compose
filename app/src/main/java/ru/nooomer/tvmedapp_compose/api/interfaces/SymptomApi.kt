package ru.nooomer.tvmedapp_compose.api.interfaces

import retrofit2.Call
import retrofit2.http.GET
import ru.nooomer.tvmedapp_compose.api.models.SymptomDto

/**
 *Интерфейс для доступа до АПИ методов
 */
interface SymptomApi {
	@GET("symptoms")
	fun getSymptoms(): Call<List<SymptomDto>>
}