package ru.nooomer.tvmedapp_compose.models
/**Patient response model */
data class UserModel(
        val id : Int,
        val surename : String,
        val name : String,
        val s_name : String,
        val phone_number : String,
        val insurance_number : String,
        val password : String,
        val user_type: String
        )
