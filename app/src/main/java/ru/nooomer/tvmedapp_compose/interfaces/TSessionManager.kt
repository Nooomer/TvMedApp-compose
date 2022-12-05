package ru.nooomer.tvmedapp_compose.interfaces

interface TSessionManager: PreferenceDataType {
    fun fetch(param: String):String?
    fun save(param: String, data: String?)
    fun clearSession()
    fun validation():Boolean
}