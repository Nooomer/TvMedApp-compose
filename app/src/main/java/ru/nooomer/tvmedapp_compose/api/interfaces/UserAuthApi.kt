package ru.nooomer.tvmedapp_compose.api.interfaces

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.nooomer.tvmedapp_compose.api.models.AuthDto
import ru.nooomer.tvmedapp_compose.api.models.LoginData

/**
 *Интерфейс для доступа до АПИ методов
 */
interface UserAuthApi {
	/**
	 * Post метоод для входа.
	 * @param body тело запроса в формате [LoginData]
	 * @return [Call] типа [AuthDto]
	 */
	@POST("user/login")
	fun login(
		@Body body: LoginData?
	): Call<AuthDto>

	/**
	 * Метод для проверки текущего статуса входа
	 * @return [Call] типа [Boolean]
	 */
	@GET("user/login")
	fun checkLogin(): Call<Boolean>

	/**
	 * Метод для выхода из аккаунта
	 * @return [Call] типа [Void]
	 */
	@GET("user/logout")
	fun logout(): Call<Void>
}