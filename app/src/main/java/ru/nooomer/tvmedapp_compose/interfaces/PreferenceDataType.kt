package ru.nooomer.tvmedapp_compose.interfaces

interface PreferenceDataType {
    /*val USER_TOKEN: String
        get() = "user_token"*/
    /*val TOKEN_LIFETIME: String
        get() = "token_lifetime"*/
    val LOGIN_STATE: String
        get() = "login_state"
    val SESSION_TIMEOUT: String
        get() = "session_timeout"
    val USER_TYPE: String
        get() = "user_type"
    val USER_ID: String
        get() = "user_id"
}