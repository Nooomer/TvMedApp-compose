package ru.nooomer.tvmedapp_compose.api

import android.content.Context
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import ru.nooomer.tvmedapp_compose.BuildConfig
import ru.nooomer.tvmedapp_compose.RetrofitService.Common
import ru.nooomer.tvmedapp_compose.api.interfaces.ChatApi
import ru.nooomer.tvmedapp_compose.api.interfaces.PredictApi
import ru.nooomer.tvmedapp_compose.api.interfaces.SymptomApi
import ru.nooomer.tvmedapp_compose.api.interfaces.TreatmentApi
import ru.nooomer.tvmedapp_compose.api.interfaces.UserAuthApi
import ru.nooomer.tvmedapp_compose.api.models.ChatDto
import ru.nooomer.tvmedapp_compose.api.models.LoginData
import ru.nooomer.tvmedapp_compose.api.models.NewMessageDto
import ru.nooomer.tvmedapp_compose.api.models.NewTreatmentDto
import ru.nooomer.tvmedapp_compose.api.models.PredictDto
import ru.nooomer.tvmedapp_compose.api.models.SoundDto
import ru.nooomer.tvmedapp_compose.api.models.SymptomDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.UUID


object API : KoinComponent {
	private val common by inject<Common>()
	val BASE_URL = BuildConfig.API_FULL_URL
	private val authApiService = common.initRetrofit<UserAuthApi>(BASE_URL)
	private val treatmentApiService = common.initRetrofit<TreatmentApi>(BASE_URL)
	private val symptomsApiService = common.initRetrofit<SymptomApi>(BASE_URL)
	private val chatApiService = common.initRetrofit<ChatApi>(BASE_URL)
	private val predictApiService = common.initRetrofit<PredictApi>("https://nooomer.site")
	fun login(login: String, password: String): Any {
		val tmp = authApiService.login(LoginData(login, password)).execute().body()!!
		return tmp
	}

	fun getPredict(file: File, contex: Context): List<PredictDto>? {
		val requestFile =
			RequestBody.create(
				MediaType.get("multipart/form-data"),
				file
			)
		val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
		return predictApiService.getPredict(body).execute().body()
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
		if (response.isSuccessful) {
			return response.body()
		}
		return null
	}

	fun getAllAudio(treatmentId: UUID): MutableSet<SoundDto> {
		return chatApiService.getAllAudioForTreatment(treatmentId).execute().body()!!
	}

	fun getOneAudio(treatmentId: String, count: Int, absolutePath: String): File {
		val responseBody =
			chatApiService.getSelectedAudioForTreatment(treatmentId, count).execute().body()!!
		val file = File("${absolutePath}/${treatmentId}_${count}.mp3")
		try {
			file.createNewFile()
			Files.write(file.toPath(), responseBody.bytes())
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return file
	}

	fun storeAudio(treatmentId: String, file: File): Int {
		val requestFile =
			RequestBody.create(
				MediaType.get("multipart/form-data"),
				file
			)
		val body =
			MultipartBody.Part.createFormData("file", file.name, requestFile)
		return chatApiService.sendAudio(treatmentId, body).execute().code()
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