package ru.nooomer.tvmedapp_compose.interfaces

interface TSessionManager: PreferenceDataType {
    fun fetch(param: String): Any?
    fun save(param: String, data: Any?)
    fun clearSession()
    fun validation():Boolean
}