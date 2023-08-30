package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nooomer.tvmedapp_compose.api.models.MessageDto

class MessagesViewModel : ViewModel() {
    val messagesFlow = MessagesModelView().messagesFlow


    private val _messages = MutableStateFlow(listOf<MessageDto>())
    val messages: StateFlow<List<MessageDto>?> get() = _messages

    private val _expandedMessagesIdsList = MutableStateFlow(listOf<Int>())
    val expandedMessagesIdsList: StateFlow<List<Int>?> get() = _expandedMessagesIdsList

    init {
        getFakeData()
    }

    private fun getFakeData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                var testList: List<MessageDto>? = null
                messagesFlow.collect {
                       testList = it
                }
                _messages.emit(testList!!)
            }
        }
    }

    fun onCardArrowClicked(cardId: Int) {
        _expandedMessagesIdsList.value = _expandedMessagesIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}