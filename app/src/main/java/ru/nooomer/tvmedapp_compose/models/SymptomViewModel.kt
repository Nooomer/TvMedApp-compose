package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nooomer.tvmedapp_compose.api.models.SymptomDto

class SymptomViewModel : ViewModel() {
    private val symptomFlow = SymptomModelView().symptomFlow


    private val _symptoms = MutableStateFlow(listOf<SymptomDto>())
    val symptoms: StateFlow<List<SymptomDto>?> get() = _symptoms

    init {
        getSymptomsData()
    }

    private fun getSymptomsData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                var symptomsList: List<SymptomDto>? = null
                symptomFlow.collect {
                    symptomsList = it
                }
                _symptoms.emit(symptomsList!!)
            }
        }
    }
}