package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto
import java.util.UUID

class CardsViewModel : ViewModel() {
    private val treatmentFlow = TreatmentModelView().treatmentFlow


    private val _cards = MutableStateFlow(listOf<TreatmentDto>())
    val cards: StateFlow<List<TreatmentDto>?> get() = _cards

    private val _expandedCardIdsList = MutableStateFlow(listOf<UUID>())
    val expandedCardIdsList: StateFlow<List<UUID>?> get() = _expandedCardIdsList

    init {
        getTreatmentData()
    }

    private fun getTreatmentData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                var treatmentList: List<TreatmentDto>? = null
                treatmentFlow.collect {
                       treatmentList = it
                }
                _cards.emit(treatmentList!!)
            }
        }
    }

    fun onCardArrowClicked(cardId: UUID) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}