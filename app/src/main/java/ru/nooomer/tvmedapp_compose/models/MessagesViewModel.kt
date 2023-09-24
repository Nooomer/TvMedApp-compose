package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nooomer.tvmedapp_compose.api.models.MessageDto
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.ssm
import java.util.UUID

class MessagesViewModel : ViewModel(), PreferenceDataType {
	private val treatmentId: UUID = UUID.fromString(ssm.fetch(CLICKED_TREATMENT_ID).toString())
	private val messagesFlow = MessagesModelView(treatmentId).messagesFlow

	private val _messages = MutableStateFlow(listOf<MessageDto>())
	val messages: StateFlow<List<MessageDto>?> get() = _messages

	private val _expandedMessagesIdsList = MutableStateFlow(listOf<Int>())
	val expandedMessagesIdsList: StateFlow<List<Int>?> get() = _expandedMessagesIdsList

	init {
		getData()
	}

	fun getData() {
		viewModelScope.launch {
			withContext(Dispatchers.Default) {
				var messagesList: List<MessageDto>? = null
				messagesFlow.collect {
					messagesList = it
				}
				_messages.emit(messagesList!!)
			}
		}
	}

	fun onCardArrowClicked(cardId: Int) {
		_expandedMessagesIdsList.value =
			_expandedMessagesIdsList.value.toMutableList().also { list ->
				if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
			}
	}
}