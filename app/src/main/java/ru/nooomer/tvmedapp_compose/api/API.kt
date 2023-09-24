package ru.nooomer.tvmedapp_compose.api

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nooomer.tvmedapp_compose.RetrofitService.Common
import ru.nooomer.tvmedapp_compose.api.interfaces.ChatApi
import ru.nooomer.tvmedapp_compose.api.interfaces.SymptomApi
import ru.nooomer.tvmedapp_compose.api.interfaces.TreatmentApi
import ru.nooomer.tvmedapp_compose.api.interfaces.UserAuthApi
import ru.nooomer.tvmedapp_compose.api.models.ChatDto
import ru.nooomer.tvmedapp_compose.api.models.LoginData
import ru.nooomer.tvmedapp_compose.api.models.NewMessageDto
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

	fun getTreatmentForUser(): Any? {
		val result = treatmentApiService.getTreatment().execute()
		return if (!result.isSuccessful) {
			//result.errorBody() as ErrorDto?
			listOf<TreatmentDto>()
		} else {
			result.body()
		}
	}

	fun getSymptoms(): List<SymptomDto>? {
		return symptomsApiService.getSymptoms().execute().body()
	}

	fun getChat(treatmentId: UUID): ChatDto? {
		return chatApiService.getChat(treatmentId).execute().body()
	}

	fun sendMessage(treatmentId: UUID, newMessageDto: NewMessageDto): ChatDto? {
		return chatApiService.sendMessage(treatmentId, newMessageDto).execute().body()
	}
}