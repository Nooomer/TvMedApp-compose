package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.interfaces.RetrofitFun
import ru.nooomer.tvmedapp_compose.ssm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class MessagesModelView: ViewModel(), PreferenceDataType, RetrofitFun {
    private var result: List<MessagesModel>? = null
    val messagesFlow: Flow<List<MessagesModel>?> = flow{
        result = withContext(Dispatchers.IO) {
                        get(
                            "all",
                            "messages", "Bearer " + ssm.fetch(USER_TOKEN), "1"
                        ) as List<MessagesModel>?
                }
                emit(result)
        }
    }