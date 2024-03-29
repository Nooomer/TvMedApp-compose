package ru.nooomer.tvmedapp_compose.api.interfaces

import retrofit2.Call
import retrofit2.http.*
import ru.nooomer.tvmedapp_compose.api.models.AuthDto
import ru.nooomer.tvmedapp_compose.api.models.DoctorModel
import ru.nooomer.tvmedapp_compose.api.models.LoginData
import ru.nooomer.tvmedapp_compose.api.models.MessageDto
import ru.nooomer.tvmedapp_compose.api.models.SymptomDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto
import ru.nooomer.tvmedapp_compose.models.*

/**Implementation of api methods and their parameters. Any method expects the name of the api method to be passed in the form method_name.php*/
@Deprecated("Old Api method")
interface APIAll {

    /**Auth methods.
     * @param body is a Json Body like [LoginData]
     *@return [AuthDto] */
    @POST("/user/login")
    fun auth(
        @Path("method") methodName: String?,
        @Body body: LoginData?
    ): Call<AuthDto?>?



    @GET("/{method}")
            /**Get all treatment methods.
             * @return [TreatmentDto]*/
    fun getAllTreatment(
        @Path("method") methodName: String?,
        @Header("Authorization") token: String?
    ): Call<List<TreatmentDto?>?>?

    @GET("treatment/{id}/chat")
    fun getAllMessages(
        @Path("id") chatId: String?,
        @Header("Authorization") token: String?
    ): Call<List<MessageDto?>?>?

    @GET("/{method}/patient/{id}")
            /**Get treatment by ID methods.
             *  @param id Treatment id information about which need get. Type - [String]
             *  @return [TreatmentDto]*/
    fun getTreatmentFromPatientId(
        @Path("method") methodName: String?,
        @Path("id") id: String?,
        @Header("Authorization") token: String?
    ): Call<List<TreatmentDto?>?>?

    @GET("/{method}")
            /**Get treatment for User by phone number.
             * @param phoneNumber it Param use for get Treatment for user
             * @return [TreatmentDto]*/
    fun getTreatmentByUser(
        @Path("method") methodName: String?,
        @Query("phone_number") phoneNumber: String?
    ): Call<List<TreatmentDto?>?>?

    @GET("/{method}")
            /**Get all doctors methods.
             * @return [DoctorModel]*/
    fun getAllDoctor(
        @Path("method") methodName: String?,
    ): Call<List<DoctorModel?>?>?

    @GET("/{method}")
            /**Get doctor by ID methods.
             * @param id Get Doctor info y ID
             * @return [DoctorModel]*/
    fun getDoctorFromId(
        @Path("method") methodName: String?,
        @Query("id") id: Int?
    ): Call<List<DoctorModel?>?>?

    @GET("/{method}")
            /**Get symptoms methods.
             * @return [SymptomDto]*/
    fun getAllSymptoms(
        @Path("method") methodName: String?,
    ): Call<List<SymptomDto?>?>?

    @GET("/{method}")
            /**Add new treatment method.
             * @param phoneNumber use for get user ID
             * @param startDate Date of start treatment
             * @param symptomsId User choice him symptoms
             * @param soundServerLinkId User record him sound*
             * @return [AuthDto]*/
    fun addTreatment(
        @Path("method") methodName: String?,
        @Query("phone_number") phoneNumber: String?,
        @Query("start_date") startDate: String?,
        @Query("symptoms_id") symptomsId: Int?,
        @Query("sound_server_link_id") soundServerLinkId: Any
    ): Call<List<AuthDto?>?>?

    /**Add conclusion methods.
     * @param treatId Treatment ID where need add conclusion
     * @param concText Conclusion text for treatment
     * @param phoneNumber Phone number for get patient ID
     * @return [AuthDto] */
    @GET("/{method}")
    fun addConclusion(
        @Path("method") methodName: String?,
        @Query("treat_id") treatId: Int?,
        @Query("conc_text") concText: String?,
        @Query("phone_number") phoneNumber: String?
    ): Call<List<AuthDto?>?>?

    /**Delete treatment methods.
     * @param treatId Treatment ID which need delete
     * @return [AuthDto]*/
    @GET("/{method}")
    fun deleteTreatment(
        @Path("method") methodName: String?,
        @Query("treat_id") treatId: Int?,
        @Query("phone_number") phoneNumber: String?
    ): Call<List<AuthDto?>?>?

    @GET("/{method}")
            /**Get symptoms methods.
             * @return [SymptomDto]*/
    fun getSymptomsForUser(
        @Path("method") methodName: String?,
        @Query("treat_id") treatId: Int?
    ): Call<List<SymptomDto?>?>?

    @GET("/{method}")
            /**Get all message from treatment chat methods.
             * @return [MessagesModel]*/
    fun getMessages(
        @Path("method") methodName: String?,
        @Query("treatment_id") treatId: Int?
    ): Call<List<MessageDto?>?>?

    @GET("/{method}")
            /**Send Message.
             * @param treatId Id of open treatment
             * @param text Message text
             * @param messageDateTime Date and time when message be send
             * @param userType Type of user who sending message
             * @return [MessagesModel]*/
    fun sendMessages(
        @Path("method") methodName: String?,
        @Query("treatment_id") treatId: Int?,
        @Query("text") text: String?,
        @Query("link") sound_server_link: String?,
        @Query("message_datetime") messageDateTime: String?,
        @Query("user_type") userType: String?
    ): Call<List<AuthDto?>?>?
}