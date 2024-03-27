package ru.nooomer.tvmedapp_compose.api

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import ru.nooomer.tvmedapp_compose.RetrofitService.Common
import ru.nooomer.tvmedapp_compose.api.interfaces.ChatApi
import ru.nooomer.tvmedapp_compose.api.interfaces.SymptomApi
import ru.nooomer.tvmedapp_compose.api.interfaces.TreatmentApi
import ru.nooomer.tvmedapp_compose.api.interfaces.UserAuthApi
import ru.nooomer.tvmedapp_compose.api.models.ChatDto
import ru.nooomer.tvmedapp_compose.api.models.LoginData
import ru.nooomer.tvmedapp_compose.api.models.NewMessageDto
import ru.nooomer.tvmedapp_compose.api.models.NewTreatmentDto
import ru.nooomer.tvmedapp_compose.api.models.SymptomDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto
import java.util.UUID

object API : KoinComponent {
	private val common by inject<Common>()
	private val authApiService = common.initRetrofit<UserAuthApi>()
	private val treatmentApiService = common.initRetrofit<TreatmentApi>()
	private val symptomsApiService = common.initRetrofit<SymptomApi>()
	private val chatApiService = common.initRetrofit<ChatApi>()
	fun login(login: String, password: String): Any {
		return authApiService.login(LoginData(login, password)).execute().body()!!
	}

	fun checkLogin(): Boolean {
		return authApiService.checkLogin().execute().body()!!
	}

	fun getTreatmentForUser(): List<TreatmentDto>? {
		return treatmentApiService.getTreatment().execute().body()
	}

	fun getSymptoms(): List<SymptomDto>? {
		return symptomsApiService.getSymptoms().execute().body()
	}

	fun getChat(treatmentId: UUID): ChatDto? {
		val response = chatApiService.getChat(treatmentId).execute()
		if (response.isSuccessful)
	}

	fun sendMessage(treatmentId: UUID, newMessageDto: NewMessageDto): Call<ChatDto> {
		return chatApiService.sendMessage(treatmentId, newMessageDto)
	}

	fun addNewTreatment(newTreatmentDto: NewTreatmentDto): Call<List<TreatmentDto>> {
		return treatmentApiService.addTreatment(newTreatmentDto)
	}

	fun <T : Any> Call<T>.executeAndGetBody(): T? {
		return this.execute().body()
	}

	fun <T : Any> Call<T>.justExecute(): Boolean {
		return this.execute().isSuccessful
	}
}