package ru.nooomer.tvmedapp_compose.api.interfaces

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import ru.nooomer.tvmedapp_compose.api.models.ChatDto
import ru.nooomer.tvmedapp_compose.api.models.NewMessageDto
import ru.nooomer.tvmedapp_compose.api.models.SoundDto
import java.util.UUID

/**
 *Интерфейс для доступа до АПИ методов
 */

interface ChatApi {
	@GET("treatment/chat/{treatment-id}")
	fun getChat(@Path("treatment-id") treatmentId: UUID?): Call<ChatDto>

	@POST("treatment/chat/{treatment-id}")
	fun sendMessage(
		@Path("treatment-id") treatmentId: UUID?,
		@Body newMessageDto: NewMessageDto
	): Call<ChatDto>

	@Multipart
	@PUT("treatment/chat/{treatment-id}")
	fun sendAudio(
		@Path("treatment-id") treatmentId: String?,
		@Part file: MultipartBody.Part
	): Call<ResponseBody>

	@GET("treatment/chat/{treatment-id}/audio")
	fun getAllAudioForTreatment(@Path("treatment-id") treatmentId: UUID?): Call<MutableSet<SoundDto>>

	@GET("treatment/chat/{treatment-id}/audio/{count}")
	fun getSelectedAudioForTreatment(
		@Path("treatment-id") treatmentId: String?,
		@Path("count") count: Int
	): Call<ResponseBody>
}