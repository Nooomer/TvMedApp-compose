package ru.nooomer.tvmedapp_compose.api.interfaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.nooomer.tvmedapp_compose.api.models.ChatDto
import java.util.UUID

interface ChatApi {
    @GET("treatment/chat/{treatment-id}")
    fun getChat(@Path("treatment-id") treatmentId: UUID?): Call<ChatDto>
}