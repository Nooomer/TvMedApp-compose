package ru.nooomer.tvmedapp_compose.api.interfaces

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.nooomer.tvmedapp_compose.api.models.ChatDto
import ru.nooomer.tvmedapp_compose.api.models.NewMessageDto
import java.util.UUID

interface ChatApi {
	@GET("treatment/chat/{treatment-id}")
	fun getChat(@Path("treatment-id") treatmentId: UUID?): Call<ChatDto>

	@POST("treatment/chat/{treatment-id}")
	fun sendMessage(
		@Path("treatment-id") treatmentId: UUID?,
		@Body newMessageDto: NewMessageDto
	): Call<ChatDto>
}