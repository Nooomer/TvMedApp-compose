package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.nooomer.tvmedapp_compose.api.interfaces.RetrofitFun
import ru.nooomer.tvmedapp_compose.api.models.MessageDto
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.ssm

class MessagesModelView: ViewModel(), PreferenceDataType, RetrofitFun {
    private var result: List<MessageDto>? = null
    val messagesFlow: Flow<List<MessageDto>?> = flow{
        result = withContext(Dispatchers.IO) {
            get(
                "all",
                "messages", "Bearer " + ssm.fetch(USER_ID), "1"
            ) as List<MessageDto>?
        }
        emit(result)
        }
    }