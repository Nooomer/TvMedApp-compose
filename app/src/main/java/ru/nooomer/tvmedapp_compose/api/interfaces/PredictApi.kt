package ru.nooomer.tvmedapp_compose.api.interfaces

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.nooomer.tvmedapp_compose.api.models.PredictDto

interface PredictApi {
	@Multipart
	@POST("predict")
	fun getPredict(@Part file: MultipartBody.Part): Call<List<PredictDto>>
}