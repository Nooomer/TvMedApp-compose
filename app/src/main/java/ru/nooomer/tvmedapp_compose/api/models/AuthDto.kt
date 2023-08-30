package ru.nooomer.tvmedapp_compose.api.models

/**Authorization response model. Return [token]
 * @param [token] May be "true" or "false" but it not [Boolean]. Return type - [String]*/
data class AuthDto(
    var id: String,
    var userType: String
)
