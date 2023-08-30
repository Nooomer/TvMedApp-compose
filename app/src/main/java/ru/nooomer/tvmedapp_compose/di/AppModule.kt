package ru.nooomer.tvmedapp_compose.di


import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.nooomer.tvmedapp_compose.RetrofitService.Common
import ru.nooomer.tvmedapp_compose.RetrofitService.SessionManager

val appModule = module {
    factory {Common(androidContext())}
    single {SessionManager(androidContext())}
}