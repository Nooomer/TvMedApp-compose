package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nooomer.tvmedapp_compose.api.API
import ru.nooomer.tvmedapp_compose.api.models.MessageDto
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import java.util.UUID

class MessagesModelView(treatmentId: UUID): ViewModel(), PreferenceDataType {
    val messagesFlow: Flow<List<MessageDto>?> = flow{
        emit(API.getChat(treatmentId)?.messages?.toMutableList())
        }
    }